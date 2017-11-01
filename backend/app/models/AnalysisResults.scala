package models

import java.util.Dictionary

import play.api.libs.json.Json

/**
  *
  * This is the Scala representation of Analysis results, following the Datastax example
  */

case class AnalysisResults(
                          id: Long,
                          keyword: String,
                          tweetID: Long,
                          analysis: Int,
                          timestamp: Long
                          )

object AnalysisResults{
  implicit val analysisResultsReads = Json.format[AnalysisResults]
  //  implicit val analysisResultsWrite = Json.writes[AnalysisResults]
}