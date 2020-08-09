package com.fh.shop.api.common;

public class SystemConstant {
    public static String CURR_MEMBER = "user";
    public static int PROFUCT_STATUS = 1;

    public interface order{
       int WEIT_PAY =10;
        int PAY_SUCCESS =20;
        int SEND_GOODS=30;
    }
    public interface pay_status{
        int WEIT_PAY =10;
        int PAY_SUCCESS =20;
    }
}
