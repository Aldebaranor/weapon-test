package com.soul.weapon.algorithm.annotation;

/**
 * @Author: nash5
 * @Date: 2021/9/12 16:10
 */

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WeaponAlgorithm {
    String algoName () default "";
}
