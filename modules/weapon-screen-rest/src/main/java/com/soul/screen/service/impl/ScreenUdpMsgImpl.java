package com.soul.screen.service.impl;

import com.soul.meta.facade.UnpackMessageService;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;

/**
 * @ClassName ScreenUdpMsgImpl
 * @Description TODO
 * @Author ShiZuan
 * @Date 2022/7/12 14:39
 * @Version
 **/
@Slf4j
@Service
@Priority(10)
@RequiredArgsConstructor
@Component("Screen-udp")
public class ScreenUdpMsgImpl implements UnpackMessageService {

    @Override
    public void unpackNetty(ByteBuf buf) {
        //标记已读索引
        buf.markReaderIndex();
        System.out.println("接收udp成功!!!");
    }
}
