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
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 40000, new Watcher() {
                public void process(WatchedEvent event) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void create() throws Exception {
        String s = zooKeeper.create("/ws/hh", "HelloWorld".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(s);
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
        deleteNode();
        LockSupport.park();
    }
}
