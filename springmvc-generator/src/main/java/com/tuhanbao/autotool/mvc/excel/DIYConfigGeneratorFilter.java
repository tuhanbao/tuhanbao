package com.tuhanbao.autotool.mvc.excel;

import java.util.Map;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.chain.event.CreateFileEvent;
import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigPattern;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.db.table.data.BooleanValue;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.MethodInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.PackageEnum;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.VarInfo;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.Xls2CodeUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.ArrayUtil;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

@FilterAnnotation("diyConfig")
@SuppressWarnings("unchecked")
public class DIYConfigGeneratorFilter extends ExcelAGCFilter {
    
    private static final int INT = 1, BOOLEAN = 2, STRING = 0;

    @Override
    public void filter(Context context) {
        Map<String, String[]> tableConfigs = this.getTableConfigs(context);
        SpringMvcProjectInfo project = this.getProject(context);
        //生成其他配置文件
        for (Map.Entry<String, String[][]> entry : ((Map<String, String[][]>)context.get(CONFIG)).entrySet()) {
            String xlsName = entry.getKey();
            if (tableConfigs.containsKey(xlsName)) continue;
            if (xlsName.startsWith("T_") || xlsName.startsWith("V_")) continue;
            String[][] arrays = entry.getValue();
            createProperties(project, context, arrays, xlsName);
        }
    }
    
    /**
     * 生成config类和properties文件
     * @param project
     * @param arrays
     * @param name
     */
    private static void createProperties(SpringMvcProjectInfo project, Context context, String[][] arrays, String name) {
        //生成Config文件
        ClassInfo clazzInfo = new ClassInfo();
        clazzInfo.setPackageInfo(project.getConstantsUrl());
        
        clazzInfo.addImportInfo(Config.class);
        clazzInfo.addImportInfo(ConfigManager.class);
        clazzInfo.addImportInfo(ConfigRefreshListener.class);
        clazzInfo.addImportInfo(LogManager.class);
        String configClassName = ClazzUtil.getClassName(name) + "Config";
        clazzInfo.setName(configClassName + " implements ConfigRefreshListener");
        
        VarInfo var = new VarInfo();
        var.setType("String");
        var.setName("KEY");
        var.setFinal(true);
        var.setStatic(true);
        var.setPe(PackageEnum.PUBLIC);
        var.setValue("\"" + name + "\"");
        clazzInfo.addVarInfo(var);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder initStr = new StringBuilder();
        
        boolean isCreateJava = true;
        //可以使用#need_create_config定义是否需要生成config
        for (String[] array : arrays) {
            if (Xls2CodeUtil.isEmptyLine(array)) {
                String key = ArrayUtil.indexOf(array, 0);
                if ("#need_create_config".equalsIgnoreCase(key)) {
                    String value = ArrayUtil.indexOf(array, 1);
                    isCreateJava = BooleanValue.valueOf(value).getValue();
                }
                continue;
            }
            String comment = ArrayUtil.indexOf(array, 3);
            if (!StringUtil.isEmpty(comment)) {
                sb.append(Constants.WELL).append(comment.replace(Constants.ENTER, Constants.ENTER + Constants.WELL)).append(Constants.ENTER);
            }
            
            String key = ArrayUtil.indexOf(array, 0);
            String value = ArrayUtil.indexOf(array, 1);
            String typeStr = ArrayUtil.indexOf(array, 2);
            int type = 0;
            //0 String, 1 int, 2 boolean
            
            //如果未制定数据类型，根据数据本身特性进行默认判断
            if (StringUtil.isEmpty(typeStr)) {
                if (value.matches("\\d*")) {
                    type = INT;
                }
                else if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                    type = BOOLEAN;
                }
                else {
                    type = STRING;
                }
            }
            else {
                if ("INT".equalsIgnoreCase(typeStr) || (INT + "").equals(typeStr)) {
                    type = INT;
                }
                else if ("BOOLEAN".equalsIgnoreCase(typeStr) || (BOOLEAN + "").equals(typeStr)) {
                    type = BOOLEAN;
                }
                else {
                    type = STRING;
                }
            }
            
            var = new VarInfo();
            var.setType("String");
            var.setName(key.toUpperCase() + "_NAME");
            var.setFinal(true);
            var.setStatic(true);
            var.setPe(PackageEnum.PRIVATE);
            var.setValue("\"" + key + "\"");
            clazzInfo.addVarInfo(var);
            
            initStr.append(Constants.ENTER).append(Constants.GAP2).append(key.toUpperCase()).append(" = ").append("config.get");
            var = new VarInfo();
            if (type == INT) {
                var.setType("int");
                initStr.append("Int(").append(key.toUpperCase() + "_NAME").append(");");
                sb.append(key).append(Constants.EQUAL).append(value).append(Constants.ENTER);
            }
            else if (type == BOOLEAN) {
                var.setType("boolean");
                initStr.append("Int(").append(key.toUpperCase() + "_NAME").append(") == 1;");
                sb.append(key).append(Constants.EQUAL).append("true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value) ? "1" : 0).append(Constants.ENTER);
            }
            else {
                var.setType("String");
                initStr.append("String(").append(key.toUpperCase() + "_NAME").append(");");
                value = value.replace("\n", "\\n");
                sb.append(key).append(Constants.EQUAL).append(value).append(Constants.ENTER);
            }
            var.setName(key.toUpperCase());
            var.setStatic(true);
            var.setPe(PackageEnum.PUBLIC);
            clazzInfo.addVarInfo(var);
            
        }
        
        StringBuilder staticStr = new StringBuilder();
        staticStr.append(Constants.GAP2).append("ConfigManager.addListener(new ").append(configClassName).append("());").append(Constants.ENTER);
        staticStr.append(Constants.GAP2).append("init();");
        clazzInfo.setStaticStr(staticStr.toString());
        
        
        MethodInfo method = new MethodInfo();
        method.setPe(PackageEnum.PRIVATE);
        method.setName(configClassName);
        clazzInfo.addMethodInfo(method);
        
        method = new MethodInfo();
        method.setPe(PackageEnum.PUBLIC);
        method.setType("void");
        method.setStatic(true);
        method.setName("init");
        StringBuilder methodBody = new StringBuilder();
        methodBody.append("Config config = ConfigManager.getConfig(KEY);").append(Constants.ENTER);
        methodBody.append(Constants.GAP2).append("if (config == null) {").append(Constants.ENTER);
        methodBody.append(Constants.GAP3).append("LogManager.warn(\"no config file for : \" + KEY);").append(Constants.ENTER);
        methodBody.append(Constants.GAP3).append("return;").append(Constants.ENTER);
        methodBody.append(Constants.GAP2).append("}").append(Constants.ENTER);
        methodBody.append(initStr);
        method.setMethodBody(methodBody.toString()); 
        clazzInfo.addMethodInfo(method);
        
        method = new MethodInfo();
        method.setPe(PackageEnum.PUBLIC);
        method.setType("void");
        method.setName("refresh");
        method.setMethodBody("init();");
        clazzInfo.addMethodInfo(method);
        
        method = new MethodInfo();
        method.setPe(PackageEnum.PUBLIC);
        method.setType("String");
        method.setName("getKey");
        method.setMethodBody("return KEY;");
        clazzInfo.addMethodInfo(method);
        
        ConfigPattern cp = getConfigPattern(name);
        name = getCleanName(name);
        if (isCreateJava && cp == null) {
            context.addEvent(new CreateFileEvent(clazzInfo, project));
        }
        
        String url = FileUtil.appendPath(project.getRootPath(), project.getConfigUrl(), cp.getPath(),
                name + Constants.PROPERTIES_SUFFIX);
        context.addEvent(new CreateFileEvent(url, sb.toString()));
        //base不需要覆盖
        if (!name.equalsIgnoreCase(BASE) || !FileUtil.isExists(url) ) {
            context.addEvent(new CreateFileEvent(url, sb.toString()));
        }
        
    }

}
