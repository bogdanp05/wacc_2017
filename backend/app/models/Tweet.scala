package models

import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints.pattern

case class Tweet(
                  id: Option[Long],
                  timestamp: Option[Long],
                  nickname: String,
                  content: String,
                  url: String,
                  analysis: Int)


// Turn off your mind, relax, and float downstream
// It is not dying...
object Tweet {
  import play.api.libs.json._

  implicit object ArticleWrites extends OWrites[Tweet] {
    def writes(tweet: Tweet): JsObject = Json.obj(
      "id" -> tweet.id,
      "timestamp" -> tweet.timestamp,
      "nickname" -> tweet.nickname,
      "content" -> tweet.content,
      "url" -> tweet.url,
      "analysis" -> tweet.analysis
    )
  }

  implicit object ArticleReads extends Reads[Tweet] {
    def reads(json: JsValue): JsResult[Tweet] = json match {
      case obj: JsObject => try {
        val id = (obj \ "id").asOpt[Long]
        val timestamp = (obj \ "timestamp").asOpt[Long]
        val nickname = (obj \ "nickname").as[String]
        val content = (obj \ "content").as[String]
        val url = (obj \ "url").as[String]
        val analysis = (obj \ "analysis").as[Int]

        JsSuccess(Tweet(id, timestamp, nickname, content, url, analysis))

      } catch {
        case cause: Throwable => JsError(cause.getMessage)
      }

      case _ => JsError("expected.jsobject")
    }
  }

  val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "timestamp" -> optional(longNumber),
      "nickname" -> nonEmptyText,
      "content" -> text,
      "url" -> nonEmptyText,
      "analysis" -> number) {
      (id, timestamp, nickname, content, url, analysis) =>
        Tweet(id, timestamp,nickname,content, url, analysis)
    } { tweet =>
      Some(
        (tweet.id,
          tweet.timestamp,
          tweet.nickname,
          tweet.content,
          tweet.url,
          tweet.analysis
        ))
    })
}


