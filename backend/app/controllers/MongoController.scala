package controllers

import javax.inject.Inject

import connectors.MongoDB
import models.Tweet
import play.Logger
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}


final class MongoController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, mongoDB: MongoDB) extends AbstractController(cc){


  def list_mongo(word: String):Action[AnyContent] = Action.async { implicit req =>
    Logger.debug("Called reading: " + word)

    // read data
    for {
      ans <- mongoDB.read(word)
    } yield {
      Ok(Json.toJson(ans))
    }
  }

  def insert_mongo(word: String):Action[AnyContent] = Action.async { implicit req =>
    Logger.debug("Called reading: " + word)

    // read data for test
    for {
      ans <- mongoDB.insert(new Tweet(10L, 32L, "AAA", word, "", 1))
    } yield {
      Ok(ans.toString())
    }
  }

  /***** NOT URL *****/

  def getTweetsOnMongoWithWord(word:String) = Future {
    var tweetsFuture = mongoDB.read(word)
    tweetsFuture onSuccess {
      case tweets => println(tweets)
    }
    Ok("getting tweets from mongoDB")
  }

  def saveTweetOnMongo(word: String) = Future {
    mongoDB.insert(new Tweet(10L, 32L, "AAA", word, "", 1))
    Ok("Saving on MongoDB")
  }

}
