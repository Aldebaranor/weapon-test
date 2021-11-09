package com.soul.weapon.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.weapon.entity.PipeAdvice;
import com.soul.weapon.service.PipeAdviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@RestController
@RequestMapping("/unity/advice")
@RequiredArgsConstructor
public class PipeAdviceController {

    public final PipeAdviceService pipeAdviceService;

    @Api
    @GetMapping(value = "/{id}")
    public PipeAdvice getById(@PathVariable("id") String id)
    {
        return pipeAdviceService.getById(id);
    }

    @Api
    @GetMapping(value = "/list-all")
    public List<PipeAdvice> getAll()
    {
        return pipeAdviceService.getAll();
    }

    @Api
    @PostMapping
    public String inset(@RequestBody PipeAdvice pipeAdvice)
    {
        return pipeAdviceService.insert(pipeAdvice);
    }

    @Api
    @PutMapping
    public void update(@RequestBody PipeAdvice pipeAdvice) {
        pipeAdviceService.update(pipeAdvice);
    }

    /**
     * 主键删除
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return pipeAdviceService.deleteById(id);
    }

}
