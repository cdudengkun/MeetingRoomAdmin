package com.cjack.meetingroomadmin.util;

import java.math.BigDecimal;

public class MathUtil {

    public static Integer divide( Integer i1, Integer i2) {
        if( !EmptyUtil.isNotEmpty( i1) || !EmptyUtil.isNotEmpty( i2)){
            return 0;
        }
        BigDecimal bigDecimal = new BigDecimal( i1);
        bigDecimal = bigDecimal.divide( new BigDecimal( i2));
        return bigDecimal.intValue();
    }

    public static void main(String[] args) {
        System.out.println( divide( 100, 10));
    }
}
