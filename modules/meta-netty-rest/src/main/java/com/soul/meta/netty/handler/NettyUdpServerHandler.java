package com.soul.meta.netty.handler;

import com.soul.meta.facade.UnpackMessageService;
import com.soul.meta.netty.UnpackMassageFactory;
import com.soul.weapon.config.MetaConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhp91
 */
@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
public class NettyUdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
//        String host = "127.0.0.1";
//        if (datagramPacket.sender().getAddress().getHostAddress().contains(host)) {
//            return;
//        }
//        String req = datagramPacket.content().toString(CharsetUtil.UTF_8);
//        System.out.println("收到数据：" + req);
//
//        channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
//                req,CharsetUtil.UTF_8), datagramPacket.sender()));
//    }
    private UnpackMassageFactory factory;

    private MetaConfig metaConfig;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) {

        ByteBuf content = datagramPacket.content();
        String unpackServiceCode = metaConfig.getUnpackServiceCode();
        UnpackMessageService service = factory.get(unpackServiceCode);
        service.unpackNetty(content.copy());
    }
}