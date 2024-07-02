package com.td.ca.web.util.lock;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个用于分布式管理tag的工具类
 *
 * 实现原理，数据库存放tag。
 * 进程启动，会从表中取出tag并缓存，每次执行操作前，检查tag，如果tag没变，执行后续操作。
 * 如果tag变了，更新缓存中的tag，跳过后续操作。
 *
 * 此工具类用于处理多节点时，但是只能一个节点处理的场景。
 * 比如定时发送通知，每隔十分钟推送离线消息等。
 */
public class TagBeanManager {
    private final Map<String, Long> tagCache = new HashMap<>();

    private final TagBeanService service;

    public TagBeanManager(TagBeanService service) {
        this.service = service;
    }

    public boolean getExcutePermission(String key) {
        return getPermissionInner(key, null);
    }

    public boolean getExcutePermission(String key, long newValue) {
        return getPermissionInner(key, newValue);
    }

    private boolean getPermissionInner(String key, Long newValue) {
        if (!tagCache.containsKey(key)) {
            TagBean tagBean = service.selectById(key);
            if (tagBean == null) {
                newTag(key);
                tagCache.put(key, 0L);
            } else {
                tagCache.put(key, tagBean.getTag());
            }
        }

        long currentValue = tagCache.get(key);
        if (newValue == null) {
            newValue = currentValue + 1;
        }
        boolean result = service.updateTag(key, currentValue, newValue);
        if (result) {
            tagCache.put(key, newValue);
        } else {
            TagBean tagBean = service.selectById(key);
            tagCache.put(key, tagBean.getTag());
        }
        return result;
    }

    private void newTag(String key) {
        service.addTag(key, 0L);
    }

}
