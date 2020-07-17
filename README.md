# Analysis_of_Geospatial_Taxi_Pickup_Data_using_Apache_Spark

## **Aim:** To find a list of the fifty most significant hot spot cells in time and space as identified using the Getis-Ord statistic.

**Phase 1:** <br>
 Setup a bidirectional paswordless SSH connection between one master and two slave nodes (Amazon EC2 Ubuntu instances) and configure Apache Hadoop and Spark on them. (https://www.youtube.com/watch?v=bdNLKN21U7w)<br>
 
Write two User Defined Functions ST_Contains and ST_Within in SparkSQL and use them to do four spatial queries:<br>

**Range query:** Use ST_Contains. Given a query rectangle R and a set of points P, find all the points within R.<br>
**Range join query:** Use ST_Contains. Given a set of Rectangles R and a set of Points S, find all (Point, Rectangle) pairs such that the point is within the rectangle.<br>
**Distance query:** Use ST_Within. Given a point location P and distance D in km, find all points that lie within a distance D from P<br>
**Distance join query:** Use ST_Within. Given a set of Points S1 and a set of Points S2 and a distance D in km, find all (s1, s2) pairs such that s1 is within a distance D from s2 (i.e., s1 belongs to S1 and s2 belongs to S2).<br>

**Phase 2:** <br>
In this phase, we were required to do spatial hot spot analysis. In particular, we needed to complete two different hot spot analysis tasks<br>

**Hot zone analysis**<br>
This task will needs to perform a range join operation on a rectangle datasets and a point dataset. For each rectangle, the number of points located within the rectangle will be obtained. The hotter rectangle means that it include more points. So this task is to calculate the hotness of all the rectangles.<br>

**Hot cell analysis**<br>
Description
This task will focus on applying spatial statistics to spatio-temporal big data in order to identify statistically significant spatial hot spots using Apache Spark. The topic of this task is from ACM SIGSPATIAL GISCUP 2016.<br>

The Problem Definition page is here: http://sigspatial2016.sigspatial.org/giscup2016/problem<br>

The Submit Format page is here: http://sigspatial2016.sigspatial.org/giscup2016/submit<br>
 
**Phase 3:** <br>
Experimental setup and a report to provide an overview of evaluation metrics derived on varying dataset on a single and multi-node setup using Apache Hadoop, Spark and AWS.
