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
        txnSnapLog.commit();
    }

    public static void testFileChannel() throws Exception {

        FileOutputStream bigbaby = new FileOutputStream("bigbaby");

        bigbaby.write(8);
        bigbaby.flush();

        FileChannel channel = bigbaby.getChannel();

        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.putInt(6);
        channel.write(allocate);
        allocate.flip();
        channel.close();
        bigbaby.flush();

        LockSupport.park();

    }
}
