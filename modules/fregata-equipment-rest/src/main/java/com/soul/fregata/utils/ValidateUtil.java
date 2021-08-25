package com.soul.fregata.utils;

import com.alibaba.fastjson.JSON;
import com.egova.exception.ExceptionUtils;
import com.soul.fregata.entity.Equipment;
import com.soul.fregata.entity.EquipmentAsset;
import com.soul.fregata.entity.Scheme;
import com.soul.fregata.entity.SchemeDetail;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: zwl
 * @Description:
 * @Date:Create: at 2020/10/27 19:13
 **/
public final class ValidateUtil {

    private ValidateUtil() {
        throw new IllegalStateException("Utils");
    }

    public static void validate(Equipment entity) {
        if (entity == null) {
            throw ExceptionUtils.api("装备数据不能为空");
        }
        if (StringUtils.isBlank(entity.getName())) {
            throw ExceptionUtils.api("装备名称不能为空");
        }
        if (entity.getCategoryType() == null || StringUtils.isBlank(entity.getCategoryType().getValue())) {
            throw ExceptionUtils.api("装备平台不能为空");
        }
    }

    public static void validate(EquipmentAsset entity) {
        if (entity == null) {
            throw ExceptionUtils.api("装备参数数据不能为空");
        }
        if (entity.getType() == null) {
            throw ExceptionUtils.api("装备参数类型不能为空");
        }
        if (entity.getEquipmentId() == null) {
            throw ExceptionUtils.api("装备编号不能为空");
        }
        if (StringUtils.isNotBlank(entity.getContent())) {
            boolean valid = JSON.isValid(entity.getContent());
            if (!valid) {
                throw ExceptionUtils.api("装备参数数据格式不正确！");
            }
        }
    }

    public static void validate(Scheme entity) {
        if (entity == null) {
            throw ExceptionUtils.api("方案数据不能为空");
        }
        if (StringUtils.isBlank(entity.getName())) {
            throw ExceptionUtils.api("方案名称不能为空");
        }
    }

    public static void validate(SchemeDetail schemeDetail) {
        if (schemeDetail == null) {
            throw ExceptionUtils.api("方案装备数据不能为空");
        }
        if (StringUtils.isBlank(schemeDetail.getEquipmentId())) {
            throw ExceptionUtils.api("方案装备编号不能为空");
        }
        if (schemeDetail.getCount() == null || schemeDetail.getCount() < 0) {
            throw ExceptionUtils.api("方案装备数量错误");
        }
    }
}
