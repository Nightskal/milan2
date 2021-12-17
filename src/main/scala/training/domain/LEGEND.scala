package training.domain


import org.apache.spark.sql.types._

object LEGEND extends Enumeration {

  private val DELIMITER = ","

  val SENSOR_ID, SENSOR_STREET_NAME, SENSOR_LAT,SENSOR_LONG,SENSOR_TYPE,UOM,TIME_INSTANT_FORMAT= Value

  val structType = StructType(
    Seq(
      StructField(SENSOR_ID.toString, IntegerType),
      StructField(SENSOR_STREET_NAME.toString, StringType),
      StructField(SENSOR_LAT.toString, DoubleType),
      StructField(SENSOR_LONG.toString, DoubleType),
      StructField(SENSOR_TYPE.toString,  StringType),
      StructField(UOM.toString,  StringType),
      StructField(TIME_INSTANT_FORMAT.toString,  StringType)
    )
  )
}

case class LEGEND_Case(sensor_id:Int,
                       sensor_street_name:String,
                                      sensor_lat:Double,
                                      sensor_long:Double,
                                      sensor_type:String,
                                      uom:String,
                                      time_instant_format:String)
