package models

import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints.pattern
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, Macros}
import reactivemongo.bson.Macros.Annotations.Key

case class Tweet(
                  id: Long,
                  timestamp: Long,
                  nickname: String,
                  content: String,
                  url: String,
                  analysis: Int)


// Turn off your mind, relax, and float downstream
// It is not dying...
object Tweet {
  import play.api.libs.json._

  implicit val bsonRead: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
  implicit val bsonWrite: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]

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
        val id = (obj \ "id").as[Long]
        val timestamp = (obj \ "timestamp").as[Long]
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
      "id" -> longNumber,
      "timestamp" -> longNumber,
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


