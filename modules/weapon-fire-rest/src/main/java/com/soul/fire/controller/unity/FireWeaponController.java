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

    /**
     * 主键删除
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return fireWeaponService.deleteById(id);
    }

    @Api
    @PostMapping("/list")
    public List<FireWeapon> list(@RequestBody FireWeaponCondition condition) {
        return fireWeaponService.list(condition);
    }

}
