package com.tuhanbao.base.util.objutil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {
    /**
     * keys 和 values只会取最小长度进行组装map
     * @param keys
     * @param values
     * @return
     */
    public static <K extends Object, V extends Object> Map<K, V> generateMap(K[] keys, V[] values) {
        Map<K, V> map = new HashMap<K, V>();
        if (keys != null && values != null) {
            int length = keys.length > values.length ? values.length : keys.length;
            
            for (int i = 0; i < length; i++) {
                map.put(keys[i], values[i]);
            }
        }
        return map;
    }
    
    /**
     * keys 和 values只会取最小长度进行组装map
     * @param keys
     * @param values
     * @return
     */
    public static <K extends Object, V extends Object> Map<K, V> generateMap(List<K> keys, List<V> values) {
        Map<K, V> map = new HashMap<K, V>();
        if (keys != null && values != null) {
            int length = keys.size() > values.size() ? values.size() : keys.size();
            
            for (int i = 0; i < length; i++) {
                map.put(keys.get(i), values.get(i));
            }
        }
        return map;
    }
}
