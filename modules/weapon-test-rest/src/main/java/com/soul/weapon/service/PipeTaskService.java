package com.soul.weapon.service;

import com.soul.weapon.entity.PipeTask;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PipeTaskService {

    PipeTask getById(String id);

    List<PipeTask> getAll();

    String insert(@RequestBody PipeTask pipeTask);

    void update(@RequestBody PipeTask pipeTask);

    int deleteById(@PathVariable("id") String id);
}
