package com.soul.fire.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.fire.condition.FireWeaponCondition;
import com.soul.fire.entity.FireWeapon;
import com.soul.fire.service.FireWeaponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/unity/weapon")
@RequiredArgsConstructor
public class FireWeaponController {
    
    private final FireWeaponService fireWeaponService;


    @Api
    @GetMapping(value = "/{id}")
    public FireWeapon getById(@PathVariable String id) {
        return fireWeaponService.getById(id);
    }
    @Api
    @PostMapping
    public String insert(@RequestBody FireWeapon fireWeapon) {
        return fireWeaponService.insert(fireWeapon);
    }
    @Api
    @PutMapping
    public void update(@RequestBody FireWeapon fireWeapon) {
        fireWeaponService.update(fireWeapon);
    }
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return fireWeaponService.deleteById(id);
    }

    /**
     * @Author: Shizuan
     * @Date: 2022/3/9 16:19
     * @Description: 根据条件获取全部武器，并根据创建时间或修改时间进行排序
     * @params:[condition] 条件
     * @return: List 武器
     **/
    @Api
    @PostMapping("/list")
    public List<FireWeapon> list(@RequestBody FireWeaponCondition condition) {
        return fireWeaponService.list(condition);
    }

}
