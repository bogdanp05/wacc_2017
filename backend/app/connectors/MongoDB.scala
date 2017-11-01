package connectors

import javax.inject.{Inject, Singleton}

import models.{Tweet}
import play.modules.reactivemongo.{ReactiveMongoApi}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.core.errors.DatabaseException

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MongoDB @Inject()(implicit ec: ExecutionContext, val reactiveMongoApi: ReactiveMongoApi){

  private val COLLECTION = "tweets"
  private val TWEETS = "content"


  private def Collection: Future[BSONCollection] = reactiveMongoApi.database.map(_.collection(COLLECTION))


  def insert(tweet: Tweet): Future[Unit] = {
    Collection.flatMap(_.insert(tweet))
      .map(result => (assert(result.ok)))
      .recoverWith {
        case _: DatabaseException => Future()
      }
  }

  def read(word: String): Future[Option[Tweet]] = {
    Collection.flatMap(_
      .find(BSONDocument(TWEETS -> word ))
      .one[Tweet]
    )
  }


}
