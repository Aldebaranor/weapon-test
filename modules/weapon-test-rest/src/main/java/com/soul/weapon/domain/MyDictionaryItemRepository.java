package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.entity.DictionaryItem;
import com.egova.generic.domain.DictionaryItemRepository;
import com.soul.weapon.entity.PipeTest;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MyDictionaryItemRepository extends AbstractRepositoryBase<DictionaryItem, String> {

    @Select("<script>"+
            "select * from com_dictionary_item where code in"+
            "<foreach collection='pipeTestsList' open='(' item='pipeTest' separator=',' close=')'>#{pipeTest.code}</foreach>"+
            "</script>")
    List<DictionaryItem> findByCodes(@Param("pipeTestsList") List<PipeTest> pipeTestsList);
}
