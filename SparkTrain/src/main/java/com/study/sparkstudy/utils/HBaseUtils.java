package com.study.sparkstudy.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Hbase工具类
 */
public class HBaseUtils {
    Configuration configration = null;
    public Connection connection = null;
    private static HBaseUtils instance = null;

    private HBaseUtils() {
        configration = HBaseConfiguration.create();
        configration.set("hbase.zookeeper.quorum", "node2,node3,node4");
        configration.set("hbase.zookeeper.property.clientPort", "2181");
        try {
            connection = ConnectionFactory.createConnection(configration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized HBaseUtils getInstance() {
        if (null == instance) {
            instance = new HBaseUtils();
        }
        return instance;
    }

    public Table getTable(String tableName) {
        Table table = null;
        TableName tn = TableName.valueOf(tableName);
        try {
            table = connection.getTable(tn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }


    public void put(String tableName, String rowkey, String cf, String column, String value) throws IOException {
        //new 一个列  ，hgs_000为row key
        Put put = new Put(Bytes.toBytes(rowkey));
        //下面三个分别为，列族，列名，列值
        put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(column), Bytes.toBytes(value));
        TableName tn = TableName.valueOf(tableName);
        //得到 table
        Table table = connection.getTable(tn);
        //执行插入
        table.put(put);
    }

    public static void main(String[] args) throws IOException {
        String tableName = "category_clickcount";
        String rowkey = "20271111_88";
        String cf = "info";
        String column = "click_count";
        String value = "1";
        //Table table = HBaseUtils.getInstance().getTable("category_clickcount");
        //System.out.println(table.getName().getNameAsString());
        HBaseUtils.getInstance().put(tableName, rowkey, cf, column, value);
    }
}
