package cse512

import org.apache.spark.sql.SparkSession

object SpatialQuery extends App{
  def runRangeQuery(spark: SparkSession, arg1: String, arg2: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    
    spark.udf.register("ST_Contains",(queryRectangle:String, pointString:String)=>{
      val point = pointString.split(",")
      val px = point(0).trim().toDouble
      val py = point(1).trim().toDouble
      val rect = queryRectangle.split(",")
      val rx1 = rect(0).trim().toDouble
      val ry1 = rect(1).trim().toDouble
      val rx2 = rect(2).trim().toDouble
      val ry2 = rect(3).trim().toDouble

      ((rx2 >= px && px >= rx1 ) || (rx2 <= px && px <= rx1)) && ((ry2 >= py && py >= ry1) || (ry2 <= py && py <=ry1))
    })

    val resultDf = spark.sql("select * from point where ST_Contains('"+arg2+"',point._c0)")
    resultDf.show()
    return resultDf.count()
  }

  def runRangeJoinQuery(spark: SparkSession, arg1: String, arg2: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point")

    val rectangleDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg2);
    rectangleDf.createOrReplaceTempView("rectangle")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    
    spark.udf.register("ST_Contains",(queryRectangle:String, pointString:String)=>{
      val point = pointString.split(",")
      val px = point(0).trim().toDouble
      val py = point(1).trim().toDouble
      val rect = queryRectangle.split(",")
      val rx1 = rect(0).trim().toDouble
      val ry1 = rect(1).trim().toDouble
      val rx2 = rect(2).trim().toDouble
      val ry2 = rect(3).trim().toDouble

      ((rx2 >= px && px >= rx1 ) || (rx2 <= px && px <= rx1)) && ((ry2 >= py && py >= ry1) || (ry2 <= py && py <=ry1))
    })

    val resultDf = spark.sql("select * from rectangle,point where ST_Contains(rectangle._c0,point._c0)")
    resultDf.show()

    return resultDf.count()
  }

  def runDistanceQuery(spark: SparkSession, arg1: String, arg2: String, arg3: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    
    spark.udf.register("ST_Within",(pointString1:String, pointString2:String, distance:Double)=>{
      val p1 = pointString1.split(",")
      val px1 = p1(0).trim().toDouble
      val py1 = p1(1).trim().toDouble
      val p2 = pointString2.split(",")
      val px2 = p2(0).trim().toDouble
      val py2 = p2(1).trim().toDouble
      val dist = scala.math.pow(scala.math.pow((px1 - px2), 2) + scala.math.pow((py1 - py2), 2), 0.5)
      dist <= distance})

    val resultDf = spark.sql("select * from point where ST_Within(point._c0,'"+arg2+"',"+arg3+")")
    resultDf.show()

    return resultDf.count()
  }

  def runDistanceJoinQuery(spark: SparkSession, arg1: String, arg2: String, arg3: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point1")

    val pointDf2 = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg2);
    pointDf2.createOrReplaceTempView("point2")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    
    spark.udf.register("ST_Within",(pointString1:String, pointString2:String, distance:Double)=>{
      val p1 = pointString1.split(",")
      val px1 = p1(0).trim().toDouble
      val py1 = p1(1).trim().toDouble
      val p2 = pointString2.split(",")
      val px2 = p2(0).trim().toDouble
      val py2 = p2(1).trim().toDouble
      val dist = scala.math.pow(scala.math.pow((px1 - px2), 2) + scala.math.pow((py1 - py2), 2), 0.5)
      dist <= distance})

    val resultDf = spark.sql("select * from point1 p1, point2 p2 where ST_Within(p1._c0, p2._c0, "+arg3+")")
    resultDf.show()
    return resultDf.count()
  }
}