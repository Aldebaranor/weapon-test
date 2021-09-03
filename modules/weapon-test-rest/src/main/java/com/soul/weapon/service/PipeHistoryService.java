package com.soul.weapon.service;

import com.soul.weapon.entity.PipeHistory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PipeHistoryService {

    PipeHistory getById(String id);

    List<PipeHistory> getAll();

    String insert(@RequestBody PipeHistory pipeHistory);

    void update(@RequestBody PipeHistory pipeHistory);

    int deleteById(@PathVariable("id") String id);
}
