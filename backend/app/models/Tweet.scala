package models

import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints.pattern

case class Tweet(
                  id: Option[String],
                  content: String,
                  author: String,
                  creationDate: Option[DateTime])


// Turn off your mind, relax, and float downstream
// It is not dying...
object Tweet {
  import play.api.libs.json._

  implicit object ArticleWrites extends OWrites[Tweet] {
    def writes(tweet: Tweet): JsObject = Json.obj(
      "_id" -> tweet.id,
      "content" -> tweet.content,
      "author" -> tweet.author,
      "creationDate" -> tweet.creationDate.fold(-1L)(_.getMillis))
  }

  implicit object ArticleReads extends Reads[Tweet] {
    def reads(json: JsValue): JsResult[Tweet] = json match {
      case obj: JsObject => try {
        val id = (obj \ "_id").asOpt[String]
        val content = (obj \ "content").as[String]
        val author = (obj \ "author").as[String]
        val creationDate = (obj \ "creationDate").asOpt[Long]

        JsSuccess(Tweet(id, content, author, creationDate.map(new DateTime(_))))

      } catch {
        case cause: Throwable => JsError(cause.getMessage)
      }

      case _ => JsError("expected.jsobject")
    }
  }

  val form = Form(
    mapping(
      "id" -> optional(text verifying pattern(
        """[a-fA-F0-9]{24}""".r, error = "error.objectId")),
      "content" -> text,
      "author" -> nonEmptyText,
      "creationDate" -> optional(longNumber)) {
      (id, content, author, creationDate) =>
        Tweet(
          id,
          content,
          author,
          creationDate.map(new DateTime(_)))
    } { tweet =>
      Some(
        (tweet.id,
          tweet.content,
          tweet.author,
          tweet.creationDate.map(_.getMillis)))
    })
}


