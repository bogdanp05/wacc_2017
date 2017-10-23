package controllers

import javax.inject._

import akka.actor.ActorSystem

import scala.concurrent.duration._

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future, Promise}
import play.api.mvc._
import play.api.libs.json.{JsObject, JsString, Json}
import reactivemongo.api.gridfs.{GridFS, ReadFile}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import reactivemongo.play.json.collection._
import models.Tweet

@Singleton
class TweetController @Inject()(val reactiveMongoApi: ReactiveMongoApi ,cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

  type JSONReadFile = ReadFile[JSONSerializationPack.type, JsString]

  val positiveArrayAdjectives = Array("adaptable", "adventurous", "affable", "affectionate", "agreeable", "ambitious",
    "amiable", "amicable", "amusing", "brave", "bright", "broad-minded", "calm", "careful", "charming",
    "communicative", "compassionate ", "conscientious", "considerate", "convivial", "courageous", "courteous",
    "creative", "decisive", "determined", "diligent", "diplomatic", "discreet", "dynamic", "easygoing",
    "emotional", "energetic", "enthusiastic", "exuberant", "fair-minded", "faithful", "fearless", "forceful",
    "frank", "friendly", "funny", "generous", "gentle", "good", "gregarious", "hard-working", "helpful",
    "honest", "humorous", "imaginative", "impartial", "independent", "intellectual", "intelligent", "intuitive",
    "inventive", "kind", "loving", "loyal", "modest", "neat", "nice", "optimistic", "passionate",
    "patient", "persistent ", "pioneering", "philosophical", "placid", "plucky", "polite", "powerful", "practical",
    "pro-active", "quick-witted", "quiet", "rational", "reliable", "reserved", "resourceful", "romantic", "",
    "self-confident", "self-disciplined", "sensible", "sensitive", "shy", "sincere", "sociable", "straightforward",
    "sympathetic", "thoughtful", "tidy", "tough", "unassuming", "understanding", "versatile", "",
    "warmhearted", "willing", "witty")

  val negativeArrayAdjectives = Array("aggressive", "aloof", "arrogant", "belligerent", "big-headed", "bitchy",
    "boastful", "bone-idle", "boring", "bossy", "callous", "cantankerous", "careless", "changeable", "clinging",
    "compulsive", "conservative", "cowardly", "cruel", "cunning", "cynical", "deceitful", "detached", "dishonest",
    "dogmatic", "domineering", "finicky", "flirtatious", "foolish", "foolhardy", "fussy", "greedy", "grumpy", "gullible",
    "harsh", "impatient", "impolite", "impulsive", "inconsiderate", "inconsistent", "indecisive", "indiscreet",
    "inflexible", "interfering", "intolerant", "irresponsible", "jealous", "lazy", "Machiavellian", "materialistic",
    "mean", "miserly", "moody", "narrow-minded", "nasty", "naughty", "nervous", "obsessive", "obstinate", "overcritical",
    "overemotional", "parsimonious", "patronizing", "perverse", "pessimistic", "pompous", "possessive", "pusillanimous",
    "quarrelsome", "quick-tempered", "resentful", "rude", "ruthless", "sarcastic", "secretive", "selfish", "self-centred",
    "self-indulgent", "silly", "sneaky", "stingy", "stubborn", "stupid", "superficial", "tactless", "timid", "touchy",
    "thoughtless", "truculent", "unkind", "unpredictable", "unreliable", "untidy", "untrustworthy", "vague", "vain",
    "vengeful", "vulgar", "weak-willed", "shit", "fuck", "asshole", "motherfucker", "fuck")


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


    getFutureBogdan(2.second).map { msg => Ok(Json.obj("hey/"+getSentimentAnalysis("shit witty")->msg)).enableCors }
  }

  private def getSentimentAnalysis(tweet: String): Double = {
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
