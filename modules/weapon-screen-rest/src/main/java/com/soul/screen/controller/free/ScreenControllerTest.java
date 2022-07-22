package com.soul.screen.controller.free;

import com.egova.web.annotation.Api;
import com.soul.screen.model.ScreenTctData;
import com.soul.screen.model.TctStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ScreenControllerTest
 * @Description 测试接口,用于调整数据
 * @Author ShiZuan
 * @Date 2022/7/22 10:24
 * @Version
 **/
@RestController
@RequestMapping("/test/screen/")
public class ScreenControllerTest {


    /**
     * 获取水下任务通道报文
     */
    @Api
    @GetMapping("/taskchannelstatus/underwater")
    public List<ScreenTctData> getUnberwaterTaskChannelStatus(){
        List<ScreenTctData> screenTctDataList = new ArrayList<>();
        Long time = System.currentTimeMillis();
        ScreenTctData sc1 = getScreenTctData(time, "001",8);
        ScreenTctData sc2 = getScreenTctData(time, "002",5);
        ScreenTctData sc3 = getScreenTctData(time, "003",6);
        ScreenTctData sc4 = getScreenTctData(time, "004",7);
        ScreenTctData sc5 = getScreenTctData(time, "005",4);

        screenTctDataList.add(sc1);
        screenTctDataList.add(sc2);
        screenTctDataList.add(sc3);
        screenTctDataList.add(sc4);
        screenTctDataList.add(sc5);
        return screenTctDataList;
    }

    private ScreenTctData getScreenTctData(Long time,String targetId,Integer status) {
        ScreenTctData sc1 = new ScreenTctData();
        sc1.setTargetId(targetId);
        TctStatus tct1 = new TctStatus();
        TctStatus tct2 = new TctStatus();
        TctStatus tct3 = new TctStatus();
        TctStatus tct4 = new TctStatus();
        TctStatus tct5 = new TctStatus();
        TctStatus tct6 = new TctStatus();
        TctStatus tct7 = new TctStatus();
        TctStatus tct8 = new TctStatus();

        tct1.setType("1");
        tct1.setTime(time -= (20 * 1000));
        tct1.setDistance(2000.23);

        tct2.setType("2");
        tct2.setTime(time -= (20 * 1000));
        tct2.setDistance(1900.23);

        tct3.setType("3");
        tct3.setTime(time -= (20 * 1000));
        tct3.setDistance(1800.23);

        tct4.setType("4");
        tct4.setTime(time -= (20 * 1000));
        tct4.setDistance(1700.23);

        tct5.setType("5");
        tct5.setTime(time -= (20 * 1000));
        tct5.setDistance(1600.23);

        tct6.setType("6");
        tct6.setTime(time -= (20 * 1000));
        tct6.setDistance(1500.23);

        tct7.setType("7");
        tct7.setName("近程导弹发射");
        tct7.setTime(time -= (20 * 1000));
        tct7.setDistance(1400.23);

        tct8.setType("8");
        tct8.setName("远程导弹发射");
        tct8.setTime(time -= (20 * 1000));
        tct8.setDistance(1300.23);

        List<TctStatus> tctStatusList1 = new ArrayList<>();

        for (Integer i = 1; i <= status; i++) {
            if (i==1) {
                tctStatusList1.add(tct1);
            }
            if (i==2) {
                tctStatusList1.add(tct2);
            }
            if (i==3) {
                tctStatusList1.add(tct3);
            }
            if (i==4) {
                tctStatusList1.add(tct4);
            }
            if (i==5) {
                tctStatusList1.add(tct5);
            }
            if (i==6) {
                tctStatusList1.add(tct6);
            }
            if (i==7) {
                tctStatusList1.add(tct7);
            }
            if (i==8) {
                tctStatusList1.add(tct8);
            }
        }
        sc1.setTctStatusList(tctStatusList1);
        return sc1;
    }
}
