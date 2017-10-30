import controllers.TweetController
import org.scalatestplus.play._
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

////test mongodb
//  "" should {
//    ".." in{
//
//      val mongo = new GuiceApplicationBuilder().build().injector.instanceOf[TweetController]
//      val tweets = Await.result(mongo.mongoGetAllTweets(), Duration.Inf)
//      tweets.length must be > 0
//    }
//  }


}