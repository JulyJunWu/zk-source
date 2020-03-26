package com.ws;

import org.apache.zookeeper.server.DataNode;
import org.apache.zookeeper.server.DataTree;
import org.apache.zookeeper.server.Request;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
import org.apache.zookeeper.txn.TxnHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description:
 * @Author: JunWu
 * @Date: 2020/3/25 0025 16:56
 */
public class OtherTest {


    public static void main(String[] args) throws Exception {
        // testFileChannel();
        testZkDatabase();
    }

    /**
     * 测试事务日志刷出 到磁盘
     *
     * @throws Exception
     */
    private static void testZkDatabase() throws Exception {

        DataTree dataTree = new DataTree();
        dataTree.addDataNode("/ws",new DataNode(null,"HelloWorld".getBytes(),1L,null));
        FileTxnSnapLog txnSnapLog = new FileTxnSnapLog(new File("txdata"), new File("snapdata"));
        TxnHeader txnHeader = new TxnHeader(666, 888, 999L, 555L, 7);
        Request request = new Request(1L, 1, 1, txnHeader, null, 1L);
        txnSnapLog.append(request);

        txnHeader.setZxid(1234L);
        txnSnapLog.rollLog();
        txnSnapLog.append(request);
        txnSnapLog.commit();
    }

    public static void testFileChannel() throws Exception {
        FileOutputStream fos = new FileOutputStream("ws");
        FileChannel channel = fos.getChannel();
        ByteBuffer wrap = ByteBuffer.wrap("HelloWorld".getBytes());
        channel.write(wrap,102400L);
        fos.flush();
        fos.close();
    }
}
