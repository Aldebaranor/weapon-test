package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.entity.SchemeDetail;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = SchemeDetail.NAME)
public interface SchemeDetailRepository extends AbstractRepositoryBase<SchemeDetail, String> {
    default int deleteBySchemeId(String schemeId) {
        return delete(SingleClause.equal("schemeId", schemeId));
    }
}
