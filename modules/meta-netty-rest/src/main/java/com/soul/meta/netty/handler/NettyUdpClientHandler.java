package com.soul.meta.netty.handler;
import com.soul.meta.netty.UnpackMassageFactory;
import com.soul.meta.facade.UnpackMessageService;
import com.soul.weapon.config.MetaConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/26
 */
@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
public class NettyUdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

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