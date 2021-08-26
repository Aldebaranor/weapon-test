package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.Task;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = Task.NAME)
public interface TaskRepository extends AbstractRepositoryBase<Task, String> {

}
