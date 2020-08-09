package com.fh.shop.api;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

public class RSAtest {

    @Test
    public void test1(){
        RSA rsa = new RSA();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();//私钥
        String publicKeyBase64 = rsa.getPublicKeyBase64();//公钥
        System.out.println("私钥："+privateKeyBase64);
        System.out.println("公钥："+publicKeyBase64);
    }

    @Test
    public void test2() throws UnsupportedEncodingException {
        String privateKey ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJtsAhyJQCbOXx/FB2y3TO5H79ak9SfF64p/mB1JZm9aJZjYJtHxyfkoHTEYR5sFtCgz4ye8Lm29nrSwRtFyAOtwrkClkUNW845PQM6B9Cn7CFkpO2cwbYItnH/qkW9cDBoR9m831CFawhSk/bO6PPWsgOXucJ3UAC2B29E69eSxAgMBAAECgYEAmjb9kVsOcHLtKBTUXSc0CmsOq6haKPDV2kLoqIagQXTtQU+YlVCY28TV0lUJF+T88bcqec7bMsCnrRoL6t530ZRLDSqPXQm9TKqjmJ4AC4/MfxpwmA5glOWCuz/1CuWI5Kk88FDlaBlri7nPUleByZ8hiU48/w4EPP0F6pDCPJECQQDrrp1Izj83/Q5DyGJ++JO21vRsCkeq/Lv4bf1EAfynIJ39Ypi+GIF6dHTR9hyqXDL2NCdPjV2W22oZmq9lJSanAkEAqNIXhaeiL4UEckQlBIHzVQa9McjJ/Lzd7in02n41pSxyzAZD/TbPU6LXyLgVIzsIuE2Pzjbc3O6M0q1pHJRc5wJBAOIVe2QgFZ0L7+cuDu5mXq0Cvy31HpL0Jw1F1bKrhUZ9j5FGR+fzciGaHYZcZVs3Xtu3ZA54OdNBYCJ3tNF+NfkCQEzZ5w9W8oQDX7TMxLU6mxUXzkS9jQXazITA4NAeKDma9F9gcs7who0iUmzzL9wr4ZpU5KQRYZXJmp3Fh1YVS/0CQAdHnqszAyua3n8R2w6LZjWysHy/gYq1KsT+iUMwJ0O4mLDiPH0hiTRpe33v6QHjL14PIH0ETwmQS5hj/cAlBlk=";
        String pulicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbbAIciUAmzl8fxQdst0zuR+/WpPUnxeuKf5gdSWZvWiWY2CbR8cn5KB0xGEebBbQoM+MnvC5tvZ60sEbRcgDrcK5ApZFDVvOOT0DOgfQp+whZKTtnMG2CLZx/6pFvXAwaEfZvN9QhWsIUpP2zujz1rIDl7nCd1AAtgdvROvXksQIDAQAB";
        RSA rsa = new RSA(privateKey, pulicKey);
        String su = rsa.encryptBase64("su", KeyType.PublicKey);
        System.out.println("加密后：公钥："+su);
        byte[] bytes = rsa.decryptFromBase64(su, KeyType.PrivateKey);
        String s = new String(bytes, "utf-8");
        System.out.println(s);
    }
}
