package com.soul.weapon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.sql.Timestamp;

/**
 * @Author: XinLai
 * @Date: 2021/9/16 11:35
 */
@Data
public class MissileWeapon {

    private String id;

    private Boolean selfCheck;

    private String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp collectTime;
}
