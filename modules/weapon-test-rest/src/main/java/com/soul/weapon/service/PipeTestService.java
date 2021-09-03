package com.soul.weapon.service;

import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.entity.PipeTest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PipeTestService {

    PipeTest getById(String id);

    List<PipeTest> getAll();

    String insert(@RequestBody PipeTest pipeTest);

    void update(@RequestBody PipeTest pipeTest);

    int deleteById(@PathVariable("id") String id);

}
