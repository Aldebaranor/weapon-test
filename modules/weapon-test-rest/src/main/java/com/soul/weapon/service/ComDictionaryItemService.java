package com.soul.weapon.service;

import com.soul.weapon.entity.ComDictionaryItem;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/2 15:35
 */
public interface ComDictionaryItemService {
    /**
     * 根据code查询通道测试text
     * @param code
     * @return
     */
    List<ComDictionaryItem> getByCode(String code);
}
