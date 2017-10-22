package connectors

import Connector.connector
import models._
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.dsl._
import scala.concurrent.Future

/**
  * Database object that wraps the existing table
  * giving the ability to receive the connector
  */

class TweetsAnalysisDB (override val connector: CassandraConnection) extends Database[TweetsAnalysisDB](connector){
  object AnalysisResultsModel extends AnalysisResultsModel with connector.Connector

  /**
    * read an event
    *
    * @param id
    * @return
    */
  def read(id: String): Future[List[AnalysisResults]] = {
    for {
      byAnalysis <- AnalysisResultsModel.getById(id)
    } yield byAnalysis
  }


  /**
    * Save a result in the table
    *
    * @param result
    * @return
    */
  def saveOrUpdate(result: AnalysisResults): Future[ResultSet] = {
    for {
      byAnalysis <- AnalysisResultsModel.store(result).future
    } yield byAnalysis
  }
}

/**
  * This is the database, it connects to a cluster with multiple contact points
  */
object AnalysisDB extends TweetsAnalysisDB(connector)