package com.example.other.ttms.utils;

import android.util.Log;

import com.example.other.ttms.beans.AreaInfo;
import com.example.other.ttms.beans.CityInfo;
import com.example.other.ttms.beans.UserInfo;
import com.example.other.ttms.http.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class LocationUtil {

    public static List<AreaInfo> areas;

    /**
     * 获取省的信息
     *
     * @return
     */
    public static List<String> getProvince() {
        return null;
    }

    /**
     * 获取市级信息
     * @return
     */
    public static CityInfo getCity() {
        CityInfo info=new CityInfo();
        info.setId(294);
        info.setCity("西安");
        return info;
    }

    /**
     * 获取区级信息
     *
     * @param cituId
     * @return
     */

    public static void getArea(int cituId) {
        final Request request = OkHttpUtils.INSTANCE.getArea(cituId);
        Call call = OkHttpUtils.INSTANCE.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("获取区级信息失败", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    areas = new ArrayList<>();
                    String result = JsonUtil.INSTANCE.parseData(response.body().string());
                    if (result!=null){
                        areas=JsonUtil.INSTANCE.parseArray(result,AreaInfo.class);
                    }
                }

            }
        });
    }

}
