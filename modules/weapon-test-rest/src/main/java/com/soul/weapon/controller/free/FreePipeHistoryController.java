package com.soul.weapon.controller.free;

import com.egova.web.annotation.Api;
import com.soul.weapon.entity.PipeHistory;
import com.soul.weapon.service.PipeHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@RestController
@RequestMapping("/free/history")
@RequiredArgsConstructor
public class FreePipeHistoryController {

    @Autowired
    private final PipeHistoryService pipeHistoryService;

    @Api
    @GetMapping(value = "/{id}")
    public PipeHistory getById(@PathVariable("id") String id)
    {
        return pipeHistoryService.getById(id);
    }

    @Api
    @GetMapping(value = "/list-all")
    public List<PipeHistory> getAll()
    {
        return pipeHistoryService.getAll();
    }

    @Api
    @PostMapping
    public String inset(@RequestBody PipeHistory pipeHistory)
    {
        return pipeHistoryService.insert(pipeHistory);
    }

    @Api
    @PutMapping
    public void update(@RequestBody PipeHistory pipeHistory) {
        pipeHistoryService.update(pipeHistory);
    }

    /**
     * 主键删除
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return pipeHistoryService.deleteById(id);
    }

    @Api
    @GetMapping(value = "/type/{type}")
    public List<PipeHistory> getByType(@PathVariable String type)
    {
        return pipeHistoryService.getByType(type);
    }
}
