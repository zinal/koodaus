# KOODAUS data masking toolkit

The set of algorithms and utilities for data masking and data generation.

Spark Scala session example for data generation.

```bash
spark-shell --jars koodaus-algo-1.0-SNAPSHOT.jar

import net.koodaus.udf.FioUdf;
val fu = new FioUdf("/s3data/textdict", "passw0rd")
val sqlfu = udf((male: Boolean, position: Long) => { fu.call(male, position) })

val SYNTHETICS_OUTPUT_PATH = "s3a://dproc-wh/s3measure/GEN/tab1/"
val numPart = 250
val rowsPerPart = 1000000 // 250M total rows

val df1 = 1.to(numPart).toDF("id_part").repartition(numPart)
val df2 = df1.as[Int].mapPartitions(c=>1.to(rowsPerPart).toIterator)
val df3 = df2.
  withColumn("partno", spark_partition_id()).
  withColumn("xval", col("value")).
  withColumn("str_1", sqlfu(lit(true), lit(rowsPerPart) * col("partno") + col("value"))).
  withColumn("str_2", sqlfu(lit(false), lit(rowsPerPart) * col("partno") + col("value")))

df3.write.
  option("compression", "gzip").
  mode("overwrite").
  //csv(SYNTHETICS_OUTPUT_PATH)
  parquet(SYNTHETICS_OUTPUT_PATH)

val rawDF = spark.read.parquet(SYNTHETICS_OUTPUT_PATH)
rawDF.createOrReplaceTempView("temp_data1")
spark.sql("SELECT COUNT(*), COUNT(DISTINCT str_1), COUNT(DISTINCT str_2) FROM temp_data1").show()

```

Sample generation statistics, case 1:

```text
total rows:         5000000000
male distinct:       398110532
female distinct:     257924017
theoretical male:    404109480
theoretical female:  258687660
```

Sample generation statistics, case 2:

```text
total rows:          250000000
male distinct:       175618473
female distinct:     151946984
theoretical male:    404109480
theoretical female:  258687660
```

# Credits

The code in this repository was inspired by (and partially based on) the [IBM DsMask](https://github.com/IBM/dsmask).
