package com.tuhanbao.base.util.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.KeyValueBean;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 配置文件Manager本身也有一个配置文件
 * 
 * @author tuhanbao
 *
 */
public final class ConfigManager implements ConfigRefreshListener {
    
    private static final String BASE_CONFIG = "base";

    private static final Map<String, Config> CONFIG_MAP = new HashMap<>();

    private static final Map<String, List<ConfigRefreshListener>> listeners = new HashMap<String, List<ConfigRefreshListener>>();

    //不能为空，先默认为生产模式。
    private static ConfigPattern CURRENT_CONFIG_PATTERN = ConfigPattern.PRODUCE;
    
    private static boolean IS_MAINTAINING;

    private static String CONFIG_PATH;
    
    private static boolean hasInit = false;
    
    private ConfigManager() {
    }

    public static synchronized void init(File f) {
        if (!hasInit) {
            // jar模式
            if (f == null) f = new File(Constants.CONFIG_ROOT);
            CONFIG_PATH = f.getAbsolutePath();
    
            List<File> files = new ArrayList<File>();
            addConfigFiles(files, f);
            
            if (files.isEmpty()) return;
            for (File configFile : files) {
                addConfig(configFile);
            }
    
            refreshBaseConfig();
            
            addListener(new ConfigManager());
            hasInit = true;
        }
    }

    /**
     * 默认返回DEFAULT_CONFIG_PATTERN
     * @return
     */
    public static ConfigPattern getCurrentConfigPattern() {
        return CURRENT_CONFIG_PATTERN;
    }

//    public static void loadAllListeners(String packageName) {
//        // 注册所有的ConfigRefreshListener
//        try {
//            List<Class<?>> classes = ClazzUtil.getAllClasses(packageName, new IClassFilter() {
//                @Override
//                public boolean filterClass(Class<?> clazz) {
//                    return clazz.isAssignableFrom(ConfigRefreshListener.class);
//                }
//            });
//
//            for (Class<?> clazz : classes) {
//                ConfigRefreshListener item = (ConfigRefreshListener)clazz.newInstance();
//                addListener(item);
//            }
//        }
//        catch (IOException | InstantiationException | IllegalAccessException e) {
//            LogManager.error(e);
//        }
//    }

    private static void addConfigFiles(List<File> files, File f) {
        for (File file : f.listFiles()) {
            if (file.isDirectory()) {
                addConfigFiles(files, file);
            }
            else {
                if (file.getName().endsWith(Constants.PROPERTIES_SUFFIX)) {
                    //必须先加载base
                    if ((BASE_CONFIG + Constants.PROPERTIES_SUFFIX).equals(file.getName())) {
                        addConfig(BASE_CONFIG, new Config(file.getAbsolutePath()));
                    }
                    else {
                        files.add(file);
                    }
                }
            }
        }
    }

    private static void addConfig(String key, Config config) {
        CONFIG_MAP.put(key, config);
    }

    private static void addConfig(File configFile) {
        //取出文件名字
        String name = configFile.getName();
        String key = name.substring(0, name.indexOf(Constants.STOP_EN));
        
        addConfig(key, new Config(configFile.getAbsolutePath()));
    }


    public static synchronized Config getConfig(String key) {
        return CONFIG_MAP.get(key);
    }

    public static void addListener(ConfigRefreshListener listener) {
        if (listener == null) return;
        
        String key = listener.getKey();
        List<ConfigRefreshListener> value = listeners.get(key);
        if (value == null) {
            //一般最多两个 listener
            value = new ArrayList<ConfigRefreshListener>(2); 
            listeners.put(key, value);
        }
        value.add(listener);
    }

    public static void removeListener(String key) {
        listeners.remove(key);
    }

    public static synchronized Config getBaseConfig() {
        return getConfig(BASE_CONFIG);
    }

    public static synchronized void refreshConfig(String key) {
        String propertiesPath = getPropertiesPath(key);
        if (!StringUtil.isEmpty(propertiesPath)) {
            addConfig(key, new Config(propertiesPath));
        }
        
        configRefreshed(key);
    }

    public static synchronized void refreshConfig(String key, String content) {
        if (StringUtil.isEmpty(content)) {
            refreshConfig(key);
            return;
        }
        
        KeyValueBean bean = new KeyValueBean(content, Constants.ENTER, Constants.EQUAL);
        Config config = getConfig(key);
        if (config != null) {
            config.addProperties(bean);
        }

        configRefreshed(key);
    }

    public static String getPropertiesPath(String key) {
        File root = new File(FileUtil.appendPath(CONFIG_PATH, CURRENT_CONFIG_PATTERN.getName()));
        File childFile = FileUtil.getChildFile(key + ".properties", root);
        if (childFile == null) {
            root = new File(CONFIG_PATH);
            childFile = FileUtil.getChildFileDirect(key + ".properties", root);
        }
        
        if (childFile == null) {
            return Constants.EMPTY;
        }
        return childFile.getPath();
    }

    /**
     * base.cfg是不会刷新的
     * 否则可能死循环
     */
    public static synchronized void refreshAllConfig() {
        for (Entry<String, Config> entry : CONFIG_MAP.entrySet()) {
            String key = entry.getKey();
            if (BASE_CONFIG.equals(key)) {
                continue;
            }
            refreshConfig(key);
        }
    }

    private static void configRefreshed(String key) {
        if (listeners.containsKey(key)) {
            for (ConfigRefreshListener item : listeners.get(key)) {
                item.refresh();
            }
        }
    }

    public static void setPattern(String name) {
        ConfigPattern configPattern = ConfigPattern.getPattern(name);
        if (!configPattern.equals(CURRENT_CONFIG_PATTERN)) {
            CURRENT_CONFIG_PATTERN = configPattern;

            //为了通知所有依赖配置文件的常量类
            refreshAllConfig();
        }
    }

    public static boolean isMaintaining() {
        return IS_MAINTAINING;
    }
    
    public static void setMaintaining(boolean value) {
        IS_MAINTAINING = value;
    }

    public static String[] getAdminTelephone() {
        return StringUtil.string2Array(getBaseConfig().getString(BaseConfigConstants.ADMIN_MOBILE));
    }

    @Override
    public void refresh() {
        refreshBaseConfig();
    }
    
    private static void refreshBaseConfig() {
        Config baseConfig = getBaseConfig();
        if (baseConfig != null) {
            if (baseConfig.containsKey(BaseConfigConstants.PATTERN)) {
                setPattern(baseConfig.getString(BaseConfigConstants.PATTERN));
            }
            
            if (baseConfig.containsKey(BaseConfigConstants.IS_MAINTAINING)) {
                IS_MAINTAINING = baseConfig.getBoolean(BaseConfigConstants.IS_MAINTAINING);
            }
        }
    }

    @Override
    public String getKey() {
        return BASE_CONFIG;
    }
    
    public static boolean isDebug() {
        return CURRENT_CONFIG_PATTERN == ConfigPattern.DEBUG;
    }
}
