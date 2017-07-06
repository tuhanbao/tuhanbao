package com.tuhanbao.autotool.mvc;
import com.tuhanbao.Constants;
import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;


public class SpringMvcProjectInfo extends ProjectInfo {
	
    //分别为mo,constants,enum,mapper,service,iservice,controller所在代码路径
	private String modelUrl, constantsUrl, enumUrl, mapperUrl, serviceUrl, iServiceUrl, controllerUrl;
	//分别为res,jsp,js,config, api, impl所在路径
	private String resUrl, configUrl, apiUrl, implUrl;
	
	private String packageHead;
	
	//maven的src路径
	private static final String MAVEN_SRC = "src/main/java";
	//maven的资源文件路径
	private static final String MAVEN_RES = "src/main/resources";
	
//    private static final String SERVER_CODE_FILE_NAME = "code";
//
//    private static final String CLIENT_CODE_FILE_NAME = "web";
	
	public SpringMvcProjectInfo(String rootPath, String projectName, String packageHead) {
		super(rootPath, FileUtil.appendPath(rootPath, MAVEN_SRC), projectName);
		if (StringUtil.isEmpty(packageHead)) {
		    this.packageHead = Constants.DEFAULT_PACKAGE_HEAD;
		}
		else {
		    this.packageHead = packageHead;
		}
		this.resUrl = MAVEN_RES;
		this.configUrl = FileUtil.appendPath(resUrl, Constants.CONFIG_URL);
		
		apiUrl = getUrl(packageHead, Constants.API, projectName);
		implUrl = getUrl(packageHead, Constants.IMPL, projectName);
		controllerUrl = getUrl(packageHead, Constants.CONTROLLER, projectName, Constants.CONTROLLER);
		modelUrl = getUrl(apiUrl, Constants.MODEL);
		constantsUrl = getUrl(apiUrl, Constants.CONSTANTS);
		enumUrl = getUrl(constantsUrl, Constants.ENUMS);
		mapperUrl = getUrl(implUrl, Constants.MAPPER);
		serviceUrl = getUrl(implUrl, Constants.SERVICE);
		iServiceUrl = getUrl(apiUrl, Constants.SERVICE);
	}

    public String getServiceBeanUrl(String module) {
		return getUrl(modelUrl, module);
	}
	
	public String getModelPath(String module) {
		return getPath(srcPath, modelUrl, module);
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
        return getUrl(mapperUrl, module);
    }

    public String getMapperPath(String module) {
        return getPath(srcPath, mapperUrl, module);
    }
    
    public String getServiceUrl(String module) {
        return getUrl(serviceUrl, module);
    }
    
    public String getServicePath(String module) {
        return getPath(srcPath, serviceUrl, module);
    }
    
    public String getIServiceUrl(String module) {
        return getUrl(iServiceUrl, module);
    }
    
    public String getIServicePath(String module) {
        return getPath(srcPath, iServiceUrl, module);
    }
    
    public String getResUrl() {
    	return resUrl;
    }

    public String getControllerUrl(String module) {
        return getUrl(controllerUrl, module);
	}
    
    public String getImplUrl() {
    	return implUrl;
    }

	public String getConfigUrl() {
		return this.configUrl;
	}

    public String getPackageHead() {
        return packageHead;
    }
}
