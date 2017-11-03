package connectors

import Connector.connector
import models._
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.dsl._
import play.api.Logger

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * Database object that wraps the existing table
  * giving the ability to receive the connector
  */

class TweetsAnalysisDB (override val connector: CassandraConnection) extends Database[TweetsAnalysisDB](connector){


  object AnalysisResultsModel extends AnalysisResultsModel with connector.Connector

  /**
    * read an event
    *
    * @param word
    * @return
    */
  def read(word: String): Future[List[AnalysisResults]] =  {
    Logger.info("I am here")
    for {
      byAnalysis <- AnalysisResultsModel.getById(word)
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

  /**
    * Create table if not exist
    *
    */

  def start()={
    Logger.info("Create table if not exist")
    Await.ready(AnalysisResultsModel.create.ifNotExists().future(), Duration.Inf)
  }


}

/**
  * This is the database, it connects to a cluster with multiple contact points
  */
object AnalysisDB extends TweetsAnalysisDB(connector)