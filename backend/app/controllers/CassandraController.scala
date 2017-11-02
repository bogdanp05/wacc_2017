package controllers

import javax.inject.Inject

import connectors.{AnalysisDB, MongoDB}
import models.{AnalysisResults, Tweet}
import play.Logger
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.api.libs.json.Json
import java.util.UUID

import scala.concurrent.{ExecutionContext, Future, Promise}


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
    val timeInMillis = System.currentTimeMillis()
    for {
      ans <- AnalysisDB.saveOrUpdate(new AnalysisResults(timeInMillis, id, 123, 1, 123456789))
    } yield {
      Ok(ans.toString)
    }
  }

  /**** NOT URL *****/
  def alreadyAnalyzedWord(word:String):Future[Boolean] = {
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

    for (tweet <- tweets){
      val timeInMillis = System.currentTimeMillis()
      AnalysisDB.saveOrUpdate(new AnalysisResults(timeInMillis, word, tweet.id, tweet.analysis, tweet.timestamp))
    }
    Ok("working")
  }
}
