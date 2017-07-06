package com.tuhanbao.base.chain;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.clazz.IClassFilter;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 责任链模式的上下文
 * 
 * @author Administrator
 *
 */
public class Context {
    protected List<IEvent> events;
    
    protected Map<String, Object> vars;
    
    protected LinkedList<IChainFilter> filters;
    
    private static final Map<String, IChainFilter> FILTERS = new HashMap<>();
    
    static {
        load();
    }
    
    private static void load() {
        try {
            List<Class<?>> list = ClazzUtil.getAllClasses(Constants.DEFAULT_PACKAGE_HEAD, new IClassFilter() {
                @Override
                public boolean filterClass(Class<?> clazz) {
                    return clazz.getAnnotation(FilterAnnotation.class) != null;
                }
            });
            for (Class<?> clazz : list) {
                String name = clazz.getAnnotation(FilterAnnotation.class).value();
                if (StringUtil.isEmpty(name)) {
                    continue;
                }
                register(name, (IChainFilter)clazz.newInstance());
            }
        }
        catch (Exception e) {
            LogManager.error(e);
        }
    }

    public void start() {
        if (filters.isEmpty()) {
            LogManager.info("no filter will run!");
        }
        
        while (!filters.isEmpty()) {
            filters.remove().filter(this);
        }
        
        EventHandleManager.handle(events);
    }
    
    private static void register(String name, IChainFilter newInstance) {
        FILTERS.put(name, newInstance);
    }

    public Context() {
        events = new LinkedList<IEvent>();
        vars = new HashMap<String, Object>();
        filters = new LinkedList<IChainFilter>();
    }
    
    public void putAttr(String argName, Object value) {
        vars.put(argName, value);
    }

    public Object get(String argName) {
        return vars.get(argName);
    }
    
    public void addEvent(IEvent event) {
        events.add(event);
    }
    
    public void addEvent(Collection<IEvent> event) {
        events.addAll(event);
    }
    
    public void addFilter(IChainFilter filter) {
        if (filter != null) filters.add(filter);
    }

    public void addFilterByName(String filters) {
        for (String filter : StringUtil.string2Array(filters)) {
            addFilter(getFilterByName(filter));
        }
    }

    private IChainFilter getFilterByName(String filterName) {
        return FILTERS.get(filterName);
    }
}
