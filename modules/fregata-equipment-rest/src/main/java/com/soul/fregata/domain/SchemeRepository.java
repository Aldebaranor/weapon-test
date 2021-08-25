package com.soul.fregata.domain;

import com.egova.data.cascade.annotation.CascadeClause;
import com.egova.data.cascade.annotation.CascadeQuery;
import com.egova.data.cascade.annotation.CascadeQuerys;
import com.egova.data.service.AbstractRepositoryBase;
import com.egova.persistent.ClauseBuilder;
import com.flagwind.commons.StringUtils;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.condition.SchemeCondition;
import com.soul.fregata.entity.Scheme;
import com.soul.fregata.entity.SchemeDetail;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;
import java.util.Optional;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = Scheme.NAME)
public interface SchemeRepository extends AbstractRepositoryBase<Scheme, String> {


    @CascadeQuery(
            name = "details", field = "id", targetEntity = SchemeDetail.class, mappedBy = "schemeId"
    )
    default List<Scheme> equipmentId(String equipmentId) {
        ClauseBuilder clauseBuilder = ClauseBuilder.and().add(SingleClause.equal("disabled", 0));
        Optional.ofNullable(equipmentId).ifPresent(s -> {
            clauseBuilder.add(SingleClause.equal("equipmentId", equipmentId));
        });
        return this.seek(clauseBuilder.toClause());
    }
}
