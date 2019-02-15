package com.study.sparkstudy.project

import com.study.sparkstudy.dao.{CategaryClickCountDao, CategarySearchClickCountDao}
import com.study.sparkstudy.domain.{CategaryClickCount, ClickLog, CategarySearchClickCount}
import kafka.serializer.{Decoder, StringDecoder}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import com.study.sparkstudy.project.util.DataUtils

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag


object StatStreamingApp {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("spark_streaming").setMaster("local[*]")

    val sc = new SparkContext(conf)
    //sc.setCheckpointDir("checkpoints")
    sc.setLogLevel("ERROR")
    //多少秒消费一次
    val ssc = new StreamingContext(sc, Seconds(5))

    val topics = Map("flumeTopic" -> 2)

    val kafkaParams = Map[String, String](
      "bootstrap.servers" -> "node1:9092,node2:9092,node3:9092",
      "group.id" -> "Test",
      "auto.offset.reset" -> "smallest" //largest smallest
    )

    // 直连方式拉取数据，这种方式不会修改数据的偏移量，需要手动的更新
    val logs = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, Set("flumeTopic")).map(_._2)
    //val lines = KafkaUtils.createStream(ssc, "10.216.5.152:2183,10.216.5.153:2183,10.216.5.154:2183", "WYWX", topics).map(_._2)
    //156.187.29.132	2017-11-20 00:39:26	"GET /www/2 HTTP/1.0"	-	200
    //30.132.100.124	2019-02-13 16:59:52	"GET www/1 HTTP/1.0"	-	200
    //logs.print()
    val cleanData = logs.map(line => {
      val infos = line.split("\t")
      val url = infos(2).split(" ")(1)
      var categaryId = 0
      //把爱奇艺的类目编号拿到了
      if (url.startsWith("www")) {
        categaryId = url.split("/")(1).toInt
      }
      ClickLog(infos(0), DataUtils.parseToMin(infos(1)), categaryId, infos(3), infos(4).toInt)
    }).filter(clickLog => clickLog.categaryId != 0)
    cleanData.print()
    //每个类别的每天的点击量
    cleanData.map(log => {
      (log.time.substring(0, 8) + log.categaryId, 1)
    }).reduceByKey(_ + _).foreachRDD(rdd => {
      rdd.foreachPartition(partitions => {
        var list = new ListBuffer[CategaryClickCount]
        partitions.foreach(pair => {
          list.append(CategaryClickCount(pair._1, pair._2))
        })
        CategaryClickCountDao.save(list)
      })
    })
    //每个栏目下面从渠道过来的流量20171122_www.baidu.com_1 100 20171122_2（渠道）_1（类别） 100
    //categary_search_count   create "categary_search_count","info"
    //124.30.187.10	2017-11-20 00:39:26	"GET www/6 HTTP/1.0"
    // 	https:/www.sogou.com/web?qu=我的体育老师	302
    cleanData.map(log => {
      val url = log.refer.replace("//", "/")
      val splits = url.split("/")
      var host = ""
      if (splits.length > 2) {
        host = splits(1)
      }
      (host, log.time, log.categaryId)
    }).filter(x => x._1 != "").map(x => {
      (x._2.substring(0, 8) + "_" + x._1 + "_" + x._3, 1)
    }).reduceByKey(_ + _).foreachRDD(rdd => {
      rdd.foreachPartition(partions => {
        val list = new ListBuffer[CategarySearchClickCount]
        partions.foreach(pairs => {
          list.append(CategarySearchClickCount(pairs._1, pairs._2))
        })
        CategarySearchClickCountDao.save(list)
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
