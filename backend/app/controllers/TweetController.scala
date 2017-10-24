package controllers

import javax.inject._

import akka.actor.ActorSystem

import scala.concurrent.duration._
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future, Promise}
import play.api.mvc._
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import reactivemongo.api.gridfs.{GridFS, ReadFile}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import reactivemongo.play.json.collection._
import models.{SentimentAnalysis, Tweet}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.collection.mutable.ListBuffer

import scala.io.Source

@Singleton
class TweetController @Inject()(val reactiveMongoApi: ReactiveMongoApi ,cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

  type JSONReadFile = ReadFile[JSONSerializationPack.type, JsString]

  val positiveArrayAdjectives = SentimentAnalysis.positiveArrayAdjectives
  val negativeArrayAdjectives = SentimentAnalysis.negativeArrayAdjectives

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

    val result = collection.map(_.find(Json.obj("id"->2))).onComplete(
      results => println("result  "+ results)
    )


    //collection.map(_.insert(Tweet(Some(1.toLong),Some(2.toLong),"a","b","c",0)))

    val filename = "../tweetsdb.csv"
    var index = 0
    for (line <- Source.fromFile(filename, "ISO-8859-1").getLines) {
      index = index + 1
      val cols = line.split(";").map(_.trim)
      /*
      * Tweet Id;Date;Hour;Nickname;Tweet content;Tweet Url
      * 0:ID    1:DATE     2:HOUR     3:NICKNAME     4:CONTENT    5:URL
      * */
      if (cols.length.equals(6)) {
        if (cols(4).toLowerCase.contains("gop")) {
          //println(getSentimentAnalysis(cols(4).toLowerCase))
        }
      }

    }
    getFutureBogdan(2.second).map { msg => Ok(Json.obj("hey/"+index->msg)).enableCors }
  }

  private def getSentimentAnalysis(tweet: String): Int = {
    val words = tweet.split("\\s+")
    var sentimentAnalysisResult:Double = 0
    for(word <- words){
      if (this.positiveArrayAdjectives.contains(word)) {
        sentimentAnalysisResult += 1
      }
      if (this.negativeArrayAdjectives.contains(word)) {
        sentimentAnalysisResult -= 1
      }
    }

    if (sentimentAnalysisResult == 0) {
      return 0
    }
    else if (sentimentAnalysisResult < 0) {
      return -1
    }
    else if (sentimentAnalysisResult > 0) {
      return 1
    }

    return 0
  }


  def getTweets(word: String) = Action {
    val filename = "../tweetsdb.csv"
    val tweets = ListBuffer[Tweet]()
    for (line <- Source.fromFile(filename, "ISO-8859-1").getLines) {
      val cols = line.split(";").map(_.trim)
      /*
      * Tweet Id;Date;Hour;Nickname;Tweet content;Tweet Url
      * 0:ID    1:DATE     2:HOUR     3:NICKNAME     4:CONTENT    5:URL
      * */
      if (cols.length.equals(6)) {
        if (cols(4).toLowerCase.contains(word.toLowerCase)) {
          var dt = DateTime.parse(cols(1)+" "+cols(2), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"))
          var analysis = this.getSentimentAnalysis(cols(4))
          var tweet = Tweet(cols(0).toLong, dt.getMillis(),cols(3),cols(4),cols(5),analysis)
          var tweetJSON = Json.toJson(tweet)
          tweets += tweet
          println(tweet)
          println(tweets.toArray.length)
        }

      }
    }
    println(tweets.toList.length)
    println(tweets.toArray.length)
    Ok(Json.toJson(tweets)).enableCors
  }



  def getTweetsFromMongoDB(word: String): Action[AnyContent] = Action.async { implicit request =>
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

  def getAllTweets(): Action[AnyContent] = Action.async { implicit request =>
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

