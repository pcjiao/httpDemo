package com.dhb.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpDemo {

    public static String dhbPost(String a, String c, String val) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("c", c);
        param.put("a", a);
        param.put("val", val);
        String response = HttpClientUtil.post("http://feature-1.newdhb.com/api.php", param);
        return response;
    }

    public static void main(String[] args) throws Exception {
        //String response = HttpClientUtil.get("https://www.baidu.com?name=12");
        //http://192.168.0.106/ndhb/www/api.shtml?=DingHuo&action=goodsContent&val={"skey":"00796a2e531674257dbd2e7b585b2d85","goods_id":"60"}
        Map<String, Object> valMap = new HashMap<>();
        valMap.put("accounts_name", "13700000013");
        valMap.put("accounts_pass", "dhb168");
        //valMap.put("company_id", 997);
        valMap.put("source_device", "weixin");
        valMap.put("mac_device", "123123");
        valMap.put("action", "noSkey");

        System.out.print(dhbPost("getSkeyValue", "DingHuo", JSON.toJSONString(valMap)));
    }

}
