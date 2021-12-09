//package com.soul.weapon.algorithm.impl;
//
//import com.egova.json.utils.JsonUtils;
//import com.soul.weapon.algorithm.AlgoFactoryContext;
//import com.soul.weapon.algorithm.Algorithm;
//import com.soul.weapon.algorithm.annotation.WeaponAlgorithm;
//import com.soul.weapon.model.MissileWeapon;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.sql.Timestamp;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @Author: nash5
// * @Date: 2021/9/12 16:18
// */
//@Service(value = "airMissilePipeTest")
//@Slf4j
//public class AirMissilePipeTest implements Algorithm {
//
//    @Override
//    public String exAlgo(String input) {
//        List<MissileWeapon> missileWeapon = JsonUtils.deserialize(input,MissileWeapon.class);
//
//        Map res = missileWeapon.stream().collect(Collectors.groupingBy(MissileWeapon::getType));
//        res.forEach((k,v)-> System.out.println(k+" : "+v));
//
//        for(int i=0;i<missileWeapon.size();i++)
//        {
//            String id = missileWeapon.get(i).getId();
//            Timestamp timestamp= missileWeapon.get(i).getCollectTime();
//            String type = missileWeapon.get(i).getType();
//            Boolean selfCheck = missileWeapon.get(i).getSelfCheck();
//            System.out.println("id: " + id +" timestamp: " +timestamp+" "+" type: " + type + " selfCheck: " + selfCheck);
//        }
//        log.info("airMissilePipeTest algo executing!");
//        return input;
//    }
//}
