import connectors.MongoDB
import controllers.TweetController
import models.Tweet
import org.scalatestplus.play._
import play.Logger
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {
    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }
  }


  "HomeController" should {
    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")
      contentAsString(home) must include ("Your new app is ready")
    }
  }


  //test mongodb
  "MongoDB" should {
    "insert and read info" in{

      val test = new Tweet(10L, 32L, "AAA", "dog", "", 1)

      val mongo = new GuiceApplicationBuilder().build().injector.instanceOf[MongoDB]

      mongo.insert(test)
      Thread.sleep(2000)

      val read_tweets = Await.result(mongo.read(test.content), Duration.Inf)
      Logger.debug(read_tweets.toString)
      read_tweets.get.id mustBe 10L



    }
  }



}