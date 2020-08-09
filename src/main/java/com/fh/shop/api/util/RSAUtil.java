package com.fh.shop.api.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.io.UnsupportedEncodingException;

public class RSAUtil {
   public static final String PRIVATEKEY ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJtsAhyJQCbOXx/FB2y3TO5H79ak9SfF64p/mB1JZm9aJZjYJtHxyfkoHTEYR5sFtCgz4ye8Lm29nrSwRtFyAOtwrkClkUNW845PQM6B9Cn7CFkpO2cwbYItnH/qkW9cDBoR9m831CFawhSk/bO6PPWsgOXucJ3UAC2B29E69eSxAgMBAAECgYEAmjb9kVsOcHLtKBTUXSc0CmsOq6haKPDV2kLoqIagQXTtQU+YlVCY28TV0lUJF+T88bcqec7bMsCnrRoL6t530ZRLDSqPXQm9TKqjmJ4AC4/MfxpwmA5glOWCuz/1CuWI5Kk88FDlaBlri7nPUleByZ8hiU48/w4EPP0F6pDCPJECQQDrrp1Izj83/Q5DyGJ++JO21vRsCkeq/Lv4bf1EAfynIJ39Ypi+GIF6dHTR9hyqXDL2NCdPjV2W22oZmq9lJSanAkEAqNIXhaeiL4UEckQlBIHzVQa9McjJ/Lzd7in02n41pSxyzAZD/TbPU6LXyLgVIzsIuE2Pzjbc3O6M0q1pHJRc5wJBAOIVe2QgFZ0L7+cuDu5mXq0Cvy31HpL0Jw1F1bKrhUZ9j5FGR+fzciGaHYZcZVs3Xtu3ZA54OdNBYCJ3tNF+NfkCQEzZ5w9W8oQDX7TMxLU6mxUXzkS9jQXazITA4NAeKDma9F9gcs7who0iUmzzL9wr4ZpU5KQRYZXJmp3Fh1YVS/0CQAdHnqszAyua3n8R2w6LZjWysHy/gYq1KsT+iUMwJ0O4mLDiPH0hiTRpe33v6QHjL14PIH0ETwmQS5hj/cAlBlk=";
   public static final String PULICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbbAIciUAmzl8fxQdst0zuR+/WpPUnxeuKf5gdSWZvWiWY2CbR8cn5KB0xGEebBbQoM+MnvC5tvZ60sEbRcgDrcK5ApZFDVvOOT0DOgfQp+whZKTtnMG2CLZx/6pFvXAwaEfZvN9QhWsIUpP2zujz1rIDl7nCd1AAtgdvROvXksQIDAQAB";

   public static String decrypt(String data){
       RSA rsa = new RSA(PRIVATEKEY,PULICKEY);
       byte[] bytes = rsa.decryptFromBase64(data, KeyType.PrivateKey);
       try {
           String result = new String(bytes, "utf-8");
           return result;
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
           throw new RuntimeException(e);
       }

   }
}
