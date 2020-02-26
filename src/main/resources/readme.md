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