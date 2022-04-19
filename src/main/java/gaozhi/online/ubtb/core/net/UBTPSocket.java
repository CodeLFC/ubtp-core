package gaozhi.online.ubtb.core.net;


import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO   PDU的发送和接收服务  发送数据与接收数据的统计工作
 * @date 2021/12/26 22:54
 */
public class UBTPSocket implements UDPSocket.PDUConsumer {
    /**
     * @description:(消息数据单元消费者)
     * @author: gaozhi.online
     * @date: 2021/9/5 23:13
     */
    @FunctionalInterface
    public interface UMsgConsumer {
        void accept(UMsg uMsg, SocketAddress socketAddress);
    }
    /**
     * @description: TODO   提供网络服务
     * @author LiFucheng
     * @date 2021/12/27 22:54
     * @version 1.0
     */
    private final UDPSocket udpSocket;
    /**
     * @description: TODO   网络服务的唯一id
     * @author LiFucheng
     * @date 2021/12/27 10:11
     * @version 1.0
     */
    private int id;
    /**
     * @description: TODO   发送数据量
     * @author LiFucheng
     * @date 2021/12/27 22:18
     * @version 1.0
     */
    private long sendLen;
    /**
     * @description: TODO  接收数据量
     * @author LiFucheng
     * @date 2021/12/27 22:18
     * @version 1.0
     */
    private long receiveLen;
    /**
     * @description: TODO ip地址,需要通过服务器间通信得到需要等待
     * @author LiFucheng
     * @date 2022/1/1 22:35
     * @version 1.0
     */
    private String ip;
    //线程安全的消费者集合
    private final Set<UMsgConsumer> consumerSet;

    public UBTPSocket(int port,int mtu) throws UnknownHostException {
        udpSocket = new UDPSocket(port, mtu);
        udpSocket.addConsumer(this);
        consumerSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        setIp(InetAddress.getLocalHost().getHostAddress());
    }

    /**
     * @description: TODO   启动监听服务
     * @author LiFucheng
     * @date 2021/12/27 22:55
     * @version 1.0
     */
    public void start() throws IOException {
        if (udpSocket.isOpen()) return;
        udpSocket.open();
        new Thread(() -> {
            try {
                System.out.println("ubtp starting listen...");
                udpSocket.listen();
                System.out.println("ubtp stoped listen...");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }
        }).start();
    }
    public void close() throws IOException {
        udpSocket.close();
    }
    /**
     * @description: TODO   发送PDU
     * @author LiFucheng
     * @date 2021/12/26 22:44
     * @version 1.0
     */
    public int send(UMsg msg, SocketAddress address) throws IOException {
        int dataLen = udpSocket.send(UMsg.Codec.encode(msg), address);
        if (Long.MAX_VALUE - dataLen > sendLen) {
            sendLen = 0;
        }
        sendLen += dataLen;
        return dataLen;
    }

    /**
     * @description: TODO   发送PDU
     * @author LiFucheng
     * @date 2021/12/26 22:57
     * @version 1.0
     */
    public int send(UMsg msg, List<SocketAddress> targets) throws IOException {
        int dataLen = udpSocket.send(UMsg.Codec.encode(msg), targets);
        if (Long.MAX_VALUE - dataLen > sendLen) {
            sendLen = 0;
        }
        sendLen += dataLen;
        return dataLen;
    }

    /**
     * @description: TODO   添加MSG的消费者
     * @author LiFucheng
     * @date 2021/12/26 22:44
     * @version 1.0
     */
    public void addUMsgConsumer(UMsgConsumer consumer) {
        consumerSet.add(consumer);
    }

    /**
     * @description: TODO 移除MSG消费者
     * @author LiFucheng
     * @date 2021/12/27 22:45
     * @version 1.0
     */
    public void removeUMsgConsumer(UMsgConsumer consumer) {
        consumerSet.remove(consumer);
    }

    /**
     * @description: TODO 获取网络服务的id
     * @author LiFucheng
     * @date 2021/12/26 15:36
     * @version 1.0
     */
    public int getId() {
        return id;
    }

    /**
     * @description: TODO 设置网络服务的id
     * @author LiFucheng
     * @date 2021/12/26 15:29
     * @version 1.0
     */
    public void setId(int id) {

        this.id = id;
    }

    @Override
    public void accept(PDU pdu, SocketAddress socketAddress) {
        int dataLen = pdu.getData().length + PDU.Codec.HeaderLen;
        if (Long.MAX_VALUE - dataLen > receiveLen) {
            receiveLen = 0;
        }
        receiveLen += dataLen;
        //触发消费
        UMsg msg = UMsg.Codec.decode(pdu);
        for (UMsgConsumer consumer : consumerSet) {
            consumer.accept(msg, socketAddress);
        }
    }

    /**
     * @description: TODO   获取发送数据总量，超出long最大值时会被归零
     * @author LiFucheng
     * @date 2021/12/27 22:23
     * @version 1.0
     */
    public long getSendLen() {
        return sendLen;
    }

    /**
     * @description: TODO   获取接收数据总量，超出long最大值时会被归零
     * @author LiFucheng
     * @date 2021/12/27 22:24
     * @version 1.0
     */
    public long getReceiveLen() {
        return receiveLen;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return udpSocket.getPort();
    }
}
