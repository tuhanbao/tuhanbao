package com.td.ca.autotool.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.td.ca.Constants;
import com.td.ca.base.util.io.codegenarator.AbstractProjectInfo;
import com.td.ca.base.util.objutil.FileUtil;
import com.td.ca.base.util.objutil.StringUtil;

/**
 *
 * @author wangbing
 *
 */
public class SpringMvcProjectInfo extends AbstractProjectInfo {

    /**
     * 分别为mo,constants,enum,mapper,service,iservice,controller所在代码路径
     */
    private String modelUrl, constantsUrl, enumUrl, mapperUrl, serviceUrl, iServiceUrl, baseControllerUrl, controllerUrl;
    /**
     * 分别为res,jsp,js,config, api, impl所在路径
     */
    private String resUrl, apiUrl, implUrl;

    private String packageHead;

    private Map<String, String> diyConfigNames = new HashMap<>();

    private Map<String, String> replaceMap = new HashMap<>();

    /**
     * maven的src路径
     */
    private static final String MAVEN_SRC = "src/main/java";
    /**
     * maven的资源文件路径
     */
    private static final String MAVEN_RES = "src/main/resources";

    public SpringMvcProjectInfo(String rootPath, String projectName, String packageHead) {
        super(rootPath, FileUtil.appendPath(rootPath, MAVEN_SRC), projectName);
        if (StringUtil.isEmpty(packageHead)) {
            this.packageHead = Constants.DEFAULT_PACKAGE_HEAD;
        } else {
            this.packageHead = packageHead;
        }
        this.resUrl = MAVEN_RES;
        this.configUrl = FileUtil.appendPath(resUrl, Constants.CONFIG_URL);

        apiUrl = getUrl(this.packageHead, Constants.API);
        implUrl = getUrl(this.packageHead, Constants.IMPL);
        baseControllerUrl = getUrl(this.packageHead, Constants.CONTROLLER);
        controllerUrl = getUrl(this.packageHead, Constants.CONTROLLER);
        modelUrl = getUrl(apiUrl, Constants.MODEL);
        constantsUrl = getUrl(apiUrl, Constants.CONSTANTS);
        enumUrl = getUrl(constantsUrl, Constants.ENUMS);
        mapperUrl = getUrl(implUrl, Constants.MAPPER);
        serviceUrl = getUrl(implUrl, Constants.SERVICE);
        iServiceUrl = getUrl(apiUrl, Constants.SERVICE);
    }

    public void addDiyConfig(String name, String value) {
        diyConfigNames.put(name, value);
    }

    public String getDiyConfigStr() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : diyConfigNames.entrySet()) {
            sb.append(entry.getKey()).append(Constants.COLON).append(entry.getValue()).append(Constants.COMMA);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public boolean hasDiyConfig() {
        return !diyConfigNames.isEmpty();
    }

    public String getServiceBeanUrl(String module) {
        return getUrl(modelUrl, getModulPackageName(module));
    }

    public String getModelPath(String module) {
        return getPath(srcPath, modelUrl, getModulPackageName(module));
    }

    @Override
    public String getConstantsUrl() {
        return constantsUrl;
    }

    @Override
    public String getEnumUrl() {
        return enumUrl;
    }

    public String getMapperUrl(String module) {
        return getUrl(mapperUrl, getModulPackageName(module));
    }

    public String getMapperPath(String module) {
        return getPath(srcPath, mapperUrl, getModulPackageName(module));
    }

    public String getServiceUrl(String module) {
        return getUrl(serviceUrl, getModulPackageName(module));
    }

    public String getServicePath(String module) {
        return getPath(srcPath, serviceUrl, getModulPackageName(module));
    }

    public String getIServiceUrl(String module) {
        return getUrl(iServiceUrl, getModulPackageName(module));
    }

    public String getIServicePath(String module) {
        return getPath(srcPath, iServiceUrl, getModulPackageName(module));
    }

    public String getResUrl() {
        return resUrl;
    }

    public String getControllerUrl(String module) {
        return getUrl(controllerUrl, getModulPackageName(module));
    }

    public String getBaseControllerUrl() {
        return baseControllerUrl;
    }

    public String getImplUrl() {
        return implUrl;
    }

    public String getPackageHead() {
        return packageHead;
    }

    public void addReplaceProperties(String key, String value) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        if (!key.startsWith("{")) {
            key = "{" + key + "}";
        }
        replaceMap.put(key, value);
    }

    public Map<String, String> getReplaceMap() {
        return this.replaceMap;
    }

    public static String getModulPackageName(String module) {
        if (StringUtil.isEmpty(module)) {
            return module;
        }
        return module.replace("_", "");
    }
}
