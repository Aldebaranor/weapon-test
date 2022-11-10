package com.soul.screen.service.impl;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisService;
import com.egova.redis.RedisUtils;
import com.flagwind.mybatis.utils.CollectionUtils;
import com.soul.meta.facade.UnpackMessageService;
import com.soul.screen.model.*;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName ScreenUdpMsgImpl
 * @Description 大屏数据解析
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


    @Autowired
    private CommonConfig config;

    //关于目标的总值 key:system-type-targetId value:总值
    private ConcurrentHashMap<String,Double> aveTVMap = new ConcurrentHashMap<>();

    //关于目标的总计数 key:system-type-targetId value:总计数
    private ConcurrentHashMap<String,Integer> aveCMap = new ConcurrentHashMap<>();


    @Override
    public void unpackNetty(ByteBuf buf) {
        //标记已读索引
        buf.markReaderIndex();
        //报文标识
        int type = buf.readInt();
        //时戳
        double Timestamp = buf.readDouble();
        //解析报文数据
        switch (type) {
            case 0:
                Msg0_SendScenarioStatus_Struct(buf);
                break;
            case 1:
                Msg1_Nums_Struct(buf);
                break;
            case 2:
                Msg2_NumsAndTarget_Struct(buf);
                break;
            case 3:
                Msg3_FrequencyAndRespond_Struct(buf);
                break;
            case 4:
                Msg4_FirstTime_Struct(buf);
                break;
            case 5:
                Msg5_ZY_Num_Struct(buf);
                break;
            case 6:
                Msg6_LaunchResult(buf);
                break;
            case 7:
                Msg7_SonarDirAndDis(buf);
                break;
            case 8:
                Msg8_WeaponStateAndDis_Struct(buf);
                break;
            default:
                log.error("暂无对应报文解析,请查看报文头!");
                break;
        }
    }

    private void Msg0_SendScenarioStatus_Struct(ByteBuf buf) {
        int i = buf.readInt();
        if (i == 1) {
            //开始新的想定,清除所有数据
            Set<String> keys = RedisUtils.getService(config.getScreenDataBase()).keys("SCREEN:*");
            List<String> keys1 = keys.stream().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(keys)) {
                RedisUtils.getService(config.getScreenDataBase()).deletes((keys1));
                aveCMap.clear();
                aveTVMap.clear();
            }
        }

        log.info("报文<0>收到清除命令!!");
    }

    /**
     * 报文1解析
     *
     * @param buf
     */
    private void Msg1_Nums_Struct(ByteBuf buf) {

        //目标类型 1.对空 2.水下
        int type1 = buf.readInt();
        //数据长度
        int length = buf.readInt();
        if (type1 == 1) {
            //对空操作
            return;
        } else if (type1 == 2) {
            String key = Constant.SCREEN_COUNT_WATERTYPE;
            //水下操作
            for (int i = 0; i < length; i++) {
                //数量类型
                /*  1.来袭鱼雷 2.来袭潜艇 3.声纳探测鱼雷数量 4.声纳探测潜艇数量
                    5.态势处理的鱼雷数量 6.态势处理的潜艇数量 7.成功拦截鱼雷目标的数量
                    8.582装填数量 9.578装填数量 10.582余弹量 11.578余弹量
                */
                int type2 = buf.readInt();
                ScreenUniversalData screenUniversalData = new ScreenUniversalData();
                switch (type2) {
                    case 1:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("1-1");
                        screenUniversalData.setText("来袭鱼雷数量");
                        screenUniversalData.setName("lxyl");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 2:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("2-1");
                        screenUniversalData.setText("来袭潜艇数量");
                        screenUniversalData.setName("lxqt");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 3:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("1-2");
                        screenUniversalData.setText("探测鱼雷数量");
                        screenUniversalData.setName("tcyl");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 4:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("2-2");
                        screenUniversalData.setText("探测潜艇数量");
                        screenUniversalData.setName("tcqt");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 5:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("1-3");
                        screenUniversalData.setText("态势鱼雷数量");
                        screenUniversalData.setName("tsyl");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 6:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("2-3");
                        screenUniversalData.setText("态势潜艇数量");
                        screenUniversalData.setName("tsqt");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 7:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("1-6");
                        screenUniversalData.setText("拦截鱼雷数量");
                        screenUniversalData.setName("ljyl");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 8:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("3-1");
                        screenUniversalData.setText("582载弹数量");
                        screenUniversalData.setName("582zd");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 9:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("3-2");
                        screenUniversalData.setText("578载弹数量");
                        screenUniversalData.setName("578zd");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 10:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("4-1");
                        screenUniversalData.setText("582余弹数量");
                        screenUniversalData.setName("578yd");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    case 11:
                        screenUniversalData.setValue(buf.readInt());
                        screenUniversalData.setType("4-2");
                        screenUniversalData.setText("578余弹数量");
                        screenUniversalData.setName("578yd");
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(type2), JsonUtils.serialize(screenUniversalData));
                        break;
                    default:
                        log.info("报文1暂无此类型type:" + type2 + "---值为:" + buf.readInt());
                        break;
                }
            }
            double timev = buf.readDouble();
            log.info("收到水下报文<1>----时戳:" + timev);
        }

    }

    /**
     * 报文2解析
     *
     * @param buf
     */
    private void Msg2_NumsAndTarget_Struct(ByteBuf buf) {
        //目标类型 1.对空 2.水下
        int type1 = buf.readInt();
        //数据长度
        int length = buf.readInt();

        if (type1 == 1) {
            return;
        } else if (type1 == 2) {
            //目前水下只有鱼雷,所以统计方案确认的数量以及武器发射的数量
            int faqr = 0;
            int wqfs = 0;
            for (int i = 0; i < length; i++) {
                int type2 = buf.readInt();
                int type3 = buf.readInt();
                int num = buf.readInt();
                if (type3 == 2) {
                    faqr = faqr + num;
                }
                if (type3 == 3 || type3 == 4 || type3 == 5) {
                    wqfs = wqfs + num;
                }
            }
            double timev = buf.readDouble();
            ScreenUniversalData screenUniversalData1 = new ScreenUniversalData();
            ScreenUniversalData screenUniversalData2 = new ScreenUniversalData();
            screenUniversalData1.setName("ylfa");
            screenUniversalData1.setType("1-4");
            screenUniversalData1.setText("鱼雷方案数量");
            screenUniversalData1.setValue(faqr);
            screenUniversalData2.setName("fswq");
            screenUniversalData2.setType("1-5");
            screenUniversalData2.setText("发射武器数量");
            screenUniversalData2.setValue(wqfs);
            String key = Constant.SCREEN_COUNT_WATERTYPE;
            RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put("ylfa", JsonUtils.serialize(screenUniversalData1));
            RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put("fswq", JsonUtils.serialize(screenUniversalData2));
            log.info("收到水下报文<2>----时戳:" + timev);
        }
    }

    /**
     * 报文3解析
     *
     * @param buf
     */
    private void Msg3_FrequencyAndRespond_Struct(ByteBuf buf) {

        //目标类型 1.对空 2.水下
        int system = buf.readInt();

        if (system == 1) {
            return;
        } else if (system == 2) {
            //1.声呐状态报文周期  2.水下方面状态 3.综合电子对抗状态
            //17-目标批号-声呐目标数据更新周期
            //18-目标批号-态势处理数据更新周期
            //19-目标批号-方案更新周期
            //20-目标批号-诸元周期
            //33-目标批号 声呐-态势反应时间
            //34-目标批号 态势-方案反应时间
            //35已用 36已用
            int type1 = buf.readInt();
            //目标批号
            int targetbatch = buf.readInt();
            double timevalue = buf.readDouble();
            double timestamp = buf.readDouble();
            String key1 = Constant.SCREEN_STATUSREPORTDATA_WATERTYPE;
            String key2 = String.format(Constant.SCREEN_STATUSREPORTDATA_WATERTYPE_TARGETID, targetbatch);
            String key3 = String.format(Constant.SCREEN_RESPONSETIME_WATERTYPE_TARGETID, targetbatch);
            ScreenUniversalData screenUniversalData = new ScreenUniversalData();
            String mapKey1 = system+"-"+type1;
            String mapKey2 = mapKey1+"-"+targetbatch;

            if (aveTVMap.containsKey(mapKey2) && aveCMap.containsKey(mapKey2)) {
                aveTVMap.put(mapKey2,(aveTVMap.get(mapKey2) + timevalue));
                aveCMap.put(mapKey2,(aveCMap.get(mapKey2) + 1));
            }else{
                aveTVMap.put(mapKey2,timevalue);
                aveCMap.put(mapKey2,1);
            }

            switch (type1) {
                case 1:
                    screenUniversalData.setText("声呐信息报文");
                    screenUniversalData.setName("snxxbw");
                    screenUniversalData.setType("1");
                    screenUniversalData.setValue(timevalue);
                    screenUniversalData.setAve(Double.valueOf(new DecimalFormat("#.00").format(aveTVMap.get(mapKey2)/aveCMap.get(mapKey2))));
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key1).put("snxxbw", JsonUtils.serialize(screenUniversalData));
                    break;
                case 2:
                    screenUniversalData.setText("水下方面报文");
                    screenUniversalData.setName("sxfmbw");
                    screenUniversalData.setType("2");
                    screenUniversalData.setValue(timevalue);
                    screenUniversalData.setAve(Double.valueOf(new DecimalFormat("#.00").format(aveTVMap.get(mapKey2)/aveCMap.get(mapKey2))));
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key1).put("sxfmbw", JsonUtils.serialize(screenUniversalData));
                    break;
                case 3:
                    screenUniversalData.setText("电子对抗报文");
                    screenUniversalData.setName("dzdkbw");
                    screenUniversalData.setType("3");
                    screenUniversalData.setValue(timevalue);
                    screenUniversalData.setAve(Double.valueOf(new DecimalFormat("#.00").format(aveTVMap.get(mapKey2)/aveCMap.get(mapKey2))));
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key1).put("dzdkbw", JsonUtils.serialize(screenUniversalData));
                    break;
                case 17:
                    screenUniversalData.setText("声呐目标报文");
                    screenUniversalData.setName("snmbbw");
                    screenUniversalData.setType("17");
                    screenUniversalData.setValue(timevalue);
                    screenUniversalData.setAve(Double.valueOf(new DecimalFormat("#.00").format(aveTVMap.get(mapKey2)/aveCMap.get(mapKey2))));
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).put("snmbbw", JsonUtils.serialize(screenUniversalData));
                    break;
                case 18:
                    screenUniversalData.setText("态势处理报文");
                    screenUniversalData.setName("tsclbw");
                    screenUniversalData.setType("18");
                    screenUniversalData.setValue(timevalue);
                    screenUniversalData.setAve(Double.valueOf(new DecimalFormat("#.00").format(aveTVMap.get(mapKey2)/aveCMap.get(mapKey2))));
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).put("tsclbw", JsonUtils.serialize(screenUniversalData));
                    break;
                case 19:
                    screenUniversalData.setText("方案更新报文");
                    screenUniversalData.setName("fagxbw");
                    screenUniversalData.setType("19");
                    screenUniversalData.setValue(timevalue);
                    screenUniversalData.setAve(Double.valueOf(new DecimalFormat("#.00").format(aveTVMap.get(mapKey2)/aveCMap.get(mapKey2))));
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).put("fagxbw", JsonUtils.serialize(screenUniversalData));
                    break;
                case 20:
                    screenUniversalData.setText("诸元信息报文");
                    screenUniversalData.setName("zyxxbw");
                    screenUniversalData.setType("20");
                    screenUniversalData.setValue(timevalue);
                    screenUniversalData.setAve(Double.valueOf(new DecimalFormat("#.00").format(aveTVMap.get(mapKey2)/aveCMap.get(mapKey2))));
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).put("zyxxbw", JsonUtils.serialize(screenUniversalData));
                    break;

                case 33:
                    screenUniversalData.setText("声呐-态势");
                    screenUniversalData.setName("sn-ts");
                    screenUniversalData.setType("33");
                    screenUniversalData.setValue(timevalue);
                    screenUniversalData.setAve(Double.valueOf(new DecimalFormat("#.00").format(aveTVMap.get(mapKey2)/aveCMap.get(mapKey2))));
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key3).put("sn-ts", JsonUtils.serialize(screenUniversalData));
                    break;
                case 34:
                    screenUniversalData.setText("态势-方案");
                    screenUniversalData.setName("ts-fa");
                    screenUniversalData.setType("34");
                    screenUniversalData.setValue(timevalue);
                    screenUniversalData.setAve(Double.valueOf(new DecimalFormat("#.00").format(aveTVMap.get(mapKey2)/aveCMap.get(mapKey2))));
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key3).put("ts-fa", JsonUtils.serialize(screenUniversalData));
                    break;
                default:
                    log.info("报文3-暂无对应报文type");
                    break;
            }
            log.info("收到水下报文<3>----时戳:" + timestamp);
        }

    }

    /**
     * 报文4解析
     *
     * @param buf
     */
    private void Msg4_FirstTime_Struct(ByteBuf buf) {
        //1.对空 2.水下
        int system = buf.readInt();
        if (system == 1) {
            return;
        } else if (system == 2) {
            //1-首次收到鱼雷声呐目标信息；
            //2-首次收到态势处理信息；
            //3-首次收到方案报；
            //4-首次收到方案确认报；
            //5-首次收到诸元报；
            //6-首次收到诸元确认报；
            //7-首次收到潜艇声呐目标信息;
            int type = buf.readInt();
            int type1;

            if (type == 1 || type == 7) {
                type1 = 1;
            }else{
                type1 = type;
            }
            //目标批号
            int targetbatch = buf.readInt();
            double timevalue = buf.readDouble() * 1000;
            double distace = Double.valueOf(new DecimalFormat("#.00").format(buf.readDouble()));
            double timestamp = buf.readDouble();
            String key = Constant.SCREEN_TASKCHANNELSTATUS_WATERTYPE;
            String key2 = Constant.SCREEN_LIUCHENGTU_WATERTYPE;
            //流程图处理
            if (RedisUtils.getService(config.getScreenDataBase()).exists(key2)) {
                if (RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).entries().containsKey(String.valueOf(targetbatch))) {
                    String json = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).entries().get(String.valueOf(targetbatch));
                    FlowchartStatus deserialize = JsonUtils.deserialize(json, FlowchartStatus.class);
                    if (deserialize.getWeaponType() == null) {
                        deserialize.setWeaponType(new HashSet<>(0));
                    }
                    switch (type){
                        case 1:
                            deserialize.setSensorStatus(true);
                            deserialize.setSensorType(1);
                            break;
                        case 2:
                            deserialize.setChargeDeviceStatus(true);
                            deserialize.setSituationType(1);
                            break;
                        case 3:
                            deserialize.setUnderwaterType(1);
                            break;
                        case 7:
                            deserialize.setSensorStatus(true);
                            deserialize.setSensorType(2);
                            break;
                        default: break;
                    }
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).put(String.valueOf(targetbatch),JsonUtils.serialize(deserialize));
                }else{
                    FlowchartStatus deserialize = new FlowchartStatus();
                    deserialize.setTargetId(String.valueOf(targetbatch));
                    HashSet<String> set = new HashSet<>();
                    deserialize.setWeaponType(set);
                    switch (type){
                        case 1:
                            deserialize.setSensorStatus(true);
                            deserialize.setSensorType(1);
                            break;
                        case 2:
                            deserialize.setChargeDeviceStatus(true);
                            deserialize.setSituationType(1);
                            break;
                        case 3:
                            deserialize.setUnderwaterType(1);
                            break;
                        case 7:
                            deserialize.setSensorStatus(true);
                            deserialize.setSensorType(2);
                            break;
                        default: break;
                    }
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).put(String.valueOf(targetbatch),JsonUtils.serialize(deserialize));
                }
            }else{
                FlowchartStatus deserialize = new FlowchartStatus();
                deserialize.setTargetId(String.valueOf(targetbatch));
                HashSet<String> set = new HashSet<>();
                deserialize.setWeaponType(set);
                switch (type){
                    case 1:
                        deserialize.setSensorStatus(true);
                        deserialize.setSensorType(1);
                        break;
                    case 2:
                        deserialize.setChargeDeviceStatus(true);
                        deserialize.setSituationType(1);
                        break;
                    case 3:
                        deserialize.setUnderwaterType(1);
                        break;
                    case 7:
                        deserialize.setSensorStatus(true);
                        deserialize.setSensorType(2);
                        break;
                    default: break;
                }
                RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).put(String.valueOf(targetbatch),JsonUtils.serialize(deserialize));
            }

            //任务通道处理
            if (RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
                if (!RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).hasKey(String.valueOf(targetbatch))) {
                    ScreenTctData screenTctData = new ScreenTctData();
                    screenTctData.setTargetId(String.valueOf(targetbatch));
                    List<TctStatus> list = new ArrayList<>();
                    TctStatus tc = new TctStatus();
                    tc.setType(String.valueOf(type1));
                    tc.setDistance(distace);
                    tc.setTime((long) timevalue);
                    list.add(tc);
                    screenTctData.setTctStatusList(list);
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(targetbatch), JsonUtils.serialize(screenTctData));
                } else {
                    String json = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).entries().get(String.valueOf(targetbatch));
                    ScreenTctData deserialize = JsonUtils.deserialize(json, ScreenTctData.class);
                    List<TctStatus> tctStatusList = deserialize.getTctStatusList();
                    Map<String, TctStatus> collect = tctStatusList.stream().collect(Collectors.toMap(TctStatus::getType, Function.identity(), (keyA, keyB) -> keyB));
                    String key3 = String.format(Constant.SCREEN_RESPONSETIME_WATERTYPE_TARGETID, targetbatch);
                    ScreenUniversalData screenUniversalData = new ScreenUniversalData();
                    //处理通道反应时间
                    if (type1 == 5) {
                        screenUniversalData.setText("方案-诸元");
                        screenUniversalData.setName("fa-zy");
                        screenUniversalData.setType("35");
                        double time = timevalue - collect.get("4").getTime();
                        screenUniversalData.setValue(time/1000);
                        screenUniversalData.setAve(time/1000);
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key3).put("fa-zy", JsonUtils.serialize(screenUniversalData));
                    }
                    if (type1 == 6) {
                        screenUniversalData.setText("诸元-作战");
                        screenUniversalData.setName("zy-zz");
                        screenUniversalData.setType("36");
                        double time = timevalue - collect.get("5").getTime();
                        screenUniversalData.setValue(time/1000);
                        screenUniversalData.setAve(time/1000);
                        RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key3).put("zy-zz", JsonUtils.serialize(screenUniversalData));
                    }
                    TctStatus tctStatus = new TctStatus();
                    tctStatus.setType(String.valueOf(type1));
                    tctStatus.setTime((long) timevalue);
                    tctStatus.setDistance(distace);
                    collect.put(String.valueOf(type1),tctStatus);
                    List<TctStatus> values = collect.values().stream().collect(Collectors.toList());
                    deserialize.setTctStatusList(values);
                    RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(targetbatch), JsonUtils.serialize(deserialize));
                }
            } else {
                ScreenTctData screenTctData = new ScreenTctData();
                screenTctData.setTargetId(String.valueOf(targetbatch));
                List<TctStatus> list = new ArrayList<>();
                TctStatus tc = new TctStatus();
                tc.setType(String.valueOf(type1));
                tc.setDistance(distace);
                tc.setTime((long) timevalue);
                list.add(tc);
                screenTctData.setTctStatusList(list);
                RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(targetbatch), JsonUtils.serialize(screenTctData));
            }
            log.info("收到水下报文<4>----时戳:" + timestamp);
        }
    }

    /**
     * 报文5解析
     *
     * @param buf
     */
    private void Msg5_ZY_Num_Struct(ByteBuf buf) {

        int system = buf.readInt();
        int targetBatch = buf.readInt();
        int launchMissileNumber = buf.readInt();
        int fsj = buf.readInt();
        int hole = buf.readInt();
        double sideAngle = buf.readDouble();
        double pitch = buf.readDouble();


        if (system == 1) {
            //对空处理
        } else if (system == 2) {
            //水下处理
            String key = String.format(Constant.SCREEN_QUERENZHUYUAN_WATERTYPE_TARGETID, targetBatch, fsj);
            Map<String, Double> result = new HashMap<>();
            result.put("xuanjiao", sideAngle);
            result.put("yangjiao", pitch);
            RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).put(String.valueOf(hole), JsonUtils.serialize(result));
        }

        log.info("收到水下报文<5>");

    }

    /**
     * 报文6解析
     *
     * @param buf
     */
    private void Msg6_LaunchResult(ByteBuf buf) {

        //1.水下 2.对空
        int system = buf.readInt();
        //是否成功
        int bSuccessSign = buf.readInt();
        //发射装置Id
        int iLauncherId = buf.readInt();
        //发射孔
        int iHoleId = buf.readInt();
        //武器类型
        int iWeaponType = buf.readInt();
        //发射舷角
        double dLaunchHeading = buf.readDouble();
        //发射仰角
        double dLaunchPitch = buf.readDouble();
        //对抗目标批号
        int iAimBatch = buf.readInt();
        //距离
        double distance = Double.valueOf(new DecimalFormat("#.00").format(buf.readDouble()));
        //弹发射时间
        double dLaunchTime = buf.readDouble() * 1000;

        log.info("报文<6>数据：system："+system+"bSuccessSign:"+bSuccessSign+"iLauncherId:"+iLauncherId+"iHoleId:"+iHoleId+"iWeaponType:"+iWeaponType+"dLaunchHeading:"+dLaunchHeading+"dLaunchPitch:"+dLaunchPitch
        +"iAimBatch:"+iAimBatch+"distance:"+distance+"dLaunchTime"+dLaunchTime);
        RedisService service = RedisUtils.getService(config.getScreenDataBase());

        if (system == 1) {
            //对空数据处理
        } else if (system == 2) {
            //水下数据处理

            //流程图处理
            String liuchengKey = Constant.SCREEN_LIUCHENGTU_WATERTYPE;
            String json = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(liuchengKey).entries().get(String.valueOf(iAimBatch));
            FlowchartStatus deserialize = JsonUtils.deserialize(json, FlowchartStatus.class);
            deserialize.setCombatExecutionStatus(true);
            if (CollectionUtils.isNotEmpty(deserialize.getWeaponType())) {
                HashSet<String> weaponType = deserialize.getWeaponType();
                if (iWeaponType == 9) {
                    weaponType.add(1+"-"+iLauncherId+"-"+1);
                    deserialize.setWeaponType(weaponType);
                } else if (iWeaponType == 10) {
                    weaponType.add(1+"-"+iLauncherId+"-"+2);
                    deserialize.setWeaponType(weaponType);
                }
            }else{
                HashSet<String> weaponType = new HashSet<>();
                if (iWeaponType == 9) {
                    weaponType.add(1+"-"+iLauncherId+"-"+1);
                    deserialize.setWeaponType(weaponType);
                } else if (iWeaponType == 10) {
                    weaponType.add(1+"-"+iLauncherId+"-"+2);
                    deserialize.setWeaponType(weaponType);
                }
            }

            RedisUtils.getService(config.getScreenDataBase()).boundHashOps(liuchengKey).put(String.valueOf(iAimBatch),JsonUtils.serialize(deserialize));
            //处理任务通道的武器状态
            String key1 = Constant.SCREEN_TASKCHANNELSTATUS_WATERTYPE;
            String json1 = service.boundHashOps(key1).entries().get(String.valueOf(iAimBatch));
            ScreenTctData screenTctData = JsonUtils.deserialize(json1, ScreenTctData.class);
            List<TctStatus> tctStatusList = screenTctData.getTctStatusList();
            String type1 = String.valueOf(iWeaponType - 2);
            Map<String, TctStatus> collect = tctStatusList.stream().collect(Collectors.toMap(TctStatus::getType, Function.identity(), (keyA, keyB) -> keyB));
            TctStatus tctStatus = new TctStatus();
            tctStatus.setType(type1);
            tctStatus.setTime((long) dLaunchTime);
            tctStatus.setDistance(distance);
            if (iWeaponType == 9) {
                tctStatus.setName("声干扰:发射");
            } else if (iWeaponType == 10) {
                tctStatus.setName("声诱饵:发射");
            }
            collect.put(type1,tctStatus);
            List<TctStatus> values = collect.values().stream().collect(Collectors.toList());
            screenTctData.setTctStatusList(values);
            service.boundHashOps(key1).put(String.valueOf(iAimBatch), JsonUtils.serialize(screenTctData));
            //处理发射架精度数据
            String key2 = String.format(Constant.SCREEN_QUERENZHUYUAN_WATERTYPE_TARGETID, iAimBatch, iLauncherId);
            String key3 = String.format(Constant.SCREEN_LAUNCHERROTATIONACCURACY_WATERTYPE_TARGETID, iAimBatch);
            String json2 = service.boundHashOps(key2).entries().get(String.valueOf(iHoleId));
            Map map = JsonUtils.deserialize(json2, Map.class);
            Double xuanjiao = (Double) map.get("xuanjiao");
            Double yangjiao = (Double) map.get("yangjiao");
            AccuracyData accuracyData = new AccuracyData();
            accuracyData.setTime((long) dLaunchTime);
            accuracyData.setDeflectionAngleAccuracy(Math.abs(xuanjiao - dLaunchHeading));
            accuracyData.setPitchAngleAccuracy(Math.abs(yangjiao - dLaunchPitch));
            accuracyData.setType(iLauncherId + "-" + iHoleId);
            service.boundZSetOps(key3).add(JsonUtils.serialize(accuracyData), dLaunchTime);
        }

        log.info("收到水下报文<6>");
    }

    /**
     * 报文7解析
     *
     * @param buf
     */
    private void Msg7_SonarDirAndDis(ByteBuf buf) {

        //1.对空 2.水下
        int system = buf.readInt();
        //探测设备类型 1.声呐 2.雷达
        int type = buf.readInt();
        //目标批号
        int targetBatch = buf.readInt();
        //声呐报参数方位
        double sonarMsgDir = buf.readDouble();
        //声呐报参数距离
        double sonarMsgDis = buf.readDouble();
        //仰角 (雷达会有)
        double pitch = buf.readDouble();
        //相对于声呐模型方位
        double sonarModelDir = buf.readDouble();
        //相对于声呐模型距离
        double sonarModelDis = buf.readDouble();
        //相对于模型仰角 (雷达会有)
        double modelPitch = buf.readDouble();
        //时戳
        double timeV = buf.readDouble() * 1000;

        RedisService service = RedisUtils.getService(config.getScreenDataBase());

        if (system == 1) {
            //对空操作
        }else if (system == 2) {
            //水下操作

            if (type == 1) {
                //声呐
                String key = String.format(Constant.SCREEN_SENSORACCURACY_WATERTYPE_TARGETID,targetBatch);
                AccuracyData accuracyData = new AccuracyData();
                accuracyData.setTime((long) timeV);
                accuracyData.setDeflectionAngleAccuracy(Math.abs(sonarModelDir - sonarMsgDir));
                accuracyData.setDistanceAccuracy(Math.abs(sonarModelDis - sonarMsgDis));
                accuracyData.setType(String.valueOf(type));
                service.boundZSetOps(key).add(JsonUtils.serialize(accuracyData),accuracyData.getTime());
            }else if (type == 2) {
                //雷达
            }
        }
        log.info("收到水下报文<7>");
    }

    /**
     * 报文8解析
     */
    private void Msg8_WeaponStateAndDis_Struct(ByteBuf buf) {
        //1.对空 2.水下
        int system = buf.readInt();
        //目标批号
        int targetBatch = buf.readInt();
        //武器类型 1.声干扰器 2.助飞声诱饵 3.摇曳声诱饵 4.ATT
        int weaponType = buf.readInt() + 7;
        // 实体Id
        int weaponId = buf.readInt();
        //飞行状态 1.飞行中 2.已入水
        int weaponFlyState = buf.readInt();
        //战斗状态 1.正在发声 2.停止发声 3.起爆
        int weaponFightState = buf.readInt();
        //目标相对距离
        double distance = Double.valueOf(new DecimalFormat("#.00").format(buf.readDouble()));
        //显示时间
        double timeValue = buf.readDouble() * 1000;
        //时戳
        double timeStamp = buf.readDouble();
        RedisService service = RedisUtils.getService(config.getScreenDataBase());

        if (system == 1) {
            //对空
        }else if (system == 2) {
            //水下
            //处理任务通道的武器状态
            String key1 = Constant.SCREEN_TASKCHANNELSTATUS_WATERTYPE;
            String json1 = service.boundHashOps(key1).entries().get(String.valueOf(targetBatch));
            ScreenTctData screenTctData = JsonUtils.deserialize(json1, ScreenTctData.class);
            List<TctStatus> tctStatusList = screenTctData.getTctStatusList();
            String type1 = String.valueOf(weaponType + 1);
            Map<String, TctStatus> collect = tctStatusList.stream().collect(Collectors.toMap(TctStatus::getType, Function.identity(), (keyA, keyB) -> keyB));
            TctStatus tctStatus = new TctStatus();
            tctStatus.setType(type1);
            tctStatus.setTime((long) timeValue);
            tctStatus.setDistance(distance);
            if (weaponFlyState == 1 && weaponType == 9) {
                tctStatus.setName("声干扰:飞行");
            } else if (weaponFlyState == 1 && weaponType == 10) {
                tctStatus.setName("声诱饵:飞行");
            } else if(weaponFlyState == 2 && weaponType == 9 && weaponFightState == 1){
                tctStatus.setName("声干扰:发声");
            } else if(weaponFlyState == 2 && weaponType == 9 && weaponFightState == 2){
                tctStatus.setName("声干扰:停声");
            } else if(weaponFlyState == 2 && weaponType == 9 && weaponFightState == 3){
                tctStatus.setName("声干扰:起爆");
            } else if(weaponFlyState == 2 && weaponType == 10 && weaponFightState == 1){
                tctStatus.setName("声诱饵:发声");
            } else if(weaponFlyState == 2 && weaponType == 10 && weaponFightState == 2){
                tctStatus.setName("声诱饵:停声");
            } else if(weaponFlyState == 2 && weaponType == 10 && weaponFightState == 3){
                tctStatus.setName("声诱饵:起爆");
            }
            collect.put(type1,tctStatus);
            List<TctStatus> values = collect.values().stream().collect(Collectors.toList());
            screenTctData.setTctStatusList(values);
            service.boundHashOps(key1).put(String.valueOf(targetBatch), JsonUtils.serialize(screenTctData));

            log.info("收到水下报文<8>");
        }
    }
}
