package com.soul.weapon.service;



import com.egova.entity.DictionaryItem;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/2 15:35
 */
public interface DictionaryItemService {
    /**
     * 根据code查询通道测试text
     * @param code
     * @return
     */
    List<DictionaryItem> getByCode(String code);
}
