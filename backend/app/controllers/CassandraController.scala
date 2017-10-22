package controllers

import javax.inject.Inject

import connectors.AnalysisDB
import models.AnalysisResults
import play.Logger
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext


final class CassandraController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents) extends AbstractController(cc){

  def list(id: String) = Action.async { implicit req =>
    Logger.debug("Called reading: " + id)
    AnalysisDB.start()

    // read data
    for {
      ans <- AnalysisDB.read(id)
    } yield {
      Ok(Json.toJson(ans))
    }
  }

  def write(id: String) = Action.async { implicit req =>
    Logger.debug("Called reading: " + id)
    AnalysisDB.start()

    // read data for test
    for {
      ans <- AnalysisDB.saveOrUpdate(new AnalysisResults(id, "123","aaa","1"))
    } yield {
      Ok(ans.toString())
    }
  }

}



