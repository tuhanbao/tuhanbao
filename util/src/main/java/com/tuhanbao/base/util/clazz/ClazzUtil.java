package com.tuhanbao.base.util.clazz;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

public class ClazzUtil {

    private static final String CLASS_SUFFIX = ".class";
    
    private static final int CLASS_SUFFIX_LENGTH = CLASS_SUFFIX.length();

    private static final String FILE = "file";
    
	public static String getClassName(String name) {
		String[] names = StringUtil.string2Array(name, "_");
		//将names转换成驼峰
		StringBuilder sb = new StringBuilder();
		for (String n : names) {
			if (!StringUtil.isEmpty(n))
				sb.append(str2ClassName(n));
		}
		return sb.toString();
	}
	
	public static String str2ClassName(String s)
    {
        s = s.toLowerCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
	
	public static String getVarName(String name) {
		String s = getClassName(name);
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}
	
	public static String firstCharLowerCase(String name) {
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

    /**
     * 取得当前类路径下的所有类
     * 
     * @param cls
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getAllClasses(String packageName, IClassFilter filter) throws IOException {
        if (StringUtil.isEmpty(packageName)) packageName = Constants.EMPTY;
        //扫描时packageName换成文件分割符
        packageName = packageName.replace(Constants.STOP_EN, Constants.FILE_SEP);
        Enumeration<URL> resourceUrls = ClazzUtil.class.getClassLoader().getResources(packageName);
        List<Class<?>> classes = new ArrayList<Class<?>>();
        
        packageName = packageName.replace(Constants.FILE_SEP, Constants.STOP_EN);
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            String protocol = url.getProtocol();
            if (protocol.equals(FILE)) {
                getClassesFromDir(new File(url.getFile()), Constants.EMPTY, classes, filter, packageName);
            }
            else {
                JarURLConnection jarCon = (JarURLConnection)url.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                getClassesFromJar(jarFile, classes, filter, packageName);
            }
        }

        return classes;
    }
    
    /**
     * 从jar包获取所有handler
     * 
     * @param jar
     * @param classes
     * @throws ClassNotFoundException
     * 
     * 参照的spring
     * @see ClassPathScanningCandidateComponentProvider.findCandidateComponents
     */
    private static void getClassesFromJar(JarFile jarFile, List<Class<?>> classes, IClassFilter filter, String packageName) {
        Enumeration<JarEntry> entrys = jarFile.entries();
        while (entrys.hasMoreElements()) {
            String name = entrys.nextElement().getName();
            if (name.endsWith(CLASS_SUFFIX)) {
                String className = name.replace(Constants.FILE_SEP, Constants.STOP_EN).substring(0, name.length() - CLASS_SUFFIX_LENGTH);
                if (StringUtil.isEmpty(packageName) || className.startsWith(packageName)) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (filter.filterClass(clazz)) {
                            classes.add(clazz);
                        }
                    }
                    catch (ClassNotFoundException e) {
                        LogManager.info("class not found : " + className);
                    }
                    catch (Throwable e) {
                        LogManager.error(e);
                    }
                }
            }
        }
    }

    /**
     * 从源码目录迭代查找handler
     * 
     * @param dir
     * @param pk
     * @return
     * @throws ClassNotFoundException
     */
    private static void getClassesFromDir(File dir, String pk, List<Class<?>> classes, IClassFilter filter, String packageName) {
        if (StringUtil.isEmpty(pk)) {
            pk = packageName;
        }
        if (!pk.isEmpty() && !pk.endsWith(Constants.STOP_EN)) pk += Constants.STOP_EN;
        if (!dir.exists()) {
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                getClassesFromDir(f, pk + f.getName(), classes, filter, packageName);
            }

            String name = f.getName();
            if (name.endsWith(CLASS_SUFFIX)) {
                String className = pk + name.substring(0, name.length() - CLASS_SUFFIX_LENGTH);
                if (StringUtil.isEmpty(packageName) || className.startsWith(packageName)) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (filter.filterClass(clazz)) {
                            classes.add(clazz);
                        }
                    }
                    catch (ClassNotFoundException e) {
                        LogManager.info("class not found : " + className);
                    }
                    catch (Throwable e) {
                        LogManager.info(e.getMessage());
                    }
                }
            }
        }
    }
    
    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage().getName();
    }
}
