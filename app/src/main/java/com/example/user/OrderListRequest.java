package com.example.user;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OrderListRequest extends StringRequest {

    final static private String URL = "http://whdvm1.dothome.co.kr/Orderlist.php";
    private Map<String, String> map;

    public OrderListRequest(String userID, String resName, String menu, int price, String date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("resName", resName);
        map.put("menu", menu);
        map.put("price", price + "");
        map.put("date", date);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
