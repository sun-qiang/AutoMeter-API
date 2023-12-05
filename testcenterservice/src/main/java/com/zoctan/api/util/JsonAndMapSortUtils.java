package com.zoctan.api.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import sun.misc.ASCIICaseInsensitiveComparator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonAndMapSortUtils {

    /**
     * map排序
     * @param map
     * @param keySort
     * @param <k>
     * @param <v>
     * @return
     */
    public static <k,v> List mapByKeyToSort(Map<k,v> map , final Comparator keySort){
        List<Map.Entry<k,v>> entryList = new ArrayList<Map.Entry<k, v>>(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<k, v>>() {
            public int compare(Map.Entry<k, v> o1, Map.Entry<k, v> o2) {
                return keySort.compare(o1.getKey(),o2.getKey());
            }
        });
        //return (Map<k,v>)entryList.stream().collect(Collectors.toMap(Map.Entry<k,v>::getKey, Function.identity(), (key1, key2) -> key2));
        Map<String,String> afterToSortMap = new HashMap<>();
        /*for (Map.Entry<k,v> m : entryList){
            System.out.println(m.getKey()+"===>"+m.getValue());
        }*/
        System.out.println("排序=====");
        entryList.forEach(m->{
            System.out.println(m.getKey()+"===>"+m.getValue());
        });
        return entryList;
    }

    /**
     * JSONArray排序
     * @param jsonArray
     * @param fildName
     * @param isAsc
     * @return
     */
    public static JSONArray jsonArrayToSort(JSONArray jsonArray,final String fildName,final boolean isAsc){
        JSONArray afterSortJsonArray = new JSONArray();
        List<JSONObject> objectList = new ArrayList<JSONObject>();
        jsonArray.forEach(obj ->{
            objectList.add((JSONObject)obj);
        });
        Collections.sort(objectList, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                String fildValueA = o1.getString(fildName);
                String fildValueB = o2.getString(fildName);
                if (isAsc)
                    return fildValueA.compareTo(fildValueB);
                return fildValueB.compareTo(fildValueA);
            }
        });
        objectList.forEach(obj->{
            afterSortJsonArray.add(obj);
        });
        return afterSortJsonArray;
    }

    /**
     *准备map测试数据
     */
    public static Map<String,String> getMapData(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("key1","麦兜");
        map.put("key3","贝塔");
        map.put("key5","酥妮");
        map.put("key2","小H");
        map.put("key4","小O");
        return map;
    }
    /**
     *准备json测试数据
     */
    public static JSONArray getJsonArrayData(){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("userId","1001");
        jsonObject1.put("name","麦兜");
        jsonArray.add(jsonObject1);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("userId","1003");
        jsonObject3.put("name","酥妮");
        jsonArray.add(jsonObject3);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("userId","1002");
        jsonObject2.put("name","贝塔");
        jsonArray.add(jsonObject2);

        return jsonArray;
    }

    public static void main(String[] args) {
        Map<String,String> map = JsonAndMapSortUtils.getMapData();
        JSONArray jsonArray = JsonAndMapSortUtils.getJsonArrayData();
        List afterSortMap = JsonAndMapSortUtils.mapByKeyToSort(map,new ASCIICaseInsensitiveComparator());

        JSONArray afterSortJsonArray_isAsc = JsonAndMapSortUtils.jsonArrayToSort(jsonArray,"userId",true);
        JSONArray afterSortJsonArray_noAsc = JsonAndMapSortUtils.jsonArrayToSort(jsonArray,"userId",false);

        System.out.println("map排序前："+map);
        System.out.println("map排序后："+afterSortMap+"\n");

        System.out.println("JsonArray排序前："+jsonArray);
        System.out.println("JsonArray排序后==》升序："+afterSortJsonArray_isAsc);
        System.out.println("JsonArray排序后==》降序："+afterSortJsonArray_noAsc);
    }


}
