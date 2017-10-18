package models

import com.outworkers.phantom.dsl._
import scala.concurrent.Future

abstract class AnalysisResultsModel extends Table[AnalysisResultsModel, AnalysisResults]{

  override def tableName: String = "Analysis"

  object id extends StringColumn with PartitionKey {
    override lazy val name = "Analysis_id"
  }

  object keyword extends StringColumn with PrimaryKey
  object tweet extends StringColumn
  object value extends StringColumn

  override def fromRow(row: Row): AnalysisResults = {
    AnalysisResults(
      id(row),
      keyword(row),
      tweet(row),
      value(row)
    )
  }


  def getById(id: String): Future[List[AnalysisResults]] = {
    select
      .where(_.id eqs id)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .fetch()
  }

  def getByIdAndKeyword(id: String, keyword: String): Future[Option[AnalysisResults]] = {
    select
      .where(_.id eqs id).and(_.keyword eqs keyword)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .one()
  }

}
