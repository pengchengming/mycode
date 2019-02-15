package com.study.sparkstudy.dao

import com.study.sparkstudy.utils.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes
import com.study.sparkstudy.domain.CategarySearchClickCount
import scala.collection.mutable.ListBuffer

object CategarySearchClickCountDao {
  val tableName = "categary_search_count"
  val cf = "info"
  val qualifer = "click_count"

  /**
    * 保存数据
    *
    * @param list
    */
  def save(list: ListBuffer[CategarySearchClickCount]): Unit = {
    val table = HBaseUtils.getInstance().getTable(tableName)
    for (els <- list) {
      import org.apache.hadoop.hbase.util.Bytes
      import org.apache.hadoop.hbase.util.Bytes
      table.incrementColumnValue(Bytes.toBytes(els.day_search_categary), Bytes.toBytes(cf), Bytes.toBytes(qualifer), els.clickCout)
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
    val list = new ListBuffer[CategarySearchClickCount]
    list.append(CategarySearchClickCount("20171122_1_1", 300))
    list.append(CategarySearchClickCount("20171122_2_1", 300))
    list.append(CategarySearchClickCount("20171122_1_2", 1600))
    save(list)

    print(count("20171122_1_1") + "---" + count("20171122_2_1") + "---" + count("20171122_1_2") + "---")
  }

}