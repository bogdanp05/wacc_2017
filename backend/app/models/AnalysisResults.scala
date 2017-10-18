package models

/**
  *
  * This is the Scala representation of Analysis results, following the Datastax example
  */

case class AnalysisResults(
                            id: String,
                            keyword: String,
                            tweet: String,
                            value: String
                          )