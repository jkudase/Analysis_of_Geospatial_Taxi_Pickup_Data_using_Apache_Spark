package cse512

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object Entrance extends App {
  Logger.getLogger("org.spark_project").setLevel(Level.WARN)
  Logger.getLogger("org.apache").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)
  Logger.getLogger("com").setLevel(Level.WARN)

  override def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .appName("CSE512-HotspotAnalysis-Group7") // YOU NEED TO CHANGE YOUR GROUP NAME
      .config("spark.some.config.option", "some-value")//.master("local[*]")
      .getOrCreate()

    paramsParser(spark, args)

  }
  
  private def paramsParser(spark: SparkSession, args: Array[String]): Unit = {
    var paramOffset = 1
    var currentQueryParams = ""
    var currentQueryName = ""
    var currentQueryIdx = -1

    while (paramOffset <= args.length) {
      if (paramOffset == args.length || args(paramOffset).toLowerCase.contains("analysis")  || args(paramOffset).toLowerCase.contains("query")) {
        // Turn in the previous query
        if (currentQueryIdx != -1) queryLoader(spark, currentQueryName, currentQueryParams, args(0) + currentQueryIdx)

        // Start a new query call
        if (paramOffset == args.length) return

        currentQueryName = args(paramOffset)
        currentQueryParams = ""
        currentQueryIdx = currentQueryIdx + 1
      }
      else {
        // Keep appending query parameters
        currentQueryParams = currentQueryParams + args(paramOffset) + " "
      }
      paramOffset = paramOffset + 1
    }
  }

  private def queryLoader(spark: SparkSession, queryName: String, queryParams: String, outputPath: String) {
    val queryParam = queryParams.split(" ")
    var metricsLocation = ""
    var aggregatedMetricsLocation = ""
    val stageMetrics = ch.cern.sparkmeasure.StageMetrics(spark)

    import spark.implicits._

    if (queryName.equalsIgnoreCase("hotcellanalysis")) {
      if (queryParam.length != 1) throw new ArrayIndexOutOfBoundsException("[CSE512] Query " + queryName + " needs 1 parameters but you entered " + queryParam.length)
      HotcellAnalysis.runHotcellAnalysis(spark, queryParam(0)).limit(50).write.mode(SaveMode.Overwrite).csv(outputPath)
      metricsLocation = "test/performanceHotCell"
      aggregatedMetricsLocation = "test/perfHotCellAggregated"
      stageMetrics.runAndMeasure(HotcellAnalysis.runHotcellAnalysis(spark, queryParam(0)).limit(50).write.mode(SaveMode.Overwrite).csv(outputPath))
      val df = stageMetrics.createStageMetricsDF("PerfStageMetrics")
      val aggregatedDF = stageMetrics.aggregateStageMetrics("PerfStageMetrics")
      aggregatedDF.show()
      aggregatedDF.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(aggregatedMetricsLocation)
      val dump = spark.sql("select * from PerfStageMetrics").toDF()
      dump.show()
      dump.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)
    }
    else if (queryName.equalsIgnoreCase("hotzoneanalysis")) {
      if (queryParam.length != 2) throw new ArrayIndexOutOfBoundsException("[CSE512] Query " + queryName + " needs 2 parameters but you entered " + queryParam.length)
      HotzoneAnalysis.runHotZoneAnalysis(spark, queryParam(0), queryParam(1)).write.mode(SaveMode.Overwrite).csv(outputPath)
      metricsLocation = "test/performanceHotZone"
      aggregatedMetricsLocation = "test/performanceHotZoneAggregated"
      stageMetrics.runAndMeasure(HotzoneAnalysis.runHotZoneAnalysis(spark, queryParam(0), queryParam(1)).write.mode(SaveMode.Overwrite).csv(outputPath))
      val dump = spark.sql("select * from PerfStageMetrics").toDF()
      dump.show()
      dump.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)
      val df = stageMetrics.createStageMetricsDF("PerfStageMetrics")
      val aggregatedDF = stageMetrics.aggregateStageMetrics("PerfStageMetrics")
      aggregatedDF.show()
      aggregatedDF.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(aggregatedMetricsLocation)
    }
    else if (queryName.equalsIgnoreCase("RangeQuery"))
    {
      if(queryParam.length!=2) throw new ArrayIndexOutOfBoundsException("[CSE512] Query "+queryName+" needs 2 parameters but you entered "+queryParam.length)
      stageMetrics.runAndMeasure(Seq(queryName, SpatialQuery.runRangeQuery(spark, queryParam(0), queryParam(1)).toString).toDF().write.mode(SaveMode.Overwrite).csv(outputPath))
      metricsLocation = "test/performanceRangeQuery"
      val dump = spark.sql("select * from PerfStageMetrics").toDF()
      dump.show()
      dump.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)
      val df = stageMetrics.createStageMetricsDF("PerfStageMetrics")
      val aggregatedDF = stageMetrics.aggregateStageMetrics("PerfStageMetrics")
      aggregatedDF.show()
      aggregatedDF.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)
    }
    else if (queryName.equalsIgnoreCase("RangeJoinQuery"))
    {
      if(queryParam.length!=2) throw new ArrayIndexOutOfBoundsException("[CSE512] Query "+queryName+" needs 2 parameters but you entered "+queryParam.length)
      stageMetrics.runAndMeasure(Seq(queryName, SpatialQuery.runRangeJoinQuery(spark, queryParam(0), queryParam(1)).toString).toDF().write.mode(SaveMode.Overwrite).csv(outputPath))
      metricsLocation = "test/performanceRangeJoinQuery"
      val dump = spark.sql("select * from PerfStageMetrics").toDF()
      dump.show()
      dump.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)

      val df = stageMetrics.createStageMetricsDF("PerfStageMetrics")
      val aggregatedDF = stageMetrics.aggregateStageMetrics("PerfStageMetrics")
      aggregatedDF.show()
      aggregatedDF.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)
    }
    else if (queryName.equalsIgnoreCase("DistanceQuery"))
    {
      if(queryParam.length!=3) throw new ArrayIndexOutOfBoundsException("[CSE512] Query "+queryName+" needs 3 parameters but you entered "+queryParam.length)
      stageMetrics.runAndMeasure(Seq(queryName, SpatialQuery.runDistanceQuery(spark, queryParam(0), queryParam(1), queryParam(2)).toString).toDF().write.mode(SaveMode.Overwrite).csv(outputPath))
      metricsLocation = "test/performanceDistanceQuery"
      val dump = spark.sql("select * from PerfStageMetrics").toDF()
      dump.show()
      dump.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)

      val df = stageMetrics.createStageMetricsDF("PerfStageMetrics")
      val aggregatedDF = stageMetrics.aggregateStageMetrics("PerfStageMetrics")
      aggregatedDF.show()
      aggregatedDF.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)
    }
    else if (queryName.equalsIgnoreCase("DistanceJoinQuery"))
    {
      if(queryParam.length!=3) throw new ArrayIndexOutOfBoundsException("[CSE512] Query "+queryName+" needs 3 parameters but you entered "+queryParam.length)
      stageMetrics.runAndMeasure(Seq(queryName, SpatialQuery.runDistanceJoinQuery(spark, queryParam(0), queryParam(1), queryParam(2)).toString).toDF().write.mode(SaveMode.Overwrite).csv(outputPath))
      metricsLocation = "test/performanceDistanceJoinQuery"
      val dump = spark.sql("select * from PerfStageMetrics").toDF()
      dump.show()
      dump.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)

      val df = stageMetrics.createStageMetricsDF("PerfStageMetrics")
      val aggregatedDF = stageMetrics.aggregateStageMetrics("PerfStageMetrics")
      aggregatedDF.show()
      aggregatedDF.coalesce(1).write.mode(SaveMode.Overwrite).option("header", "true").csv(metricsLocation)
    }
    else {
      throw new NoSuchElementException("[CSE512] The given query name " + queryName + " is wrong. Please check your input.")
    }
  }
}
