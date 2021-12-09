//package com.soul.fire.controller.free;
//
//import com.egova.web.annotation.Api;
//import com.soul.fire.condition.FireTaskCondition;
//import com.soul.fire.entity.FireTask;
//import com.soul.fire.service.FireTaskService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.parameters.P;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.concurrent.locks.Condition;
//
//@Slf4j
//@RestController
//@RequestMapping("/free/task")
//@RequiredArgsConstructor
//public class FreeFireTaskController {
//
//    private final FireTaskService fireTaskService;
//
//    @Api
//    @GetMapping("/current")
//    public FireTask getCurTask() {
//        return fireTaskService.getCurTask();
//    }
//
//    // @Api
//    // @PatchMapping("/cur-task/{running-status}")
//    // public void updateTask(@PathVariable("running-status") boolean running) {
//    //    fireTaskService.updateTask(running);
//    // }
//
//    @Api
//    @PutMapping("/current")
//    public void updateTask(@RequestBody FireTask task) {
//        fireTaskService.update(task);
//    }
//
//    @Api
//    @PostMapping("/current")
//    public String insertTask(@RequestBody FireTask task) {
//        return fireTaskService.insert(task);
//    }
//
//    @Api
//    @PostMapping("/list")
//    public List<FireTask> list(@RequestBody FireTaskCondition con) {
//        return fireTaskService.list(con);
//    }
//
//}
