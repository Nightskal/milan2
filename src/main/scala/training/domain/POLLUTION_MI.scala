package training.domain


import org.apache.spark.sql.types._

object POLLUTION_MI extends Enumeration {

  private val DELIMITER = ","

  val SENSOR_ID, TIME_INSTANT,MEASUREMENT = Value

  val structType = StructType(
    Seq(
      StructField(SENSOR_ID.toString, IntegerType),
      StructField(TIME_INSTANT.toString, TimestampType),
      StructField(MEASUREMENT.toString, DoubleType)
    )
  )
}

case class POLLUTION_MI_Case(sensor_id:Int,
                             time_instant:TimestampType,
                             measurement:Double)
