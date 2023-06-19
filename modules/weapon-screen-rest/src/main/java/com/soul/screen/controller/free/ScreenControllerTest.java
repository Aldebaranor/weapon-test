package com.soul.screen.controller.free;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.soul.screen.model.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @ClassName ScreenControllerTest
 * @Description 测试接口,用于调整数据
 * @Author ShiZuan
 * @Date 2022/7/22 10:24
 * @Version
 **/
@RestController
@RequestMapping("/free/screen")
public class ScreenControllerTest {


    //当前目标ID
    private String SCREEN_TARGETID = "";


    /**
     * 获取选中TargetID
     */
    @Api
    @GetMapping("/sendTargetId/{targetId}")
    public void sendTargetId(@PathVariable String targetId){
        this.SCREEN_TARGETID = targetId;
    }

    /**
     * 获取水下任务通道报文
     */
    @Api
    @PostMapping("/page/taskchannelstatus/underwater")
    public PageResult<ScreenTctData> getUnberwaterTaskChannelStatus(@RequestBody QueryModel<ScreenTctData> model){
        List<ScreenTctData> screenTctDataList = new ArrayList<>();
        Long time = System.currentTimeMillis();
        ScreenTctData sc1 = getScreenTctData(time, "001",8);
        ScreenTctData sc2 = getScreenTctData(time, "002",5);
        ScreenTctData sc3 = getScreenTctData(time, "003",6);
        ScreenTctData sc4 = getScreenTctData(time, "004",7);
        ScreenTctData sc5 = getScreenTctData(time, "004",7);
        ScreenTctData sc6 = getScreenTctData(time, "005",4);
        ScreenTctData sc7 = getScreenTctData(time, "006",5);
        ScreenTctData sc8 = getScreenTctData(time, "007",6);
        ScreenTctData sc9 = getScreenTctData(time, "008",7);
        ScreenTctData sc10 = getScreenTctData(time, "009",8);
        ScreenTctData sc11 = getScreenTctData(time, "010",7);
        ScreenTctData sc12 = getScreenTctData(time, "011",6);
        ScreenTctData sc13 = getScreenTctData(time, "012",6);
        ScreenTctData sc14 = getScreenTctData(time, "013",4);
        ScreenTctData sc15 = getScreenTctData(time, "014",5);

        screenTctDataList.add(sc1);
        screenTctDataList.add(sc2);
        screenTctDataList.add(sc3);
        screenTctDataList.add(sc4);
        screenTctDataList.add(sc5);
        screenTctDataList.add(sc6);
        screenTctDataList.add(sc7);
        screenTctDataList.add(sc8);
        screenTctDataList.add(sc9);
        screenTctDataList.add(sc10);
        screenTctDataList.add(sc11);
        screenTctDataList.add(sc12);
        screenTctDataList.add(sc13);
        screenTctDataList.add(sc14);
        screenTctDataList.add(sc15);

        screenTctDataList.sort(new Comparator<ScreenTctData>() {
            @Override
            public int compare(ScreenTctData o1, ScreenTctData o2) {
                return Integer.valueOf(o1.getTargetId()) - Integer.valueOf(o2.getTargetId());
            }
        });

        ArrayList<ScreenTctData> result = new ArrayList<>();
        Long pageIndex = model.getPaging().getPageIndex();
        Long pageSize = model.getPaging().getPageSize();

        if ((pageIndex-1)*pageSize > screenTctDataList.size()) {
            return null;
        }
        if ((screenTctDataList.size() - (pageSize*pageIndex)) < pageSize) {
            for (long i = (pageIndex-1)*pageSize; i < screenTctDataList.size(); i++) {
                result.add(screenTctDataList.get((int) i));
            }
        }else{
            for (long i = (pageIndex-1)*pageSize; i < pageIndex*pageSize; i++) {
                result.add(screenTctDataList.get((int) i));
            }
        }

        return PageResult.of(result,screenTctDataList.size());
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

    /**
     * 获取水下状态周期报
     */
    @Api
    @GetMapping("/statusreport/watertype")
    public ScreenSrAndRtData getWatertypeStatusReportData(){
        Random random = new Random();
        ScreenSrAndRtData reslut = new ScreenSrAndRtData();
        reslut.setTargetId(this.SCREEN_TARGETID);
        List<ScreenUniversalData> list = new ArrayList<>();
        ScreenUniversalData sc1 = new ScreenUniversalData();
        sc1.setText("001报文");
        sc1.setName("001");
        sc1.setType("1");
        sc1.setValue(random.nextDouble()*10);
        sc1.setAve(random.nextDouble()*10);
        ScreenUniversalData sc2 = new ScreenUniversalData();
        sc2.setText("002报文");
        sc2.setName("002");
        sc2.setType("2");
        sc2.setValue(random.nextDouble()*10);
        sc1.setAve(random.nextDouble()*10);
        ScreenUniversalData sc3 = new ScreenUniversalData();
        sc3.setText("003报文");
        sc3.setName("003");
        sc3.setType("3");
        sc3.setValue(random.nextDouble()*10);
        sc1.setAve(random.nextDouble()*10);
        ScreenUniversalData sc4 = new ScreenUniversalData();
        sc4.setText("004报文");
        sc4.setName("004");
        sc4.setType("4");
        sc4.setValue(random.nextDouble()*10);
        sc1.setAve(random.nextDouble()*10);
        ScreenUniversalData sc5 = new ScreenUniversalData();
        sc5.setText("005报文");
        sc5.setName("005");
        sc5.setType("5");
        sc5.setValue(random.nextDouble()*10);
        sc1.setAve(random.nextDouble()*10);
        ScreenUniversalData sc6 = new ScreenUniversalData();
        sc6.setText("006报文");
        sc6.setName("006");
        sc6.setType("6");
        sc6.setValue(random.nextDouble()*10);
        sc1.setAve(random.nextDouble()*10);
        list.add(sc1);
        list.add(sc2);
        list.add(sc3);
        list.add(sc4);
        list.add(sc5);
        list.add(sc6);
        reslut.setReportData(list);
        return reslut;
    }

    /**
     * 获取水下通道时间
     */
    @Api
    @GetMapping("/responsetime/underwater")
    public ScreenSrAndRtData getUnderwarterResponseTime(){
        Random random = new Random();
        ScreenSrAndRtData reslut = new ScreenSrAndRtData();
        reslut.setTargetId(this.SCREEN_TARGETID);
        List<ScreenUniversalData> list = new ArrayList<>();
        ScreenUniversalData sc1 = new ScreenUniversalData();
        sc1.setText("发现目标");
        sc1.setName("fxmb");
        sc1.setType("1");
        sc1.setValue(random.nextDouble()*500);
        ScreenUniversalData sc2 = new ScreenUniversalData();
        sc2.setText("生成态势");
        sc2.setName("scts");
        sc2.setType("2");
        sc2.setValue(random.nextDouble()*(-100));
        ScreenUniversalData sc3 = new ScreenUniversalData();
        sc3.setText("生成方案");
        sc3.setName("scfa");
        sc3.setType("3");
        sc3.setValue(random.nextDouble()*10);
        ScreenUniversalData sc4 = new ScreenUniversalData();
        sc4.setText("确认方案");
        sc4.setName("qrfa");
        sc4.setType("4");
        sc4.setValue(random.nextDouble()*200);
        ScreenUniversalData sc5 = new ScreenUniversalData();
        sc5.setText("生成诸元");
        sc5.setName("sczy");
        sc5.setType("5");
        sc5.setValue(random.nextDouble()*150);
        ScreenUniversalData sc6 = new ScreenUniversalData();
        sc6.setText("作战执行");
        sc6.setName("zzzx");
        sc6.setType("6");
        sc6.setValue(random.nextDouble()*300);
        list.add(sc1);
        list.add(sc2);
        list.add(sc3);
        list.add(sc4);
        list.add(sc5);
        list.add(sc6);
        reslut.setReportData(list);
        return reslut;
    }

    /**
     * 获取水下数量统计
     */
    @Api
    @GetMapping("/count/watertype")
    public List<ScreenUniversalData> getWatertypeCountData(){
        Random random = new Random();
        List<ScreenUniversalData> list = new ArrayList<>();
        ScreenUniversalData sc1 = new ScreenUniversalData();
        sc1.setText("目标总数");
        sc1.setName("lxyl");
        sc1.setType("1-1");
        sc1.setValue(random.nextInt(300));
        ScreenUniversalData sc2 = new ScreenUniversalData();
        sc2.setText("探测目标数量");
        sc2.setName("tcyl");
        sc2.setType("1-2");
        sc2.setValue(random.nextInt(300));
        ScreenUniversalData sc3 = new ScreenUniversalData();
        sc3.setText("确认目标数量");
        sc3.setName("tsyl");
        sc3.setType("1-3");
        sc3.setValue(random.nextInt(300));
        ScreenUniversalData sc4 = new ScreenUniversalData();
        sc4.setText("打击方案数量");
        sc4.setName("ylfa");
        sc4.setType("1-4");
        sc4.setValue(random.nextInt(300));
        ScreenUniversalData sc5 = new ScreenUniversalData();
        sc5.setText("发射武器数量");
        sc5.setName("fswq");
        sc5.setType("1-5");
        sc5.setValue(random.nextInt(300));
        ScreenUniversalData sc6 = new ScreenUniversalData();
        sc6.setText("拦截目标数量");
        sc6.setName("ljyl");
        sc6.setType("1-6");
        sc6.setValue(random.nextInt(300));
        ScreenUniversalData sc7 = new ScreenUniversalData();
        sc7.setText("鱼雷防御武器");
        sc7.setName("ylfywl");
        sc7.setType("3-1");
        sc7.setValue(random.nextInt(300));
        ScreenUniversalData sc8 = new ScreenUniversalData();
        sc8.setText("鱼雷");
        sc8.setName("yl");
        sc8.setType("3-2");
        sc8.setValue(random.nextInt(300));
        ScreenUniversalData sc9 = new ScreenUniversalData();
        sc9.setText("鱼雷防御武器");
        sc9.setName("ylfywq");
        sc9.setType("4-1");
        sc9.setValue(random.nextInt(300));
        ScreenUniversalData sc10 = new ScreenUniversalData();
        sc10.setText("鱼雷");
        sc10.setName("yl");
        sc10.setType("4-2");
        sc10.setValue(random.nextInt(300));
        ScreenUniversalData sc11 = new ScreenUniversalData();
        sc11.setText("张瑶弹");
        sc11.setName("ylfywl");
        sc11.setType("3-3");
        sc11.setValue(random.nextInt(300));
        ScreenUniversalData sc12 = new ScreenUniversalData();
        sc12.setText("施钻弹");
        sc12.setName("yl");
        sc12.setType("3-4");
        sc12.setValue(random.nextInt(300));
        ScreenUniversalData sc13 = new ScreenUniversalData();
        sc13.setText("张瑶弹");
        sc13.setName("ylfywq");
        sc13.setType("4-3");
        sc13.setValue(random.nextInt(300));
        ScreenUniversalData sc14 = new ScreenUniversalData();
        sc14.setText("施钻弹");
        sc14.setName("yl");
        sc14.setType("4-4");
        sc14.setValue(random.nextInt(300));
        list.add(sc1);
        list.add(sc2);
        list.add(sc3);
        list.add(sc4);
        list.add(sc5);
        list.add(sc6);
        list.add(sc7);
        list.add(sc8);
        list.add(sc9);
        list.add(sc10);
        list.add(sc11);
        list.add(sc12);
        list.add(sc13);
        list.add(sc14);
        return list;
    }

    /**
     * 获取探测器精度报文
     */
    @Api
    @GetMapping("/detectoraccuracy/underwater/{number}")
    public ScreenAccuracyData getScreenDetectorAccuracyData(@PathVariable Integer number){
        Random random = new Random();
        ScreenAccuracyData screenAccuracyData = new ScreenAccuracyData();
        List<AccuracyData> resultList = new ArrayList<>();
        if (this.SCREEN_TARGETID.equals("001")) {
            screenAccuracyData.setTargetId(this.SCREEN_TARGETID);
            AccuracyData ad1 = new AccuracyData("1",random.nextDouble()*10, random.nextDouble()*10, System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad2 = new AccuracyData("1",random.nextDouble()*10, random.nextDouble()*10, System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad3 = new AccuracyData("1",random.nextDouble()*10, random.nextDouble()*10, System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad4 = new AccuracyData("1",random.nextDouble()*10, random.nextDouble()*10, System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad5 = new AccuracyData("1",random.nextDouble()*10, random.nextDouble()*10, System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad6 = new AccuracyData("1",random.nextDouble()*10, random.nextDouble()*10, System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad7 = new AccuracyData("1",random.nextDouble()*10, random.nextDouble()*10, System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad8 = new AccuracyData("1",random.nextDouble()*10, random.nextDouble()*10, System.currentTimeMillis()-random.nextInt(1000000));
            resultList.add(ad1);
            resultList.add(ad2);
            resultList.add(ad3);
            resultList.add(ad4);
            resultList.add(ad5);
            resultList.add(ad6);
            resultList.add(ad7);
            resultList.add(ad8);
        }else{
            screenAccuracyData.setTargetId(this.SCREEN_TARGETID);
            AccuracyData ad1 = new AccuracyData("2",random.nextDouble()*10, random.nextDouble()*10,random.nextDouble()*10,System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad2 = new AccuracyData("2",random.nextDouble()*10, random.nextDouble()*10,random.nextDouble()*10,System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad3 = new AccuracyData("2",random.nextDouble()*10, random.nextDouble()*10,random.nextDouble()*10,System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad4 = new AccuracyData("2",random.nextDouble()*10, random.nextDouble()*10,random.nextDouble()*10,System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad5 = new AccuracyData("2",random.nextDouble()*10, random.nextDouble()*10,random.nextDouble()*10,System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad6 = new AccuracyData("2",random.nextDouble()*10, random.nextDouble()*10,random.nextDouble()*10,System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad7 = new AccuracyData("2",random.nextDouble()*10, random.nextDouble()*10,random.nextDouble()*10,System.currentTimeMillis()-random.nextInt(1000000));
            AccuracyData ad8 = new AccuracyData("2",random.nextDouble()*10, random.nextDouble()*10,random.nextDouble()*10,System.currentTimeMillis()-random.nextInt(1000000));
            resultList.add(ad1);
            resultList.add(ad2);
            resultList.add(ad3);
            resultList.add(ad4);
            resultList.add(ad5);
            resultList.add(ad6);
            resultList.add(ad7);
            resultList.add(ad8);
        }
        resultList.sort(new Comparator<AccuracyData>() {
            @Override
            public int compare(AccuracyData o1, AccuracyData o2) {
                return (int) (o1.getTime() - o2.getTime());
            }
        });

        screenAccuracyData.setAccuracyData(resultList);

        return screenAccuracyData;
    }

    /**
     * 获取发射架精度报文
     */
    @Api
    @GetMapping("/launcheraccuracy/underwater/{number}")
    public ScreenAccuracyData getScreenLauncherAccuracyData(@PathVariable Integer number){
        Random random = new Random();
        ScreenAccuracyData screenAccuracyData = new ScreenAccuracyData();
        List<AccuracyData> resultList = new ArrayList<>();
        screenAccuracyData.setTargetId(this.SCREEN_TARGETID);
        AccuracyData ad1 = new AccuracyData("1-2",random.nextDouble()*10, random.nextDouble()*10,null,System.currentTimeMillis()-random.nextInt(1000000));
        AccuracyData ad2 = new AccuracyData("2-1",random.nextDouble()*10, random.nextDouble()*10,null,System.currentTimeMillis()-random.nextInt(1000000));
        AccuracyData ad3 = new AccuracyData("2-2",random.nextDouble()*10, random.nextDouble()*10,null,System.currentTimeMillis()-random.nextInt(1000000));
        AccuracyData ad4 = new AccuracyData("12-5",random.nextDouble()*10, random.nextDouble()*10,null,System.currentTimeMillis()-random.nextInt(1000000));
        AccuracyData ad5 = new AccuracyData("2-15",random.nextDouble()*10, random.nextDouble()*10,null,System.currentTimeMillis()-random.nextInt(1000000));
        AccuracyData ad6 = new AccuracyData("25-1",random.nextDouble()*10, random.nextDouble()*10,null,System.currentTimeMillis()-random.nextInt(1000000));
        AccuracyData ad7 = new AccuracyData("26-3",random.nextDouble()*10, random.nextDouble()*10,null,System.currentTimeMillis()-random.nextInt(1000000));
        AccuracyData ad8 = new AccuracyData("12-8",random.nextDouble()*10, random.nextDouble()*10,null,System.currentTimeMillis()-random.nextInt(1000000));
        resultList.add(ad1);
        resultList.add(ad2);
        resultList.add(ad3);
        resultList.add(ad4);
        resultList.add(ad5);
        resultList.add(ad6);
        resultList.add(ad7);
        resultList.add(ad8);
        resultList.sort(new Comparator<AccuracyData>() {
            @Override
            public int compare(AccuracyData o1, AccuracyData o2) {
                return (int) (o1.getTime() - o2.getTime());
            }
        });
        screenAccuracyData.setAccuracyData(resultList);
        return screenAccuracyData;
    }

    /**
     * 获取树状图
     */
    @Api
    @GetMapping("/tree/status")
    public FlowchartStatus getFlowchartStatus(){

        FlowchartStatus flowchartStatus = new FlowchartStatus();
        HashSet<String> result = new HashSet<>();
        flowchartStatus.setTargetId(this.SCREEN_TARGETID);
        flowchartStatus.setSensorStatus(true);
        flowchartStatus.setSensorType(1);
        flowchartStatus.setChargeDeviceStatus(true);
        flowchartStatus.setSituationType(1);
        flowchartStatus.setMissionType(1);
        flowchartStatus.setCombatExecutionStatus(true);
        result.add("1-1-1");
        result.add("1-2-1");
        result.add("1-3-2");
        result.add("1-4-1");
        result.add("1-5-2");
        flowchartStatus.setWeaponType(result);
        return flowchartStatus;
    }

}
