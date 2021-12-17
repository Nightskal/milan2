package training.system

import training.domain._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType

object Parameters {
  val POPULATION_DATASET_PATH = ""
  val EXAMPLE_OUTPUT_PATH = "./output/"


  val path_TELECOMMUNICATIONS_MI  = "./dataset/months/december/sms-call-internet-mi-2013-12-01.txt"
  val path_LEGEND = "./dataset/pollution-legend-mi.csv"
  val path_POLLUTION_MI = "./dataset/pollution-mi/*"

  val table_TELECOMMUNICATIONS_MI  = "telecom"
  val table_LEGEND = "legend"
  val table_POLLUTION_MI = "pollution_mi"

  private def createTableForCSV(name: String, structType: StructType, path: String, delimiter: String = ",")
                         (implicit spark: SparkSession): Unit = {
    spark.read
      .format("com.databricks.spark.csv")
      //.option("inferSchema", "true")
      .options(
        Map(
          "delimiter" -> delimiter,
          "nullValue" -> "\\N"
        )
      )
      .option("timestampFormat","yyyy/MM/dd HH:mm")
      .schema(structType).csv(path).createOrReplaceTempView(name)
  }
  private def createTableForTXT(name: String, structType: StructType, path: String, delimiter: String = "\\t")
                         (implicit spark: SparkSession): Unit = {
    spark.read
      .format("com.databricks.spark.csv")
      //.option("inferSchema", "true")
      .options(
        Map(
          "delimiter" -> delimiter,
          "nullValue" -> "\\N"
        )
      ).schema(structType).csv(path).createOrReplaceTempView(name)
  }

  def initTables(implicit spark: SparkSession): Unit = {
    createTableForCSV(Parameters.table_POLLUTION_MI, POLLUTION_MI.structType,Parameters.path_POLLUTION_MI)
    createTableForTXT(Parameters.table_TELECOMMUNICATIONS_MI, TELECOMMUNICATIONS_MI.structType, Parameters.path_TELECOMMUNICATIONS_MI)
    createTableForCSV(Parameters.table_LEGEND, LEGEND.structType, Parameters.path_LEGEND)
  }
}
