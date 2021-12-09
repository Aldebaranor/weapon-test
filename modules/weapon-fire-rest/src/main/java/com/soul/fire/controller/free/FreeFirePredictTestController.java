//package com.soul.fire.controller.free;
//
//import com.soul.fire.service.FireConflictPredictService;
//import com.soul.weapon.model.ConflictReport;
//import com.soul.weapon.model.ScenariosInfo;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @Author: XinLai
// * @Date: 2021/11/9 16:46
// */
//
//@Slf4j
//@RestController
//@RequestMapping("/free/test")
//@RequiredArgsConstructor
//public class FreeFirePredictTestController {
//
//    private final FireConflictPredictService fireConflictPredict;
//
//    @GetMapping("/predict")
//    public ConflictReport predictTest(){
//
//        ScenariosInfo scenariosA = new ScenariosInfo();
//        ScenariosInfo scenariosB = new ScenariosInfo();
//        scenariosA.setEquipmentId("1");
//        scenariosA.setEquipmentMode("1");
//        scenariosA.setEquipmentTypeId("2");
//        scenariosA.setBeginTime(100L);
//        scenariosA.setLaunchAzimuth(0.35F);
//        scenariosA.setLaunchPitchAngle(0.5F);
//
//
//        scenariosB.setEquipmentId("2");
//        scenariosB.setEquipmentMode("2");
//        scenariosB.setEquipmentTypeId("3");
//        scenariosB.setBeginTime(102L);
//        scenariosB.setLaunchAzimuth(0.32F);
//        scenariosB.setLaunchPitchAngle(0.52F);
//
//        return fireConflictPredict.conflictPredict(scenariosA,scenariosB);
//
//    }
//}
