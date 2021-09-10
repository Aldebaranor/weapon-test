package com.soul.weapon.controller.free;

import com.egova.web.annotation.Api;
import com.soul.weapon.entity.PipeSelfCheck;
import com.soul.weapon.service.PipeSelfCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/free/self-check")
@RequiredArgsConstructor
public class FreePipeSelfCheckController {

    private final PipeSelfCheckService pipeSelfCheckService;

    @Api
    @GetMapping(value = "/{id}")
    public PipeSelfCheck getById(@PathVariable("id") String id)
    {
        return pipeSelfCheckService.getById(id);
    }

    @Api
    @GetMapping(value = "/list-all")
    public List<PipeSelfCheck> getAll()
    {
        return pipeSelfCheckService.getAll();
    }

    @Api
    @PostMapping
    public String inset(@RequestBody PipeSelfCheck pipeSelfCheck)
    {
        return pipeSelfCheckService.insert(pipeSelfCheck);
    }

    @Api
    @PutMapping
    public void update(@RequestBody PipeSelfCheck pipeSelfCheck) {
        pipeSelfCheckService.update(pipeSelfCheck);
    }

    /**
     * 主键删除
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return pipeSelfCheckService.deleteById(id);
    }
}
