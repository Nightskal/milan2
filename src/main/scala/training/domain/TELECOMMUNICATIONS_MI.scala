package training.domain


import org.apache.spark.sql.types._

object TELECOMMUNICATIONS_MI extends Enumeration {

  private val DELIMITER = ","

  val SQUARE_ID, TIME_INTERVAL, COUNTRY_CODE, SMS_IN_ACTIVITY,SMS_OUT_ACTIVITY,CALL_IN_ACTIVITY,CALL_OUT_ACTIVITY,INTERNET_TRAFFIC_ACTIVITY = Value

  val structType = StructType(
    Seq(
      StructField(SQUARE_ID.toString, IntegerType),
      StructField(TIME_INTERVAL.toString, LongType),
      StructField(COUNTRY_CODE.toString, IntegerType),
      StructField(SMS_IN_ACTIVITY.toString, DoubleType),
      StructField(SMS_OUT_ACTIVITY.toString,  DoubleType),
      StructField(CALL_IN_ACTIVITY.toString,  DoubleType),
      StructField(CALL_OUT_ACTIVITY.toString,  DoubleType),
      StructField(INTERNET_TRAFFIC_ACTIVITY.toString,  DoubleType)
    )
  )
}

case class TELECOMMUNICATIONS_MI_Case(square_id:Int,
                                      time_interval:Long,
                                      country_code:Int,
                                      sms_in_activity:Double,
                                      sms_out_activity:Double,
                                      call_in_activity:Double,
                                      call_out_activity:Double,
                                      internet_traffic_activity:Double)
