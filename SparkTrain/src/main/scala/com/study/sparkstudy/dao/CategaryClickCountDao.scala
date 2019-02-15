package com.study.sparkstudy.dao

import scala.collection.mutable.ListBuffer
import com.study.sparkstudy.domain.CategaryClickCount
import com.study.sparkstudy.utils.HBaseUtils
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client.Get

object CategaryClickCountDao {
  val tableName = "category_clickcount"
  val cf = "info"
  val qualifer = "click_count"

  /**
    * 保存数据
    *
    * @param list
    */
  def save(list: ListBuffer[CategaryClickCount]): Unit = {
    val table = HBaseUtils.getInstance().getTable(tableName)
    for (els <- list) {
      import org.apache.hadoop.hbase.util.Bytes
      table.incrementColumnValue(Bytes.toBytes(els.categaryID), Bytes.toBytes(cf), Bytes.toBytes(qualifer), els.clickCout)
    }
  }

  def count(day_categaryId: String): Long = {
    val table = HBaseUtils.getInstance().getTable(tableName)
    val get = new Get(Bytes.toBytes(day_categaryId))
    val values = table.get(get).getValue(Bytes.toBytes(cf), Bytes.toBytes(qualifer))
    if (values == null) {
      0L
    } else {
      Bytes.toLong(values)
    }
  }

  def main(args: Array[String]): Unit = {
    val list = new ListBuffer[CategaryClickCount]
    list.append(CategaryClickCount("20181122_8", 300))
    list.append(CategaryClickCount("20181122_9", 300))
    list.append(CategaryClickCount("20181122_10", 1600))
    save(list)

    print(count("20181122_8") + "/t" + count("20181122_9") + "/t" + count("20181122_10"))
  }

}