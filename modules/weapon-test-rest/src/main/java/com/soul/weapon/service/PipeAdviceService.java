package com.soul.weapon.service;

import com.soul.weapon.entity.PipeAdvice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PipeAdviceService {

    PipeAdvice getById(String id);

    List<PipeAdvice> getAll();

    String insert(@RequestBody PipeAdvice pipeAdvice);

    void update(@RequestBody PipeAdvice pipeAdvice);

    int deleteById(@PathVariable("id") String id);
    
}
