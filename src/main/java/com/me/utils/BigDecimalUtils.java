package com.me.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    /**
     * 将 BigDecimal 数值保留一位小数，并进行四舍五入
     * @param value 需要处理的数值，如果为 null 则返回 null
     * @return 处理后的 BigDecimal 数值，保留一位小数
     */
    public static BigDecimal roundToOneDecimal(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(1, RoundingMode.HALF_UP);
    }

    /**
     * 将 BigDecimal 数值保留一位小数，并进行指定的舍入模式处理
     * @param value 需要处理的数值，如果为 null 则返回 null
     * @param roundingMode 指定的舍入模式，如 RoundingMode.DOWN、RoundingMode.UP 等
     * @return 处理后的 BigDecimal 数值，保留一位小数
     */
    public static BigDecimal roundToOneDecimal(BigDecimal value, RoundingMode roundingMode) {
        if (value == null || roundingMode == null) {
            return null;
        }
        return value.setScale(1, roundingMode);
    }
}