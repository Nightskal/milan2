
package training

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import training.system.Parameters
import training.domain._


object Lab1 extends App {
 implicit val spark: SparkSession = SparkSession
   .builder()
   .master("local[*]")
   .appName("lab1")
   .config("spark.sql.broadcastTimeout", "3600")
   .getOrCreate()
 spark.sparkContext.setLogLevel("WARN")
 import spark.implicits._
 val params = Parameters
 params.initTables

   //SGT_1
  val windowSpecAgg  = Window.partitionBy()
 val telecomWork = spark.table("telecom")
   .select('square_id,hour(from_unixtime(col("time_interval")/1000L)) as "Hours",'sms_in_activity,'sms_out_activity,'call_in_activity,'call_out_activity,'internet_traffic_activity)
   .filter('Hours.between(9,12) || 'Hours.between(15,16))
   .groupBy('square_id)
   .agg(
    (sum('sms_in_activity)+sum('sms_out_activity)+sum('call_in_activity)+sum('call_out_activity)+sum('internet_traffic_activity)) as "SumOfAll"
   )
   .withColumn("avg", avg(col("SumOfAll")).over(windowSpecAgg))
   .withColumn("min", min(col("SumOfAll")).over(windowSpecAgg))
   .withColumn("max", max(col("SumOfAll")).over(windowSpecAgg))
   .withColumn("type",when('SumOfAll<'avg && 'SumOfAll>='min,"relaxDistrict").when('SumOfAll>='avg && 'SumOfAll<='max,"workDistrict"))
   .drop("SumOfAll")
   .drop("avg")



  //SGT_2

   val milano_grid = spark.read.option("multiline", true).json("./dataset/milano-grid/milano-grid.geojson")
     .select(explode($"features") alias "feature")
     .withColumn("coord",explode($"feature.geometry.coordinates"))
     .withColumn("square_id1",$"feature.properties.cellId")
     .withColumn("p1_1",$"coord"(0)(0))
     .withColumn("p1_2",$"coord"(0)(1))
    // .withColumn("p2_1",$"coord"(1)(0))
     //.withColumn("p2_2",$"coord"(1)(1))
     .withColumn("p3_1",$"coord"(2)(0))
     .withColumn("p3_2",$"coord"(2)(1))
     //.withColumn("p4_1",$"coord"(3)(0))
    // .withColumn("p4_2",$"coord"(3)(1))
    // .withColumn("p5_1",$"coord"(4)(0))
     //.withColumn("p5_2",$"coord"(4)(1))

     .drop("square_id_before")
     .drop("crs")
     .drop("type")
     .drop("coord")
     .drop("coord1")
     .drop("feature")


  val Milano_grid_AND_Telecom = milano_grid.join(telecomWork,$"square_id1" === $"square_id","inner")


  //SGT_3
  val legend = spark.table("legend")
  val pollution = spark.table("pollution_mi")
  val legend_AND_pollution = legend.join(pollution,Seq("sensor_id","sensor_id"))
    .groupBy('sensor_id,'sensor_lat,'sensor_long)
    .agg(
      sum('measurement) alias "final_measurement"
    )



  //SGT_4
  val result = legend_AND_pollution
    .join(Milano_grid_AND_Telecom,
      'sensor_lat<='p1_2  &&
        'sensor_lat>='p3_2  &&
        'sensor_long>='p1_1 &&
        'sensor_long<='p3_1,"inner")
    .groupBy('square_id,'type).agg(
    count('sensor_id) alias "count_of_sensor",
    avg('final_measurement) alias "avg_measurement",
    min('final_measurement) alias "min_measurement", //пороговые значения
    max('final_measurement) alias "max_measurement"  //пороговые значения
  )
  //Вывод зон без датчиков
 // val Square_without_sensor = result.join(Milano_grid_AND_Telecom,
 //   result("square_id")!==Milano_grid_AND_Telecom("square_id1")).select('square_id1)

    //топ 5
  val allOf = result.show()
  val top5dirtRelax = result.orderBy('type,'avg_measurement.desc).select('square_id,'avg_measurement,'type).distinct().limit(5).show()
  val top5dirtWork = result.orderBy('type.desc,'avg_measurement.desc).select('square_id,'avg_measurement,'type).distinct().limit(5).show()
  val top5clearRelax = result.orderBy('type,'avg_measurement).select('square_id,'avg_measurement,'type).distinct().limit(5).show()
  val top5clearWork = result.orderBy('type.desc,'avg_measurement).select('square_id,'avg_measurement,'type).distinct().limit(5).show()

}


