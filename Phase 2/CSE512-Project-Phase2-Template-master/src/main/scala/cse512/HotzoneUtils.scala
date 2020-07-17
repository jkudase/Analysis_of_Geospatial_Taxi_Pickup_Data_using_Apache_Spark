
package cse512

object HotzoneUtils {

  def ST_Contains(queryRectangle: String, pointString: String ): Boolean = {
    // YOU NEED TO CHANGE THIS PART
    val point = pointString.split(",")
    val px = point(0).trim().toDouble
    val py = point(1).trim().toDouble
    val rect = queryRectangle.split(",")
    val rx1 = rect(0).trim().toDouble
    val ry1 = rect(1).trim().toDouble
    val rx2 = rect(2).trim().toDouble
    val ry2 = rect(3).trim().toDouble
    if(((rx2 >= px && px >= rx1 ) || (rx2 <= px && px <= rx1)) && ((ry2 >= py && py >= ry1) || (ry2 <= py && py <=ry1)))
      return true
    else
      return false
  }
  // YOU NEED TO CHANGE THIS PART

}