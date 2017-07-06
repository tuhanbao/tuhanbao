//package com.tuhanbao.autotool.mvc;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import com.tuhanbao.autotool.filegenerator.j2ee.IServiceClazzCreator;
//import com.tuhanbao.autotool.filegenerator.j2ee.MOClazzCreator;
//import com.tuhanbao.autotool.filegenerator.j2ee.ModelClazzCreator;
//import com.tuhanbao.autotool.filegenerator.j2ee.ServiceClazzCreator;
//import com.tuhanbao.autotool.filegenerator.j2ee.SolidClazzCreator;
//import com.tuhanbao.autotool.filegenerator.j2ee.SolidObject;
//import com.tuhanbao.autotool.mvc.web.WebManager;
//import com.tuhanbao.io.base.Constants;
//import com.tuhanbao.io.impl.ProjectInfo;
//import com.tuhanbao.io.impl.classUtil.ClassInfo;
//import com.tuhanbao.io.impl.classUtil.EnumClassInfo;
//import com.tuhanbao.io.impl.classUtil.MethodInfo;
//import com.tuhanbao.io.impl.classUtil.PackageEnum;
//import com.tuhanbao.io.impl.classUtil.VarInfo;
//import com.tuhanbao.io.impl.codeUtil.Xls2CodeUtil;
//import com.tuhanbao.io.impl.codeUtil.Xls2JavaUtil;
//import com.tuhanbao.io.impl.sqlUtil.DBUtil;
//import com.tuhanbao.io.impl.tableUtil.DBType;
//import com.tuhanbao.io.impl.tableUtil.EnumManager;
//import com.tuhanbao.io.impl.tableUtil.ImportColumn;
//import com.tuhanbao.io.impl.tableUtil.ImportTable;
//import com.tuhanbao.io.impl.tableUtil.TableConfig;
//import com.tuhanbao.io.impl.tableUtil.TableUtil;
//import com.tuhanbao.io.impl.tableUtil.src.TableSrcUtilFactory;
//import com.tuhanbao.io.impl.xlsUtil.fixConfig.Xls2Enum;
//import com.tuhanbao.io.impl.xlsUtil.fixConfig.Xls2ErrorCode;
//import com.tuhanbao.io.impl.xlsUtil.fixConfig.Xls2LanguageResource;
//import com.tuhanbao.io.objutil.ArrayUtil;
//import com.tuhanbao.io.objutil.FileUtil;
//import com.tuhanbao.io.objutil.OverwriteStrategy;
//import com.tuhanbao.io.objutil.StringUtil;
//import com.tuhanbao.io.txt.util.TxtUtil;
//import com.tuhanbao.util.config.Config;
//import com.tuhanbao.util.db.conn.DBSrc;
//import com.tuhanbao.util.db.table.CacheType;
//import com.tuhanbao.util.exception.MyException;
//import com.tuhanbao.util.util.clazz.ClazzUtil;
//
//public class ClassCreatorUtil {
//	private static final String CLASS_GAP = "\n    /**\n     * 分割线以上的代码不允许做任何改动，需要自定义的方法在分割线下实现\n     */\n    //-------------------------------------华丽丽的分割线-------------------------------------";
//	private static final String GAP_KEY = "-华丽丽的分割线-";
//	
//	private static final Map<String, String[][]> configs = new HashMap<String, String[][]>();
//	private static final Map<String, String[]> tableConfigs = new HashMap<String, String[]>();
//	
//	//以下为系统内置配置文件，区分大小写。 除这些配置外，均为表或者自定义配置文件
//	private static final String LANGUAGE = "language";
//	private static final String TYPES = "types";
//	private static final String ERROR_CODE = "error_code";
//	private static final String DB = "db";
//	private static final String BASE = "base";
//	private static final String TABLES = "tables";
//	private static final String README = "readme";
//	
//	//debug模式后缀
//	private static final String DEBUG = "_debug";
//	
//	private static SpringMvcProjectInfo project = null;
//	
//	public static void startCreateProject(Config solidConfig) {
//	    
//		
//		try {
//			//生成常量类配置 constants, types, errorCode, language, redis和其他常量配置文件
//			initConstants();
//			//加载数据库配置
//			initDb();
//			List<J2EETable> tables = getTables();
//			Collections.sort(tables);
//			//生成mo, bean, service, iService, controller代码
//			if (ProjectConfig.NEED_CREATE_CODE) {
//				for (J2EETable table : tables) {
//					DBSrc dbSrc = ModuleManager.getDBSrc(table.getModule());
//					ClassCreatorUtil.generate(project, dbSrc.getDbType(), table);
//					if (ProjectConfig.NEED_CREATE_WEB) {
//						WebManager.createWebFile(project, table);
//					}
//				}
//			}
//			//生成sql语句
//			if (ProjectConfig.NEED_SYNC_DB) {
//				for (String module : ModuleManager.getAllModules().keySet()) {
//					TxtUtil.write(FileUtil.appendPath(project.getRootPath(), project.getResUrl(), "init_" + module + ".sql"), "");
//				}
//				for (String module : ModuleManager.getAllDebugModules().keySet()) {
//					TxtUtil.write(FileUtil.appendPath(project.getRootPath(), project.getResUrl(), "init_" + module + "_debug.sql"), "");
//				}
//				
//				Map<DBSrc, List<J2EETable>> newTables = new HashMap<DBSrc, List<J2EETable>>();
//				Map<DBSrc, List<J2EETable>> newTablesDebug = new HashMap<DBSrc, List<J2EETable>>();
//				for (J2EETable table : tables) {
//					DBSrc dbSrc = ModuleManager.getDBSrc(table.getModule());
//					if (!newTables.containsKey(dbSrc)) newTables.put(dbSrc, new ArrayList<J2EETable>());
//					newTables.get(dbSrc).add(table);
//					try {
//						writeSql(table.getModule(), DBUtil.getSql(table.table, dbSrc), false);
//					}
//					catch (MyException e) {
//						//不处理
//					}
//					
//					dbSrc = ModuleManager.getDebugDBSrc(table.getModule());
//					//debug的db配置可能不存在
//					if (dbSrc == null) continue;
//					if (!newTablesDebug.containsKey(dbSrc)) newTablesDebug.put(dbSrc, new ArrayList<J2EETable>());
//					newTablesDebug.get(dbSrc).add(table);
//					try {
//						writeSql(table.getModule(), DBUtil.getSql(table.table, dbSrc), true);
//					}
//					catch (MyException e) {
//						//不处理
//					}
//				}
//				
//				//删除多余的表
//				for (Entry<DBSrc, List<J2EETable>> entry : newTables.entrySet()) {
//					List<ImportTable> oldTables = DBUtil.getTables(entry.getKey());
//					
//					A : for (ImportTable oldTable : oldTables) {
//						for (J2EETable newTable : entry.getValue()) {
//							if (oldTable.getTableName().equals(newTable.getTableName())) {
//								continue A;
//							}
//						}
//						
//						writeSql(ModuleManager.getModule(entry.getKey()), DBUtil.getDropTableSql(oldTable, entry.getKey().getDBType()), false);
//					}
//				}
//
//				//删除多余的表
//				for (Entry<DBSrc, List<J2EETable>> entry : newTablesDebug.entrySet()) {
//					List<ImportTable> oldTables = DBUtil.getTables(entry.getKey());
//					
//					A : for (ImportTable oldTable : oldTables) {
//						for (J2EETable newTable : entry.getValue()) {
//							if (oldTable.getTableName().equals(newTable.getTableName())) {
//								continue A;
//							}
//						}
//						
//						writeSql(ModuleManager.getDebugModule(entry.getKey()), DBUtil.getDropTableSql(oldTable, entry.getKey().getDBType()), true);
//					}
//				}
//			}
//			
//			
//			//生成TableConstants
//			ClassCreatorUtil.generateTableConstants(project);
//			writeTableLanguage(project);
//			//生成init/config/bak下的文件，包括applicationContext-config,db,mapper,serverManager,serviceImpl等
//	        SolidClazzCreator solidClazzCreator = new SolidClazzCreator(project, tables, getSolidObject(solidConfig));
//	        
//	        solidClazzCreator.writeSolidFiles();
//	        
//	        //生成其他配置文件
//	        for (Map.Entry<String, String[][]> entry : configs.entrySet()) {
//				String xlsName = entry.getKey();
//				if (tableConfigs.containsKey(xlsName)) continue;
//				if (xlsName.startsWith("T_") || xlsName.startsWith("V_")) continue;
//				String[][] arrays = entry.getValue();
//				createProperties(project, arrays, xlsName);
//			}
////	        DBUtil.getSql(tables, dbType)
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private static List<SolidObject> getSolidObject(Config config) {
//	    List<SolidObject> list = new ArrayList<SolidObject>();
//	    if (config == null) return list;
//	    
//	    for (Entry<String, String> entry : config.getProperties().entrySet()) {
//	        String key = entry.getKey();
//	        String[] array = StringUtil.string2Array(entry.getValue());
//	        
//	        String url = ArrayUtil.indexOf(array, 0);
//	        String targetUrl = ArrayUtil.indexOf(array, 1);
//	        int needOverwrite = OverwriteStrategy.COVER.value;
//	        String needOverwriteStr = ArrayUtil.indexOf(array, 2);
//	        if (!StringUtil.isEmpty(needOverwriteStr)) {
//	            needOverwrite = Integer.valueOf(needOverwriteStr);
//	        }
//	        list.add(new SolidObject(key, url, targetUrl, needOverwrite));
//	    }
//	    
//	    return list;
//	}
//	
//	private static void writeSql(String module, String sql, boolean isDebug) throws IOException {
//		String url = FileUtil.appendPath(project.getRootPath(), project.getResUrl(), "init_" + module + (isDebug ? "_debug.sql" : ".sql"));
//		TxtUtil.write(url, sql, true);
//	}
//	
//	private static void generate(ProjectInfo project, DBType dbType,
//			J2EETable table) throws IOException {
//		ClassInfo clazz = null;
//		
//		clazz = new ServiceClazzCreator(project, table).toClazz();
//		String serviceUrl = getFullPath(project, clazz);
//		if (!FileUtil.isExists(serviceUrl)) {
//			TxtUtil.write(serviceUrl, clazz.toString());
//		}
//
//		clazz = new IServiceClazzCreator(project, table).toClazz();
//		String iServiceUrl = getFullPath(project, clazz);
//		if (!FileUtil.isExists(iServiceUrl)) {
//			TxtUtil.write(iServiceUrl, clazz.toString());
//		}
//        
//		//创建bean对象
//        ClassInfo classInfo = new ModelClazzCreator(project, table).toClazz();
//        String url = FileUtil.appendPath(project.getSrcPath(), 
//        		Xls2JavaUtil.instance.getClassSrcUrl(classInfo));
//        File f = new File(url);
//        if (!f.exists())
//        {
//            TxtUtil.write(url, classInfo.toString());
//        }
//
//        //创建MO对象
//        classInfo = new MOClazzCreator(project).toClazz(table);
//        url = FileUtil.appendPath(project.getSrcPath(), 
//        		Xls2JavaUtil.instance.getClassSrcUrl(classInfo));
//        TxtUtil.write(url, classInfo.toString());
//        
//        //暂时不生成controller
////        clazz =  new ControllerClazzCreator(project).toClazz(table);
////        String controllerUrl = getFullPath(project, clazz);
////		if (!FileUtil.isExists(controllerUrl)) {
////			TxtUtil.write(controllerUrl, clazz.toString());
////		}
//	}
//    
//	
//	private static String getFullPath(SpringMvcProjectInfo project, ClassInfo clazz) {
//		return FileUtil.appendPath(project.getSrcPath(), clazz.getPath());
//	}
//	
//	@SuppressWarnings("unused")
//	@Deprecated
//	private static String getAppendClassStr(ClassInfo clazz, String url) {
//		String classStr = clazz.toString();
//		
//		int endIndex = classStr.lastIndexOf(Constants.ENTER, classStr.lastIndexOf("}"));
//		String result = classStr.substring(0, endIndex);
//		if (FileUtil.isExists(url)) {
//			String oldStr = null;
//			try {
//				oldStr = TxtUtil.read(url);
//			} catch (IOException e) {
//				throw MyException.getMyException(e);
//			}
//			int gapIndex = oldStr.indexOf(GAP_KEY);
//			String diyClass = classStr.substring(endIndex);
//			if (gapIndex != -1) {
//				diyClass = oldStr.substring(oldStr.indexOf(Constants.ENTER, gapIndex));
//			}
//			result = classStr.substring(0, endIndex) + CLASS_GAP + diyClass;
//		}
//		else {
//			result = classStr.substring(0, endIndex) + CLASS_GAP + classStr.substring(endIndex);
//		}
//		return result;
//	}
//	
//	private static void initDb() {
//		String[][] arrays = configs.remove(DB);
//		String[][] arraysDebug = configs.remove(DB + DEBUG);
//		
//		int length = arrays.length;
//		for (int i = 1; i < length; i++) {
//			String[] array = arrays[i];
//			String module = null;
//			if (!StringUtil.isEmpty(array[0])) {
//				module = array[0];
//			}
//			DBSrc src = new DBSrc(getDriver(array[1]), array[2], array[3], array[4], 0);
//			ModuleManager.addModule(module, src);
//		}
//		
//		if (arraysDebug != null) {
//			length = arraysDebug.length;
//			for (int i = 1; i < length; i++) {
//				String[] array = arraysDebug[i];
//				String module = null;
//				if (!StringUtil.isEmpty(array[0])) {
//					module = array[0];
//				}
//				DBSrc src = new DBSrc(getDriver(array[1]), array[2], array[3], array[4], 0);
//				ModuleManager.addDebugModule(module, src);
//			}
//		}
//	}
//	
//	private static void initConstants() throws IOException {
//		configs.remove(README);
//		String[][] arrays = configs.remove(TABLES);
//		int length = arrays.length;
//		for (int i = 1; i < length; i++) {
//			if (Xls2CodeUtil.isEmptyLine(arrays[i])) continue;
//			String tableName = ArrayUtil.indexOf(arrays[i], 0);
//			if (!StringUtil.isEmpty(tableName)) {
//				tableConfigs.put(tableName, arrays[i]);
//			}
//		}
//		
//		String url = "";
//		//这个也用配置文件实现
////		arrays = configs.remove(CONSTANTS);
////		if (arrays != null) {
////			ClassInfo classInfo = new Xls2Constants().getClassInfos(project, arrays).get(0);
////	    	url = FileUtil.appendPath(project.getFullConstantsUrl(),
////	    			classInfo.getClassName() + ".java");
////	    	TxtUtil.write(url, classInfo.toString());
////		}
//    	
//    	arrays = configs.remove(TYPES);
//    	Xls2Enum xls2Enum = new Xls2Enum();
//		List<ClassInfo> classInfos = xls2Enum.getClassInfos(project, arrays);;
//        for (ClassInfo ci : classInfos) {
//        	EnumManager.register(ci.getName(), (EnumClassInfo) ci);
//        	url = FileUtil.appendPath(project.getEnumPath(),
//            		ci.getClassName() + ".java");
//        	TxtUtil.write(url, ci.toString());
//        }
//        
//        arrays = configs.remove(ERROR_CODE);
//        Xls2ErrorCode xls2ErrorCode = new Xls2ErrorCode();
//		classInfos = xls2ErrorCode.getClassInfos(project, arrays);
//        for (ClassInfo ci : classInfos) {
//        	url = FileUtil.appendPath(project.getConstantsPath(),
//        			ci.getClassName() + ".java");
//        	TxtUtil.write(url, ci.toString());
//        }
//        url = FileUtil.appendPath(project.getRootPath(), project.getConfigUrl(),
//				"errorCode.properties");
//        TxtUtil.write(url, xls2ErrorCode.getProperties(arrays));
//        
//        arrays = configs.remove(LANGUAGE);
//        //语言资源
//        String[] strs = new Xls2LanguageResource().getConfigStrAndCode(project, arrays);
//    	url = FileUtil.appendPath(project.getConstantsPath(),
//    			"LanguageResource.java");
//    	TxtUtil.write(url, strs[0]);
//    	url = FileUtil.appendPath(project.getRootPath(), project.getConfigUrl(),
//				"language.properties");
//    	
//    	StringBuilder sb = new StringBuilder();
//    	sb.append(xls2Enum.getProperties());
//    	sb.append(strs[1]);
//    	TxtUtil.write(url, sb.toString());
//	}
//	
//	private static void createProperties(SpringMvcProjectInfo project, String[][] arrays, String name) throws IOException {
//		//生成Config文件
//		ClassInfo clazzInfo = new ClassInfo();
//		clazzInfo.setPackageInfo(project.getConstantsUrl());
//		clazzInfo.addImportInfo("com.tuhanbao.util.config.Config");
//		clazzInfo.addImportInfo("com.tuhanbao.util.config.ConfigManager");
//		clazzInfo.addImportInfo("com.tuhanbao.util.config.ConfigRefreshListener");
//		String configClassName = ClazzUtil.getClassName(name) + "Config";
//		clazzInfo.setName(configClassName + " implements ConfigRefreshListener");
//		
//		VarInfo var = new VarInfo();
//    	var.setType("String");
//    	var.setName("KEY");
//    	var.setFinal(true);
//    	var.setStatic(true);
//    	var.setPe(PackageEnum.PUBLIC);
//    	var.setValue("\"" + name + "\"");
//    	clazzInfo.addVarInfo(var);
//    	
//		StringBuilder sb = new StringBuilder();
//		StringBuilder initStr = new StringBuilder();
//		
//		boolean isCreateJava = true;
//		for (String[] array : arrays) {
//			if (Xls2CodeUtil.isEmptyLine(array)) {
//				String key = ArrayUtil.indexOf(array, 0);
//				if ("#need_create_config".equalsIgnoreCase(key)) {
//					String value = ArrayUtil.indexOf(array, 1);
//					if ("false".equalsIgnoreCase(value) || "0".equals(value)) {
//						isCreateJava = false;
//					}
//				}
//				continue;
//			}
//			String comment = ArrayUtil.indexOf(array, 3);
//			if (!StringUtil.isEmpty(comment)) {
//				sb.append(Constants.WELL).append(comment.replace("\n", Constants.ENTER + Constants.WELL)).append(Constants.ENTER);
//			}
//			
//			String key = ArrayUtil.indexOf(array, 0);
//			String value = ArrayUtil.indexOf(array, 1);
//			String typeStr = ArrayUtil.indexOf(array, 2);
//			int type = 0;//0 String, 1 int, 2 boolean
//			if (StringUtil.isEmpty(typeStr)) {
//				if (value.matches("\\d*")) {
//					type = 1;
//				}
//				else if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
//					type = 2;
//				}
//				else {
//					type = 0;
//				}
//			}
//			else {
//				if ("INT".equalsIgnoreCase(typeStr)) {
//					type = 1;
//				}
//				else if ("BOOLEAN".equalsIgnoreCase(typeStr)) {
//					type = 2;
//				}
//				else {
//					type = 0;
//				}
//			}
//			
//	    	var = new VarInfo();
//	    	var.setType("String");
//	    	var.setName(key.toUpperCase() + "_NAME");
//	    	var.setFinal(true);
//	    	var.setStatic(true);
//	    	var.setPe(PackageEnum.PRIVATE);
//	    	var.setValue("\"" + key + "\"");
//	    	clazzInfo.addVarInfo(var);
//	    	
//	    	initStr.append(Constants.ENTER).append(Xls2CodeUtil.GAP2).append(key.toUpperCase()).append(" = ").append("config.get");
//	    	var = new VarInfo();
//			if (type == 1) {
//				var.setType("int");
//				initStr.append("Int(").append(key.toUpperCase() + "_NAME").append(");");
//				sb.append(key).append(Constants.EQUAL).append(value).append(Constants.ENTER);
//			}
//			else if (type == 2) {
//				var.setType("boolean");
//				initStr.append("Int(").append(key.toUpperCase() + "_NAME").append(") == 1;");
//				sb.append(key).append(Constants.EQUAL).append("true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value) ? "1" : 0).append(Constants.ENTER);
//			}
//			else {
//				var.setType("String");
//				initStr.append("String(").append(key.toUpperCase() + "_NAME").append(");");
//				value = value.replace("\n", "\\n");
//				sb.append(key).append(Constants.EQUAL).append(value).append(Constants.ENTER);
//			}
//	    	var.setName(key.toUpperCase());
//	    	var.setStatic(true);
//	    	var.setPe(PackageEnum.PUBLIC);
//	    	clazzInfo.addVarInfo(var);
//	    	
//		}
//		
//		StringBuilder staticStr = new StringBuilder();
//		staticStr.append(Xls2CodeUtil.GAP2).append("ConfigManager.addListener(new ").append(configClassName).append("());").append(Constants.ENTER);
//		staticStr.append(Xls2CodeUtil.GAP2).append("init();");
//		clazzInfo.setStaticStr(staticStr.toString());
//		
//		
//		MethodInfo method = new MethodInfo();
//		method.setPe(PackageEnum.PRIVATE);
//		method.setName(configClassName);
//		clazzInfo.addMethodInfo(method);
//		
//		method = new MethodInfo();
//		method.setPe(PackageEnum.PUBLIC);
//		method.setType("void");
//		method.setStatic(true);
//		method.setName("init");
//		method.setMethodBody("Config config = ConfigManager.getConfig(KEY);" + initStr.toString()); 
//		clazzInfo.addMethodInfo(method);
//		
//		method = new MethodInfo();
//		method.setPe(PackageEnum.PUBLIC);
//		method.setType("void");
//		method.setName("refresh");
//		method.setMethodBody("init();");
//		clazzInfo.addMethodInfo(method);
//		
//		method = new MethodInfo();
//		method.setPe(PackageEnum.PUBLIC);
//		method.setType("String");
//		method.setName("getKey");
//		method.setMethodBody("return KEY;");
//		clazzInfo.addMethodInfo(method);
//		
//		boolean isDebugProperties = name.endsWith(DEBUG);
//		if (isCreateJava && !isDebugProperties) {
//			String url = FileUtil.appendPath(project.getSrcPath(), 
//	        		Xls2JavaUtil.instance.getClassSrcUrl(clazzInfo));
//	        TxtUtil.write(url, clazzInfo.toString());
//		}
//		
//		if (isDebugProperties) {
//			TxtUtil.write(FileUtil.appendPath(project.getRootPath(), project.getConfigUrl(), "debug",
//					name + ".properties"), sb.toString());
//		}
//		else {
//			//base不需要覆盖
//			String url = FileUtil.appendPath(project.getRootPath(), project.getConfigUrl(),
//					name + ".properties");
//			if (!name.equalsIgnoreCase(BASE) || !FileUtil.isExists(url) ) {
//				TxtUtil.write(url, sb.toString());
//			}
//		}
//    	
//	}
//	
//	private static String getDriver(String dbType) {
//		if ("oracle".equalsIgnoreCase(dbType)) {
//			return "oracle.jdbc.driver.OracleDriver";
//		}
//		return "com.mysql.jdbc.Driver";
//	}
//	
//	private static List<J2EETable> getTables() {
//		List<J2EETable> list = new ArrayList<J2EETable>();
//		for (String tableName : tableConfigs.keySet()) {
//			String[][] arrays = configs.remove(tableName);
//			list.add(getTable(tableName, arrays));
//		}
//		
//		//这里需要初始化一下所有的table外键
//		for (J2EETable table : list) {
//			List<ImportColumn> cols = table.getColumns();
//			for (ImportColumn ic : cols) {
//				ic.getFkColumn();
//			}
//		}
//		return list;
//	}
//	
//	private static J2EETable getTable(String tableName, String[][] arrays) {
//		String[] tableConfig = tableConfigs.get(tableName);
////		#表名	中文描述	缓存(NOT/ALL/AUTO)默认为NO	序列号（oracle数据库可配置，非必填，填autocreate，会根据表名自动生成序列）	默认排序字段（可不填）
////		T_CRAWLER_CLASS		NO	autocreate	
//		String comment = ArrayUtil.indexOf(tableConfig, 1);
//		String module = ArrayUtil.indexOf(tableConfig, 2);
//		CacheType cacheType = getCacheType(ArrayUtil.indexOf(tableConfig, 3));
//		String seq = ArrayUtil.indexOf(tableConfig, 4);
//		String orderCol = ArrayUtil.indexOf(tableConfig, 5);
//		DBType dbType = ModuleManager.getDBSrc(module).getDbType();
//
//		ImportTable table = TableSrcUtilFactory.getTable(tableName, arrays, dbType);
//		table.setComment(comment);
//		TableConfig tcfg = new TableConfig();
//		tcfg.setCacheType(cacheType);
//		tcfg.setDefaultOrderColName(orderCol);
//		if (dbType == DBType.ORACLE) {
//			tcfg.setSequence(getSeqName(seq, tableName));
//		}
//		else if (dbType == DBType.MYSQL) {
//            tcfg.setAutoIncrement("autocreate".equalsIgnoreCase(seq));
//		}
//		table.setTableConfig(tcfg);
//		return new J2EETable(table, module);
//	}
//	
//	private static String getSeqName(String seqName, String tableName) {
//		if ("autocreate".equalsIgnoreCase(seqName)) {
//			tableName = tableName.toUpperCase();
//			if (tableName.startsWith("T_") || tableName.startsWith("V_")) tableName = tableName.substring(2);
//			return "SEQ_" + tableName;
//		}
//		else {
//			return seqName;
//		}
//	}
//
//	private static CacheType getCacheType(String str) {
//		if ("auto".equalsIgnoreCase(str)) return CacheType.AUTO;
//		if ("all".equalsIgnoreCase(str)) return CacheType.CACHE_ALL;
//		return CacheType.NOT_CACHE;
//	}
//	
//    private static void writeTableLanguage(SpringMvcProjectInfo project) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        for (J2EETable table : J2EETableManager.getAllTables().values()) {
//            sb.append(getTableLanguage(table));
//        }
//        String url = FileUtil.appendPath(project.getRootPath(), project.getConfigUrl(),
//                "language.properties");
//        TxtUtil.write(url, sb.toString(), true);
//    }
//    
//	private static String getTableLanguage(J2EETable table) {
//	    StringBuilder sb = new StringBuilder();
//	    String comment = table.getComment();
//	    String tableName = table.getName();
//        if (!StringUtil.isEmpty(comment)) {
//            sb.append(tableName + "=" + comment).append(Constants.ENTER);
//        }
//	    for (ImportColumn column : table.getColumns()) {
//	        comment = column.getComment();
//	        if (!StringUtil.isEmpty(comment)) {
//	            sb.append(tableName).append(".").append(column.getName()).append("=").append(comment).append(Constants.ENTER);
//	        }
//	    }
//	    return sb.toString();
//	}
//}
