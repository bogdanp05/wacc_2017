package controllers

import java.util.Dictionary
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
import models.{AnalysisResults, SentimentAnalysis, Tweet}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.collection.mutable.ListBuffer
import play.api.Logger

import scala.collection.mutable.ListBuffer
import scala.io.Source
import connectors.AnalysisDB
import connectors.MongoDB

@Singleton
class TweetController @Inject()(val reactiveMongoApi: ReactiveMongoApi ,cc: ControllerComponents, actorSystem: ActorSystem,
                                mongoDB: MongoDB, cassandraController: CassandraController, mongoController: MongoController)
                               (implicit exec: ExecutionContext)
  extends AbstractController(cc) with ReactiveMongoComponents {

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
    cassandraController.saveTweetOnCassandra("alex",1234,1,123456789)
    getFutureBogdan(2.second).map { msg => Ok(Json.obj("hey"->msg)).enableCors }
  }

  def bogdan2 = Action.async {
    cassandraController.getTweetsOnCassandraByWord("dog")
    getFutureBogdan(1.second).map { msg => Ok(Json.obj("hey"->msg)).enableCors }
  }

  private def getFutureBogdan(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Bogdan async2")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
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
    val currentDirectory = new java.io.File(".").getCanonicalPath
    //Use Logger instead of println
    Logger.info("--------" + currentDirectory)

    //val filename = "../tweetsdb.csv"
    // This is the path in the docker container. If you want to develop without docker,
    // you can change back the path everytime, or put the file on your machine at the same path
    val filename = "/var/lib/dataset/tweetsdb.csv"
    val tweets = ListBuffer[Tweet]()
    for (line <- Source.fromFile(filename, "ISO-8859-1").getLines) {
      val cols = line.split(";").map(_.trim)
      /* 0:ID    1:DATE     2:HOUR     3:NICKNAME     4:CONTENT    5:URL */
      if (cols.length.equals(6)) {
        if (this.tweetContainsWord(cols(4), word)) {
          val dt = DateTime.parse(cols(1)+" "+cols(2), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"))
          val analysis = this.getSentimentAnalysis(cols(4))
          val tweet = Tweet(cols(0).toLong, dt.getMillis(),cols(3),cols(4),cols(5),analysis)
          //save tweets to cassandra
          cassandraController.saveTweetOnCassandra(word,tweet.id,tweet.analysis,tweet.timestamp)
          //save tweets on mongoDB

          val tweetJSON = Json.toJson(tweet)
          tweets += tweet
        }
      }
    }
    Ok(Json.toJson(tweets)).enableCors
  }

  def tweetContainsWord(tweet: String, wordToSearch: String): Boolean = {
    val words = tweet.split("\\s+")
    for(word <- words){
      if (word.toLowerCase.equals(wordToSearch.toLowerCase)
        || word.toLowerCase.equals("#"+wordToSearch.toLowerCase)
        || word.toLowerCase.equals("@"+wordToSearch.toLowerCase)) {
        return true
      }
    }
    return false
  }

  // select tweets from mongodb
  def mongoGetTweets() = {
    val found = collection.map(_.find(Json.obj()).cursor[Tweet]())
    found.flatMap(_.collect[List]())
}

  def getTweetsFromMongoDB(word: String): Action[AnyContent] = Action.async { implicit request =>
    val found = mongoGetTweets()

    found.map { tweets =>
      val tweetsJSON = Json.toJson(tweets)
      Ok(tweetsJSON).enableCors
    }.recover {
      case e =>
        e.printStackTrace()
        BadRequest(e.getMessage())
            }
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

