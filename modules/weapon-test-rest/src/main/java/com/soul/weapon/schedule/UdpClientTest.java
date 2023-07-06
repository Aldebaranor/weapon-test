package com.soul.weapon.schedule;

import com.egova.json.utils.JsonUtils;
import com.soul.weapon.model.dds.CombatScenariosInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author: Song
 * @Date 2023/7/6 10:46
 */
@Component
public class UdpClientTest {

    public void startClient(String msg) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ch.pipeline().addLast(new MyUdpEncoder());
                    }
                });
        try{
            Channel channel = b.bind(9001).sync().channel();
            channel.writeAndFlush(msg);
            channel.closeFuture().sync();
            group.shutdownGracefully();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class MyUdpEncoder extends MessageToMessageEncoder<String>{
        InetSocketAddress address = new InetSocketAddress("226.100.100.108",26268);
        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            byte[] bytes = msg.getBytes(CharsetUtil.UTF_8);
            ByteBuf buf = ctx.alloc().buffer(bytes.length);
            buf.writeBytes(bytes);
            DatagramPacket packet = new DatagramPacket(buf, address);
            out.add(packet);
        }
    }

}
