

```bash
spark-shell --jars koodaus-algo-1.0-SNAPSHOT.jar

import net.koodaus.udf.FioUdf;
val fu = new FioUdf("/s3data/textdict", "passw0rd")
val sqlfu = udf((male: Boolean, position: Long) => { fu.get(male, position) })

val SYNTHETICS_OUTPUT_PATH = "s3a://dproc-wh/s3measure/GEN/tab1/"
val numPart = 200
val rowsPerPart = 1000000

val df1 = 1.to(numPart).toDF("id_part").repartition(numPart)
val df2 = df1.as[Int].mapPartitions(c=>1.to(rowsPerPart).toIterator)
val df3 = df2.
  withColumn("xval", col("value")).
  withColumn("str_1", sqlfu(lit(true), col("value"))).
  withColumn("str_2", sqlfu(lit(false), col("value")))

df3.write.
  option("compression", "gzip").
  mode("overwrite").
  //csv(SYNTHETICS_OUTPUT_PATH)
  parquet(SYNTHETICS_OUTPUT_PATH)

```
