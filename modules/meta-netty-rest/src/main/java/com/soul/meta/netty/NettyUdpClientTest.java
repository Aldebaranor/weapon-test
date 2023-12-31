package com.soul.meta.netty;

import com.soul.meta.netty.handler.NettyUdpClientHandlerTest;
import com.soul.weapon.config.MetaConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
@Component
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "meta.netty")
@ConditionalOnProperty(prefix = "meta.netty.udp.client", name = "enable",havingValue = "true",matchIfMissing = false)
public class NettyUdpClientTest {

    @Autowired
    private  UnpackMassageFactory factory;

    @Autowired
    private MetaConfig metaConfig;


    private  Bootstrap bootstrap = new Bootstrap() ;

    private  NioEventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    private Integer clientTestPort;

    @PostConstruct
    public void start() throws InterruptedException {
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 100)
                    .option(ChannelOption.SO_SNDBUF,1024 * 1024 * 100)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new NettyUdpClientHandlerTest(factory,metaConfig));
                        }
                    });
            channel = bootstrap.bind(clientTestPort).sync().channel();
            log.info("----------------------------UdpClient2 start success");
    }

    @PreDestroy
    public void destory() throws InterruptedException {
        group.shutdownGracefully().sync();
        log.info("----------------------------关闭Netty");
    }



}