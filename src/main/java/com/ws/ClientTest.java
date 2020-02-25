package com.ws;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.locks.LockSupport;

/**
 * @author JunWu
 * @Description:
 * @Date: 2020/2/24 0024 16:56
 */
public class ClientTest {

    public static ZooKeeper zooKeeper;

    static {
        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 20000, new Watcher() {
                public void process(WatchedEvent event) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void create() throws Exception {
        //String s = zooKeeper.create("/ws/qjw", "HelloWorld".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //TimeUnit.SECONDS.sleep(5);
        String s2 = zooKeeper.create("/ws/qjw/NN", "HelloWorld".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(s2);
    }

    public static void getData() throws Exception {
        Stat stat = new Stat();
        byte [] data = zooKeeper.getData("/ws", false,stat);
        System.out.println(new String(data));
    }


    public static void deleteNode()throws Exception{
        zooKeeper.delete("/ws",-1);
    }
    public static void main(String[] args)throws Exception {
        create();
        LockSupport.park();
    }
}
