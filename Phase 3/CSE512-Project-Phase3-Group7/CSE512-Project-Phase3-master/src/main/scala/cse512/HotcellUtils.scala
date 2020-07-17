package cse512

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar

object HotcellUtils {
  val coordinateStep = 0.01

  def CalculateCoordinate(inputString: String, coordinateOffset: Int): Int =
  {
    // Configuration variable:
    // Coordinate step is the size of each cell on x and y
    var result = 0
    coordinateOffset match
    {
      case 0 => result = Math.floor((inputString.split(",")(0).replace("(","").toDouble/coordinateStep)).toInt
      case 1 => result = Math.floor(inputString.split(",")(1).replace(")","").toDouble/coordinateStep).toInt
      // We only consider the data from 2009 to 2012 inclusively, 4 years in total. Week 0 Day 0 is 2009-01-01
      case 2 => {
        val timestamp = HotcellUtils.timestampParser(inputString)
        result = HotcellUtils.dayOfMonth(timestamp) // Assume every month has 31 days
      }
    }
    return result
  }

  def timestampParser (timestampString: String): Timestamp =
  {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    val parsedDate = dateFormat.parse(timestampString)
    val timeStamp = new Timestamp(parsedDate.getTime)
    return timeStamp
  }

  def dayOfYear (timestamp: Timestamp): Int =
  {
    val calendar = Calendar.getInstance
    calendar.setTimeInMillis(timestamp.getTime)
    return calendar.get(Calendar.DAY_OF_YEAR)
  }

  def dayOfMonth (timestamp: Timestamp): Int =
  {
    val calendar = Calendar.getInstance
    calendar.setTimeInMillis(timestamp.getTime)
    return calendar.get(Calendar.DAY_OF_MONTH)
  }
  // YOU NEED TO CHANGE THIS PART
  def squaredValues(point:Int):Double=
  {
    return (point*point).toDouble;
  }

  def countNeighbouringCells(minimumX: Int, minimumY: Int, minimumZ: Int, maximumX: Int, maximumY: Int, maximumZ: Int, inputX: Int, inputY: Int, inputZ: Int): Int =
  {
    var cellNumber = 0;
    if (inputX == minimumX || inputX == maximumX) {
      cellNumber += 1;
    }
    if (inputY == minimumY || inputY == maximumY) {
      cellNumber += 1;
    }
    if (inputZ == minimumZ || inputZ == maximumZ) {
      cellNumber += 1;
    }
    if (cellNumber == 1) {return 18;}
    else if (cellNumber == 2){return 12;}
    else if (cellNumber == 3){return 8;}
    else{return 27;}
  }
  def gScoreStats(x: Int, y: Int, z: Int, mean:Double, sd: Double, countNumber: Int, sumNumber: Int, numOfCells: Int): Double =
  {
    val statNum = (sumNumber.toDouble - (mean*countNumber.toDouble))
    val statDen = sd*math.sqrt((((numOfCells.toDouble*countNumber.toDouble) -(countNumber.toDouble*countNumber.toDouble))/(numOfCells.toDouble-1.0).toDouble).toDouble).toDouble
    return (statNum/statDen ).toDouble
  }

}
