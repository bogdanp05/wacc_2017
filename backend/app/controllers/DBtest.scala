package controllers

import javax.inject.Inject

import connectors.{AnalysisDB, MongoDB}
import models.{AnalysisResults, Tweet}
import play.Logger
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext


final class DBtest @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents, mongoDB: MongoDB) extends AbstractController(cc){

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



