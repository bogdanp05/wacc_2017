package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.mvc._
import play.api.libs.json.Json

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future, Promise}
import play.api.Logger
import play.api.mvc._
import play.api.libs.json.{JsObject, JsString, Json}
import reactivemongo.api.gridfs.{GridFS, ReadFile}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import reactivemongo.play.json.collection._
import models.Tweet
import play.modules.reactivemongo._
import MongoController.readFileReads

@Singleton
class TweetController @Inject()(val reactiveMongoApi: ReactiveMongoApi ,cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

  type JSONReadFile = ReadFile[JSONSerializationPack.type, JsString]

  // get the collection 'tweets'
  def collection: Future[JSONCollection] = reactiveMongoApi.database.
    map(_.collection[JSONCollection]("tweets"))

  def message = Action.async {
    getFutureMessage(1.second).map { msg => Ok(msg) }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

  def bogdan = Action.async {
    getFutureBogdan(2.second).map { msg => Ok(Json.obj("ok"->msg)).enableCors }
  }

  def getTweets(word: String): Action[AnyContent] = Action.async { implicit request =>
    val found = collection.map(_.find(Json.obj()).cursor[Tweet]())
    found.flatMap(_.collect[List]()).map { tweets =>
      var tweetsJSON = Json.toJson(tweets)
      Ok(tweetsJSON).enableCors
    }.recover {
      case e =>
        e.printStackTrace()
        BadRequest(e.getMessage())
    }
  }


  private def getFutureBogdan(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Bogdan async2")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

  implicit class RichResult (result: Result) {
    def enableCors =  result.withHeaders(
      "Access-Control-Allow-Origin" -> "*"
      , "Access-Control-Allow-Methods" -> "OPTIONS, GET, POST, PUT, DELETE, HEAD"   // OPTIONS for pre-flight
      , "Access-Control-Allow-Headers" -> "Accept, Content-Type, Origin, X-Json, X-Prototype-Version, X-Requested-With" //, "X-My-NonStd-Option"
      , "Access-Control-Allow-Credentials" -> "true"
    )
  }


}
