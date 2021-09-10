package com.soul.weapon.service;

import com.soul.weapon.entity.PipeSelfCheck;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PipeSelfCheckService {

    PipeSelfCheck getById(String id);

    PipeSelfCheck getByName(String Name);

    List<PipeSelfCheck> getAll();

    String insert(@RequestBody PipeSelfCheck pipeSelfCheck);

    void update(@RequestBody PipeSelfCheck pipeSelfCheck);

    int deleteById(@PathVariable("id") String id);
    
}
