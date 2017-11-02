package controllers

import javax.inject.Inject

import connectors.{AnalysisDB, MongoDB}
import models.{AnalysisResults, Tweet}
import play.Logger
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}


final class CassandraController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents) extends AbstractController(cc){

  def list(id: String):Action[AnyContent] = Action.async { implicit req =>
    Logger.debug("Called reading: " + id)
    AnalysisDB.start()
    // read data
    for {
      ans <- AnalysisDB.read(id)
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

  def saveTweetOnCassandra(word:String, tweetID:Long, analysis:Int, timestamp:Long) = Future {
    AnalysisDB.start()
    val timeInMillis = System.currentTimeMillis()
    AnalysisDB.saveOrUpdate(new AnalysisResults(timeInMillis, word, tweetID, analysis, timestamp))
    Ok("working")
  }

  def getTweetsOnCassandraByWord(word:String) = Future {
    AnalysisDB.start()
    val analysisResultFuture = AnalysisDB.read(word)

    analysisResultFuture onSuccess {
      case analysisResults => handleListAnalysisResults(analysisResults)
    }
    Ok("reading from cassandra")
  }

  def handleListAnalysisResults(analysisResults: List[AnalysisResults])= {
    for(analysisResult <- analysisResults) {

      println(analysisResult)
    }
  }



}
