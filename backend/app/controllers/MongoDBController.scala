package controllers

import javax.inject.Inject

import connectors.MongoDB
import models.Tweet
import play.api.Logger
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.api.libs.json.Json

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}


final class MongoDBController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, mongoDB: MongoDB) extends AbstractController(cc){


  def list_mongo(tweetID: Long):Action[AnyContent] = Action.async { implicit req =>
    Logger.debug("Called reading: " + tweetID)

    // read data
    for {
      ans <- mongoDB.read(tweetID)
    } yield {
      Ok(Json.toJson(ans))
    }
  }

  def insert_mongo(word: String):Action[AnyContent] = Action.async { implicit req =>
    Logger.debug("Called on writting: " + word)

    // read data for test
    for {
      ans <- mongoDB.insert(new Tweet("sdvsvgsvg", 32L, "AAA", word, "", 1))
    } yield {
      Ok(ans.toString())
    }
  }


  /***** NOT URL *****/

  /*def getTweetsOnMongoWithWord(word:String) = Future {
    var tweetsFuture = mongoDB.read(tweetID)
    tweetsFuture onSuccess {
      case tweets => println(tweets)
    }
    Ok("getting tweets from mongoDB")
  }*/

//  def saveTweetMongo(tweetID: Long, timestamp: Long, nickname: String, content: String, url: String, analysis: Int) = Future {
//    mongoDB.insert(new Tweet(tweetID, timestamp, nickname, content, url, analysis))
//    Ok("Saving on MongoDB")
//  }
  def saveTweetsMongo(tweets : List[Tweet]) = Future {
    var i = 0
    for (tweet <- tweets){
      mongoDB.insert(tweet)
      i = i + 1
    }
    Logger.info("------inserted in Mongo: " + i)
    //mongoDB.insert(new Tweet(tweetID, timestamp, nickname, content, url, analysis))
    //Ok("Saving on MongoDB")
  }

  def getTweetsFromMongo(ids: List[String]): Future[List[Tweet]] =  {
    val retTweets = ListBuffer[Tweet]()
    mongoDB.readList(ids).map {tweets =>
      for (tweet <- tweets){
        //Logger.info(tweet.content)
        retTweets += tweet
      }
      Logger.info("----------tweets from mongo" + retTweets.length)
      retTweets.toList
    }

  }

}
