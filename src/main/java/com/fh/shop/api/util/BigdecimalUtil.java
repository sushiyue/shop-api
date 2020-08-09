package com.fh.shop.api.util;

import java.math.BigDecimal;

public class BigdecimalUtil  {
    public static BigDecimal mull(String b1, String b2){
        BigDecimal bigDecimal = new BigDecimal(b1);
        BigDecimal bigDecima2 = new BigDecimal(b2);
        return bigDecimal.multiply(bigDecima2).setScale(2);
    }

    public static BigDecimal add(String b1, String b2){
        BigDecimal bigDecimal = new BigDecimal(b1);
        BigDecimal bigDecima2 = new BigDecimal(b2);
        return bigDecimal.add(bigDecima2).setScale(2);
    }
}
