package com.client.server;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CpmpNettyServer {

    private final int port;

    private final String ip;


    public CpmpNettyServer(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    //记录客户端的信息和ctx
    public static Map<String, ChannelHandlerContext> contextMap = new ConcurrentHashMap();

    //服务端的IP地址
    public void start() {

        Bootstrap client = new Bootstrap();

        //第1步 定义线程组，处理读写和链接事件，没有了accept事件
        EventLoopGroup group = new NioEventLoopGroup();
        client.group(group);

        //第2步 绑定客户端通道
        client.channel(NioSocketChannel.class);

        //第3步 给NIoSocketChannel初始化handler， 处理读写事件
        client.handler(new ChannelInitializer<NioSocketChannel>() {  //通道是NioSocketChannel
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                //字符串编码器，一定要加在SimpleClientHandler 的上面
                ch.pipeline().addLast(new CpmpMsgDecoder1());
            }
        });


        //连接服务器
        ChannelFuture future = null;
        try {
            future = client.connect(ip, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //发送数据给服务器

        future.channel().writeAndFlush("132");

        for(int i=0;i<5;i++){
            String msg = "ssss"+i+"\r\n";
            future.channel().writeAndFlush(msg);
        }

        //当通道关闭了，就继续往下走
        try {
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //接收服务端返回的数据
        AttributeKey<String> key = AttributeKey.valueOf("ServerData");
        Object result = future.channel().attr(key).get();
        System.out.println(result.toString());
    }
}
