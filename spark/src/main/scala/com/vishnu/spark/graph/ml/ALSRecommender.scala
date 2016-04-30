package com.vishnu.spark.graph.ml

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS,MatrixFactorizationModel,Rating}

object ALSRecommender {
  
  def main(args: Array[String]): Unit = {
    //conf
    val conf = new SparkConf().setAppName("MovieRecommender")
    val sc = new SparkContext(conf)
    
    //load data
    val ratingText = sc.textFile("/mapr_lab_data/data/ratings.dat")
    val ratingsRDD = ratingText.map(parseRating).cache()
    
    //split into training and testing set
    val splits = ratingsRDD.randomSplit(Array(0.8,0.2),0L)
    val trainingRatingsRDD = splits(0).cache
    val testRatingsRDD = splits(1).cache
    
    //buid ALS model
    val model = (new ALS().setRank(20).setIterations(10).run(trainingRatingsRDD))
    
    val testUserProductRDD = testRatingsRDD.map{ case Rating(user,product,rating) => (user,product)}
    
    val predictionsRDD = model.predict(testUserProductRDD)
    
    val predictionsKeyed = predictionsRDD.map{case Rating(user,prod,pred) => ((user,prod),pred)}
    val testUserKeyed = testRatingsRDD.map{case Rating(user,prod,rating) => ((user,prod),rating)}
    
    val testAndPred = testUserKeyed.join(predictionsKeyed)
    
    //find false positive, if predicted high (>4) and actual was low (<1)
    val falsePositives = testAndPred.filter{case ((user,prod),(rating,pred)) => rating <= 1 && pred >= 4}
    
    //MAE (mean absolute error)
    val absoluteError = testAndPred.map{case ((user,prod),(rating,pred)) => Math.abs(pred-rating)}
    val mean = absoluteError.mean()
  }
  
  def parseRating(str: String): Rating = {
    val p = str.split("::")
    Rating(p(0).toInt,p(1).toInt,p(2).toDouble)
  }
  case class Movie(movieId: Int, title: String)
  case class User(userId: Int, gender: String, age: Int, occupation: Int, zip: String)
  //case class Rating(user:Int, movie: Int, rating: Double) no need of this since spark ml lib package is having Rating class
  
}