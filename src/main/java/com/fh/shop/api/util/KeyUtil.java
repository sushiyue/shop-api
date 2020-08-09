package com.fh.shop.api.util;

public class KeyUtil {
    public static final int sconds = 5*60;

    public static final int Login_sconds = 30*60;

    public static String buidMember(String uuid,Long id){
        return "member:"+uuid+":"+id;
    }

    public static String buidCartKey(Long id) {
        return "cartId:"+id;
    }

    public static String buidStockless(Long memberId) {
        return "order:stock:less"+memberId;
    }

    public static String buidOrderKey(Long memberId) {
        return "order:"+memberId;
    }


    public static String buidPayLogKey(Long memberId) {
        return "pay:log:"+memberId;
    }

    public static String buidOrderError(Long memberId) {
        return "order:error:"+memberId;
    }
}
