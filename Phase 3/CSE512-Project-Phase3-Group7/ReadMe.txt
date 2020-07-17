CSE 512 Distributed Database Systems
Project Phase 3

Group Members :- Jayesh Kudase, Sushmita Muthe, Prashant Ravindra Jadhav

1. In Entrance.Scala from Project Phase 2, we have modified the paramsParser function in order to handle the query type commands (e.g. RangeQuery, RangeJoinQuery).
2. SpatialQuery.scala file from Project Phase 1 is added in Project Phase 2 codebase.
3. Code to perform RangeQuery, RangeJoinQuery, DistanceQuery and DistanceJoinQuery of Project Phase 1 is implemented in Entrance.scala file.
4. Also, code to generate spark metrics and write it in output files in test folder is also added for every function that we perform.
5. Prepare jar using sbt assembly command and execute below command on different sizes of datasets.

Single command to execute all tasks together :-

/usr/local/spark-2.3.4-bin-hadoop2.7/bin/spark-submit 
/home/ubuntu/CSE512-Project-Phase3-master/target/scala-2.11/CSE512-Hotspot-Analysis-Template-assembly-0.1.0.jar
test/output
rangequery
src/resources/arealm10000.csv
-93.63173,33.0183,-93.359203,33.219456
rangejoinquery
src/resources/arealm10000.csv
src/resources/zcta10000.csv
distancequery
src/resources/arealm10000.csv
-88.331492,32.324142
1
distancejoinquery
src/resources/arealm10000.csv
src/resources/arealm10000.csv
0.1
hotzoneanalysis
src/resources/point-hotzone.csv
src/resources/zone-hotzone.csv
hotcellanalysis
src/resources/yellow_trip_sample_100000.csv