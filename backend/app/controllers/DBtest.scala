package controllers

import javax.inject.Inject

import connectors.{AnalysisDB, MongoDB}
import models.{AnalysisResults, Tweet}
import akka.actor.FSM.Failure
import akka.actor.Status.Success
import connectors.AnalysisDB
import models.AnalysisResults
import play.Logger
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

final class CassandraController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents) extends AbstractController(cc){

  def list(id: String): Action[AnyContent] = Action.async { implicit req =>
    Logger.debug("Called reading: " + id)
    AnalysisDB.start()
    // read data
    for {
      ans <- AnalysisDB.read(id)
    } yield {
      Ok(Json.toJson(ans))
    }
  }

    def write(id: String): Action[AnyContent] = Action.async { implicit req =>
      Logger.debug("Called reading: " + id)
      AnalysisDB.start()

      val timeInMillis = System.currentTimeMillis()

      for {
        ans <- AnalysisDB.saveOrUpdate(new AnalysisResults(timeInMillis,id,123,1,123456789))

      } yield {
        Ok(ans.toString())
      }
  }

  def list_mongo(word: String) = Action.async { implicit req =>
    Logger.debug("Called reading: " + word)

    // read data
    for {
      ans <- mongoDB.read(word)
    } yield {
      Ok(Json.toJson(ans))
    }
  }


  def insert_mongo(word: String) = Action.async { implicit req =>
    Logger.debug("Called reading: " + word)

    // read data for test
    for {
      ans <- mongoDB.insert(new Tweet(10L, 32L, "AAA", word, "", 1))
    } yield {
      Ok(ans.toString())
    }
  }

}



