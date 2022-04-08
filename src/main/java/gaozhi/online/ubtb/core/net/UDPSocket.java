package gaozhi.online.ubtb.core.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lfc
 * @title: UDPSocket
 * @description: TODO UDP通信工具
 * @date 2021/9/5 18:30
 */
 class UDPSocket {
    /**
     * @description:(协议数据单元消费者)
     * @author: gaozhi.online
     * @date: 2021/9/5 23:13
     */
    @FunctionalInterface
    public interface PDUConsumer {
        void accept(PDU pdu, SocketAddress socketAddress);
    }

    /**
     * @description: ${todo 提供udp服务}
     */
    private Selector selector;
    /**
     * @description: TODO 通道
     * @author LiFucheng
     * @date 2021/12/26 22:47
     * @version 1.0
     */
    private DatagramChannel channel;
    //线程安全的消费者集合
    private final Set<PDUConsumer> consumerSet;
    //pdu编解码器
    PDU.Codec codec;
    //缓冲区
    ByteBuffer byteBuffer;
    //端口
    private final int port;

    /**
     * @description: TODO   构造udp通信者
     * @author LiFucheng
     * @date 2021/12/26 22:49
     * @version 1.0
     */
    public UDPSocket(int port, int mtu) {
        this.port = port;
        codec = new PDU.Codec(mtu);
        byteBuffer = codec.generateBuffer();
        consumerSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    /**
     * @description: TODO 添加PDU消费者
     * @author LiFucheng
     * @date 2021/12/26 22:49
     * @version 1.0
     */
    public void addConsumer(PDUConsumer consumer) {
        consumerSet.add(consumer);
    }

    /**
     * @description: TODO   移除PDU消费者
     * @author LiFucheng
     * @date 2021/12/26 22:50
     * @version 1.0
     */
    public void removeConsumer(PDUConsumer consumer) {
        consumerSet.remove(consumer);
    }

    /**
     * @description: (开始监听消息 ， 整个过程是阻塞的 ， 需要单独启用线程)
     * @author: gaozhi.online
     * @date: 2021/9/5 21:13
     * @throws:
     */
    public void listen() throws IOException {
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    SocketAddress socketAddress = channel.receive(byteBuffer);
                    byteBuffer.flip();
                    PDU pdu = codec.decode(byteBuffer);
                    if (pdu != null) {
                        for (PDUConsumer consumer : consumerSet) {
                            consumer.accept(pdu, socketAddress);
                        }
                    }
                    byteBuffer.clear();
                }
            }
            iterator.remove();
        }
    }

    /**
     * @description: TODO 向指定地址发送协议数据单元
     * @author LiFucheng
     * @date 2021/12/26 22:51
     * @version 1.0
     */
    public int send(PDU pdu, SocketAddress target) throws IOException {
        return channel.send(codec.encode(pdu), target);
    }

    /**
     * @description: TODO 向指定地址发送协议数据单元
     * @author LiFucheng
     * @date 2021/12/26 22:51
     * @version 1.0
     */
    public int send(PDU pdu, List<SocketAddress> targets) throws IOException {
        ByteBuffer buffer = codec.encode(pdu);
        int size = 0;
        for (SocketAddress address : targets) {
            size += channel.send(buffer, address);
        }
        return size;
    }

    /**
     * @description: TODO 打开通道
     * @author LiFucheng
     * @date 2021/12/26 22:51
     * @version 1.0
     */
    public void open() throws IOException {
        selector = Selector.open();
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        if (port > 0) {
            channel.bind(new InetSocketAddress(port));
            System.out.println("ubtp initialized with port(s): "+port+" (udp)");
        } else {
            throw new IOException("ubtp initialize port(s) should > 0");
        }
        channel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * @description: TODO 关闭通信通道
     * @author LiFucheng
     * @date 2021/12/26 22:52
     * @version 1.0
     */
    public void close() throws IOException {
        channel.close();
        selector.close();
    }

    /**
     * @description: TODO   判断通道是否打开
     * @author LiFucheng
     * @date 2021/12/26 22:52
     * @version 1.0
     */
    public boolean isOpen() {
        return channel != null && selector != null && channel.isOpen() && selector.isOpen();
    }

    public int getPort() {
        return port;
    }
}
