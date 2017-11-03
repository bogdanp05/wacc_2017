package controllers

import javax.inject.Inject

import connectors.AnalysisDB
import models.{AnalysisResults, Tweet}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.api.libs.json.Json

import play.api.Logger

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future, Promise}
import java.util.UUID

final class CassandraController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, mongoController: MongoDBController) extends AbstractController(cc){

  def list(word: String):Action[AnyContent] = Action.async { implicit req =>
    Logger.debug("Called reading: " + word)
    AnalysisDB.start()
    // read data
    for {
      ans <- AnalysisDB.read(word)
    } yield {
      Ok(Json.toJson(ans))
    }
  }

  def write(id:String):Action[AnyContent] = Action.async {  implicit req =>
    AnalysisDB.start()
    val uuid = UUID.randomUUID().toString
    for {
      ans <- AnalysisDB.saveOrUpdate(new AnalysisResults(uuid, id, "sdfvsef", 1, 123456789))
    } yield {
      Ok(ans.toString)
    }
  }

  /**** NOT URL *****/
  def alreadyAnalyzedWord(word:String):Future[Boolean] = {
    Logger.info("----------here1.1")
    AnalysisDB.start()
    for {
      tweets <- AnalysisDB.read(word)
    } yield !tweets.isEmpty
  }

//  def saveTweetCassandra(word:String, tweetID:Long, analysis:Int, timestamp:Long) = Future {
//    AnalysisDB.start()
//    val timeInMillis = System.currentTimeMillis()
//    AnalysisDB.saveOrUpdate(new AnalysisResults(timeInMillis, word, tweetID, analysis, timestamp))
//    Ok("working")
//  }
  def saveTweetsCassandra(tweets : List[Tweet], word : String) = Future {
    AnalysisDB.start()
    var i = 0
    for (tweet <- tweets){
      val uuid = UUID.randomUUID().toString
      AnalysisDB.saveOrUpdate(new AnalysisResults(uuid, word, tweet.id, tweet.analysis, tweet.timestamp))
      i = i + 1
    }
    Logger.info("------inserted in Cass: " + i)
    //Ok("working")
  }

  def getIdsCassandra(word: String): Future[List[String]] = {
    AnalysisDB.start()
    val ids = ListBuffer[String]()
    AnalysisDB.read(word).map { analysis =>
      for (a <- analysis){
        //Logger.info(a.tweetID.toString)
        ids += a.tweetID
      }
      Logger.info("----------ids from cassandra" + ids.length)
      ids.toList
    }

  }


}
