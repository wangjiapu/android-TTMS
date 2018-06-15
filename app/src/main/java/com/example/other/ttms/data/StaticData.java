package com.example.other.ttms.data;


import com.example.other.ttms.beans.OrderInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个里面的数据本应该是从本地数据库，或者服务器下发的
 * 但是，后台压力太大 ，就做一次性数据处理
 */
public class StaticData {

    public static List<OrderInfo> unfinished=new ArrayList<>();
    public static List<OrderInfo> finished=new ArrayList<>();
    public static List<P> p=new ArrayList<>();
}
