zk版本基于 : 3.4.13

单体启动:
    QuorumPeerMain
    ZookeeperServerMain
    ZookeeperServer      :  全局的
    NioServerCnxnFactory : 监听客户端请求的线程
    ZKDatabase         : 操作内存中的数据,包括启动加载磁盘数据到内存    
        DataTree       : 保存节点数据,内部其实就是一个Map
            DataNode   :  节点数据
    FileTxnSnapLog:
         TxnLog        :  日志
         SnapShot      :  快照
    SessionTrackerImpl : 跟踪session,管理session(创建/删除/定时检测过期session等等) , 同时自身也是一个线程
    SessionImpl        : session的实现类
    NioServerCnxn : 类似于tomcat中NioChannel,Netty中的NioSocketChannel,都是用来包装SocketChannel,同时也是一个watch
    RequestProcessor   : 处理请求的链路
    PrepRequestProcessor(同时又是一个线程) -> SyncRequestProcessor(同时也是一个线程) -> FinalRequestProcessor
    
    
集群下:
    QuorumPeer
    FastLeaderElection : 负责选举
       Messenger : 
           WorkerSender
           WorkerReceiver
    QuorumCnxManager:
        Listener:
        SendWorker: 每一个sid对应一个发送数据线程,本节点除外 
        RecvWorker: 每一个sid对应一个接收数据线程,本节点除外
    QuorumMaj: 投票数是否过半
    
  集群启选举流程:
      启动Listener监听选举端口
      启动Messenger内部2个线程,分别为WorkerSender,WorkerReceive 
      QuorumPeer线程启动,触发FastLeaderElection.lookForLeader
      发送投票信息: sendNotifications(首次都是推荐自身为leader) , 投票信息存入sendqueue队列
      WorkerSender拉取sendqueue队列数据,将对象数据转为字节数据,交给QuorumCnxManager处理
      QuorumCnxManager将字节数据保存到对应的ArrayLinkedQueue队列中
        启动对应的sid的选举端口的socket client
        通过socket写入本节点的myid
        启动处理该目标sid的socket的读写线程,分别为 RecvWorker , SendWorker线程
        sendWorker线程从QuorumCnxManager.queueSendMap拉取属于自己的sid 需要发送的数据进行发送
        RecvWorker线程则是读取数据,将数据转为Message对象,保存到QuorumCnxManager.recvQueue队列;
      WorkerReceive线程不断从QuorumCnxManager.recvQueue队列拉取数据处理,满足条件的数据则发送到FastLeaderElection.recvQueue队列
      lookForLeader函数while中处理FastLeaderElection.recvQueue队列数据
        选举处理投票三种情况:
            1.外来节点比本节点轮次高,则将外来的节点轮次设置为本节点的轮次,清空该轮的投票,再比较双方的投票
                1.1 外来节点合理: 将自身投票信息更新为外来节点的投票数据,重新广播本节点投票信息到其他节点
                1.2 本节点合理: 重新更细下自身节点选举的leader;
            2.外来节点轮次比本节点低,则不做任何处理,忽略
            3.相同的轮次,开始比较,比较的优先级 轮次 > zxid(事物id) > id(sid)
              
      
      
      