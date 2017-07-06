package com.tuhanbao.base.chain;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.clazz.IClassFilter;
import com.tuhanbao.base.util.log.LogManager;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EventHandleManager {
    
    private static Map<Class<? extends IEvent>, IEventHandler> HANDLERS = new HashMap<Class<? extends IEvent>, IEventHandler>();
    
    private static final IEventHandler DEFAULT_HANDLER = new IEventHandler<IEvent>() {

        @Override
        public void handle(IEvent t) {
        }
    };
    
    static {
        load();
    }
    
    private static void load() {
        try {
            List<Class<?>> list = ClazzUtil.getAllClasses(ClazzUtil.getPackageName(EventHandleManager.class), new IClassFilter() {
                @Override
                public boolean filterClass(Class<?> clazz) {
                    return IEventHandler.class.isAssignableFrom(clazz);
                }
            });
            for (Class<?> clazz : list) {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                    continue;
                }
                register(getEventType(clazz), (IEventHandler)clazz.newInstance());
            }
        }
        catch (Exception e) {
            LogManager.error(e);
        }
    }
    
    private static Class<?> getEventType(Class<?> clazz) {
        Type[] types = clazz.getGenericInterfaces();

        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType item = (ParameterizedType)type;
                if (item.getRawType() == IEventHandler.class) {
                    return (Class<?>)item.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }
    
    
    public static void handle(List<IEvent> events) {
        for (IEvent event : events) {
            getEventHandler(event).handle(event);
        }
    }
    
    private static IEventHandler getEventHandler(IEvent event) {
        Class clazz = event.getClass();
        while (clazz != null) {
            if (HANDLERS.containsKey(clazz)) {
                return HANDLERS.get(clazz);
            }
            clazz = clazz.getSuperclass();
        }
        
        //如果最后没找到，就调用默认的handler
        return DEFAULT_HANDLER;
    }
    
    private static void register(Class<?> eventType, IEventHandler handle) {
        HANDLERS.put((Class<? extends IEvent>)eventType, handle);
    }
}
