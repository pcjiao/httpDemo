package com.dhb.http.ssotest;

import com.alibaba.fastjson.JSON;
import com.dhb.http.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThirdLogin {

    private static final String url = "http://passport.newdhb.com/login/thirdlogin";
    private static final String appSecret = "456";
    private static final String rsaKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEizWHAPMY0busC7vvMzxXo/Bk" +
            "1ISkQt54D6/eN/ijozt+ZWG/5zPG7hSs6w+Thg2yNTVXGYdNhKDuYTu8IaU/Ia8p" +
            "C36d9uLdeTxW5IiRipAr82F+ZMalgpRkbFoqJeoE9XC8rJP4BMqhvz5+ev2j3Qxh" +
            "ZuurXFfmsePDSFlOkwIDAQAB";

    public static void main(String[] args) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("username", "13700000004");
        param.put("password", "69595245554c56565a4c5a414e694b4948756f4c6338353867542f5568337056374174464464624853516f492b6d65617a4b414e4466726e7a5a4e7139756c48452f5469424474447733397168516b4141594d4b46314d51376b6143677a366d5050527169786c4c467a2f2f46685568766d44562b6b2b5031545865416d49372f34566b7a41622f4d2b4d486b3277594e69434637624c67623869563842557077795847464d566d547a733d");
        param.put("password", EncodeUtil.byteToHexStr(RSAUtil.encryptByPublicKey("123456".getBytes("UTF-8"), rsaKey)));
        param.put("partner_id", "1");
        param.put("appkey", "123");
        param.put("timestamp", System.currentTimeMillis() + "");

        System.out.println(param.get("password"));

        String json = JSON.toJSONString(MapUtils.sortMap(param));
        String sign = MD5Util.encrypt(appSecret + json + appSecret);

        param.put("sign", sign);

        String response = HttpClientUtil.postJson(url, JSON.toJSONString(param), "UTF-8");
        System.out.println(response);
    }

}
