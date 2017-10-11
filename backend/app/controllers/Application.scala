package controllers

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
import reactivemongo.api.Cursor

/*
 * Example using ReactiveMongo + Play JSON library.
 *
 * There are two approaches demonstrated in this controller:
 * - using JsObjects directly
 * - using case classes that can be turned into JSON using Reads and Writes.
 *
 * This controller uses JsObjects directly.
 *
 * Instead of using the default Collection implementation (which interacts with
 * BSON structures + BSONReader/BSONWriter), we use a specialized
 * implementation that works with JsObject + Reads/Writes.
 *
 * Of course, you can still use the default Collection implementation
 * (BSONCollection.) See ReactiveMongo examples to learn how to use it.
 */
class Application @Inject()(val reactiveMongoApi: ReactiveMongoApi, cc: ControllerComponents)(implicit exec: ExecutionContext)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

  import MongoController.readFileReads

  type JSONReadFile = ReadFile[JSONSerializationPack.type, JsString]

  // get the collection 'tweets'
  def collection: Future[JSONCollection] = reactiveMongoApi.database.
    map(_.collection[JSONCollection]("tweets"))


  private val gridFS = for {
    fs <- reactiveMongoApi.database.map(db =>
      GridFS[JSONSerializationPack.type](db))
    _ <- fs.ensureIndex().map { index =>
      // let's build an index on our gridfs chunks collection if none
      Logger.info(s"Checked index, result is $index")
    }
  } yield fs

  //   list all articles and sort them
  def getTweets: Action[AnyContent] = Action.async { implicit request =>
    // get a sort document (see getSort method for more information)
    //val sort: JsObject = getSort(request).getOrElse(Json.obj())

    // the cursor of documents
    //val found = collection.map(_.find(Json.obj()).sort(sort).cursor[Tweet]())
    val found = collection.map(_.find(Json.obj()).cursor[Tweet]())
// cursor.collect[Vector](3, Cursor.FailOnError())
    // build (asynchronously) a list containing all the articles
    //
    found.flatMap(_.collect[List]()).map { tweets =>
      Ok(Json.toJson(tweets))
    }.recover {
      case e =>
        e.printStackTrace()
        BadRequest(e.getMessage())
    }
  }



  def delete(id: String) = Action.async {
    // let's collect all the attachments matching that match the article to delete
    (for {
      fs <- gridFS
      files <- fs.find[JsObject, JSONReadFile](
        Json.obj("tweet" -> id)).collect[List]()
      _ <- {
        // for each attachment, delete their chunks and then their file entry
        def deletions = files.map(fs.remove(_))

        Future.sequence(deletions)
      }
      coll <- collection
      _ <- {
        // now, the last operation: remove the article
        coll.remove(Json.obj("_id" -> id))
      }
    } yield Ok).recover { case _ => InternalServerError }
  }


//  private def getSort(request: Request[_]): Option[JsObject] =
//    request.queryString.get("sort").map { fields =>
//      val sortBy = for {
//        order <- fields.map { field =>
//          if (field.startsWith("-"))
//            field.drop(1) -> -1
//          else field -> 1
//        }
//        if order._1 == "date"
//      } yield order._1 -> implicitly[Json.JsValueWrapper](Json.toJson(order._2))
//
//      Json.obj(sortBy: _*)
//    }
}

