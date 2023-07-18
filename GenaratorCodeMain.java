import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wWX497819
 */
public class GenaratorCodeMain {
    public static void main(String args[]) {
//        WebStartUtil.start();
//        Context context = new AGCContext(ConfigManager.getConfig("solid"));
//        context.addFilterByName(ProjectConfig.FILTERS);
//        context.start();

        List<Map<String, Object>> list = new ArrayList<>();
        //{"p":"p1","m":"m1","l":"list1"},{"p":"p1","m":"m2","l":"list2"},{"p":"p1","m":"m2","l":"list3"},{"p":"p2","m":"m3","l":"list4"}"
        Map<String, Object> map1 = JSONObject.parseObject("{\"p\":\"p1\",\"m\":\"m1\",\"l\":\"list1\"}");
        Map<String, Object> map2 = JSONObject.parseObject("{\"p\":\"p1\",\"m\":\"m2\",\"l\":\"list2\"}");
        Map<String, Object> map3 = JSONObject.parseObject("{\"p\":\"p1\",\"m\":\"m2\",\"l\":\"list3\"}");
        Map<String, Object> map4 = JSONObject.parseObject("{\"p\":\"p2\",\"m\":\"m3\",\"l\":\"list4\"}");
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        // 先分组
        List<Map<String, Object>> result = meger(list, 0, new String[]{"p", "m"}, new String[]{"mlist", "l"}, new FinalHandler() {
            @Override
            public Object handle(List<Map<String, Object>> value) {
                // 最后一步我不知道你啥意思，是要字符串加起来吗，还是说是两个list变量合并，这个一般自己根据业务来写就好，我先写个简单的
                StringBuilder sb = new StringBuilder();
                value.stream().forEach(item -> sb.append(item.get("l").toString()));
                return sb.toString();
            }
        });

        System.out.println(JSON.toJSONString(result));
    }


    /**
     * 按多个字段分组后返回
     * @param index 当前分组的字段下标
     */
    private static List<Map<String, Object>> meger(List<Map<String, Object>> list, int index, String[] megerCols, String[] newColNames, FinalHandler handler) {
        Map<Object, List<Map<String, Object>>> map = new HashMap<>();

        String megerCol = megerCols[index];
        String newColName = newColNames[index];
        list.stream().forEach(item -> {
            Object key = item.remove(megerCol);
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(item);
        });

        List<Map<String, Object>> result = new ArrayList<>();
        map.entrySet().stream().forEach(item -> {
            Map<String, Object> newMap = new HashMap<>();
            newMap.put(megerCol, item.getKey());
            if (index + 1 == megerCols.length) {
                newMap.put(newColName, handler.handle(item.getValue()));
            } else {
                newMap.put(newColName, meger(item.getValue(), index + 1, megerCols, newColNames, handler));
            }
            result.add(newMap);
        });
        return result;
    }

    private static interface FinalHandler {
        Object handle(List<Map<String, Object>> value);
    }
}
