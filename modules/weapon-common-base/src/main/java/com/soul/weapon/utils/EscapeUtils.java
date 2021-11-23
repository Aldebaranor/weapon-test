package com.soul.weapon.utils;

import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * '转义
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
public final class EscapeUtils {

    private EscapeUtils() {
        throw new IllegalStateException("😄");
    }

    public static String sql(String value) {
        return Optional.ofNullable(value)
                .map(e -> StringUtils.replace(e, "'", "''"))
                .orElse(null);
    }

}
