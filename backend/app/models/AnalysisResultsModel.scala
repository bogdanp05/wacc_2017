package models

import com.outworkers.phantom.dsl._
import scala.concurrent.Future

abstract class AnalysisResultsModel extends Table[AnalysisResultsModel, AnalysisResults]{

  override def tableName: String = "Analysis"

  object id extends LongColumn with PrimaryKey
  object keyword extends StringColumn with PartitionKey
  object tweetID extends LongColumn
  object analysis extends IntColumn
  object timestamp extends LongColumn

  override def fromRow(row: Row): AnalysisResults = {
    AnalysisResults(
      id(row),
      keyword(row),
      tweetID(row),
      analysis(row),
      timestamp(row)
    )
  }


  def getById(word: String): Future[List[AnalysisResults]] = {
    select
      .where(_.keyword eqs word)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .fetch()
  }

  def getByIdAndKeyword(id: String, keyword: String): Future[Option[AnalysisResults]] = {
    select
      .where(_.keyword eqs id).and(_.keyword eqs keyword)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .one()
  }

}
