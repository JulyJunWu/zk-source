package com.ws;

import org.apache.zookeeper.server.Request;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
import org.apache.zookeeper.txn.TxnHeader;

import java.io.File;

/**
 * @Description:
 * @Author: JunWu
 * @Date: 2020/3/25 0025 16:56
 */
public class OtherTest {


    public static void main(String[] args) throws Exception {
        testZkDatabase();
    }

    /**
     * 测试事务日志刷出 到磁盘
     *
     * @throws Exception
     */
    private static void testZkDatabase() throws Exception {
        FileTxnSnapLog txnSnapLog = new FileTxnSnapLog(new File("txdata"), new File("snapdata"));
        TxnHeader txnHeader = new TxnHeader(666, 888, 999L, 555L, 7);
        Request request = new Request(1L, 1, 1, txnHeader, null, 1L);
        txnSnapLog.append(request);
    }
}
