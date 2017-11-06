package models

import play.api.libs.json.Json

/**
  *
  * This is the Scala representation of Analysis results, following the Datastax example
  */

case class AnalysisResults(
                          id: String,
                          keyword: String,
                          tweetID: String,
                          analysis: Int,
                          timestamp: Long
                          )

object AnalysisResults{
  implicit val analysisResultsReads = Json.format[AnalysisResults]
  //  implicit val analysisResultsWrite = Json.writes[AnalysisResults]
}