package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.entity.Person;
import com.soul.fregata.entity.ExperimentTeam;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = ExperimentTeam.NAME)
public interface ExperimentTeamRepository extends AbstractRepositoryBase<ExperimentTeam, String> {

    @Select("SELECT * FROM com_person WHERE id NOT IN (SELECT personId FROM fregata_experiment_team WHERE experimentId = #{experimentId})")
    List<Person> getByExperimentId(@Param("experimentId") String experimentId);
}
