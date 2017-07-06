package com.tuhanbao.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class KeyValueBean implements Cloneable
{
    private static final String EQUAL = Constants.EQUAL;
    
    private Map<String, String> map;
    
    private String propertyGap;
    
    private String keyValueGap;
    
    private boolean isAutoTrim = true; 
    
    public KeyValueBean()
    {
        this(Constants.EMPTY);
    }
    
    public KeyValueBean(String s)
    {
        this(s, Constants.SEMICOLON, EQUAL);
    }
    
    public KeyValueBean(String s, String propertyGap, String keyValueGap)
    {
        this(s, propertyGap, keyValueGap, true);
    }
    
    public KeyValueBean(String s, String propertyGap, String keyValueGap, boolean isAutoTrim)
    {
        super();
        this.propertyGap = propertyGap;
        this.keyValueGap = keyValueGap;
        this.isAutoTrim = isAutoTrim;
        init(s);
    }

    public String get(String key)
    {
        if (map == null)
        {
            return null;
        }
        return map.get(key);
    }
    
    public Map<String, String> getProperties()
    {
        return map;
    }
    
    public void put(String key, String value)
    {
        if (map == null)
        {
            map = new HashMap<String, String>();
        }
        if (value == null) value = Constants.EMPTY;
        
        if (isAutoTrim)
        {
            map.put(key.trim(), value.trim());
        }
        else
        {
            map.put(key, value);
        }
    }
    
    /**
     * 为空抛出异常
     * @param s
     */
    private void init(String s)
    {
        String[] arrays = s.split(propertyGap);
        for (String array : arrays)
        {
            if (isNote(array))
            {
                continue;
            }
            int index = array.indexOf(keyValueGap);
            if (index != -1)
            {
                //处理换行符
                String value = array.substring(index + keyValueGap.length(), array.length());
                value = value.replace("\\n", Constants.ENTER);
                put(array.substring(0, index), value);
            }
            else
            {
                put(array, Constants.EMPTY);
            }
        }
    }
    
    protected boolean isNote(String s)
    {
        return s.trim().isEmpty() || s.trim().startsWith(Constants.WELL);
    }
    
    @Override
    public String toString()
    {
        if (map != null)
        {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, String> entry : map.entrySet())
            {
                sb.append(entry.getKey()).append(this.keyValueGap).append(entry.getValue()).append(this.propertyGap);
            }
            
            if (sb.length() > 0)
            {
                sb.delete(sb.length() - this.propertyGap.length(), sb.length());
            }
            return sb.toString();
        }
        else
        {
            return Constants.EMPTY;
        }
    }
    
    /**
     * 将属性排序后输出
     * @return
     */
    public String sortProperties()
    {
        if (map != null)
        {
            StringBuilder sb = new StringBuilder();
            List<String> list = new ArrayList<String>(map.keySet());
            Collections.sort(list);
            for (String key : list)
            {
                sb.append(key).append(this.keyValueGap).append(get(key)).append(this.propertyGap);
            }
            if (sb.length() > 0)
            {
                sb.delete(sb.length() - this.propertyGap.length(), sb.length());
            }
            return sb.toString();
        }
        else
        {
            return Constants.EMPTY;
        }
    }
    
    @Override
    public KeyValueBean clone()
    {
        KeyValueBean bean = new KeyValueBean(this.toString(), this.propertyGap, this.keyValueGap);
        return bean;
    }

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

    public void addProperties(KeyValueBean bean) {
        if (bean == null) return;
        for (Entry<String, String> entry : bean.getProperties().entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
    }
}
