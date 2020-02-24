package com.ws;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * @Description:
 * @Date: 2020/2/24 0024 16:56
 * @author JunWu
 */
public class ClientTest {

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 3000, new Watcher() {
            public void process(WatchedEvent event) {

            }
        });

        Stat stat = new Stat();

        byte[] data =  zooKeeper.getData("/ws",false, stat);

        System.out.println(new String(data));
    }
}
