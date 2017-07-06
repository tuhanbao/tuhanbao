package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo4MemCache;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.MethodInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.PackageEnum;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.VarInfo;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.Xls2CodeUtil;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.Xls2JavaUtil;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.DBUtil;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.XlsTable.XlsColumn;
import com.tuhanbao.base.util.io.excel.Excel2007Util;
import com.tuhanbao.base.util.io.txt.TxtUtil;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 本类用于将设计db的excel表格转换成对应的一套java对象。
 * 包裹MO对象，javabean本身，proxy对象，生成数据库表的sql文件，TableConstants和IDUtil常量类
 * @author tuhanbao
 *
 */
public class TableUtil
{
    //每个项目的命名规则必须符合以下规范，如果不使用本套架构，请另行实现
    public static final String TABLECONSTANTS_CLASS_NAME = "TableConstants";
//    public static final String TABLE_CLASS_PACKAGE = "com.tuhanbao.service.base.constant";
    private static final String MOCLASS_SUFFIX = "MO";
//    private static final String CLASS_PACKEGE = "com.tuhanbao.service.bean";
    private static final String MANAGERCLASS_SUFFIX = "Proxy";
//    private static final String MANAGERCLASS_PACKEGE = "com.threetorch.service.bean";
//    private static final String MOCLASS_PACKEGE = "com.threetorch.service.base.servicebean";
    private static final String IDUTIL_CLASS_NAME = "IDUtil.java";
    
    private static final String[] MOCLASS_IMPORTS = new String[]{
        "com.tuhanbao.util.mem.MetaObject",
        "com.tuhanbao.util.mem.ServiceBean",
    };
    
    private static final String[] BEANCLASS_IMPORTS = new String[]{
    	"com.tuhanbao.util.mem.MetaObject",
    };
    
    private static final String VOID = "void";
    
    private static final String[] MANAGERCLASS_IMPORTS = new String[]{
        "java.util.ArrayList",
        "java.util.List",
        "java.util.Map",
        "com.tuhanbao.util.db.filter.SelectorFilter",
        "com.tuhanbao.util.db.table.Column",
        "com.tuhanbao.util.db.table.Table",
        "com.tuhanbao.util.mem.MetaObject",
        "com.tuhanbao.util.mem.MemCacheProxy",
    };
    
    private TableUtil(){};
    
    /**
     * codeUrl只需传入根目录即可
     * 
     * @param dbXlsUrl
     * @param codeUrl
     * @throws IOException 
     * @throws SQLException 
     */
    public static void handler(String dbXlsUrl, ProjectInfo4MemCache projectInfo, DBType dbType) throws IOException
    {
        List<XlsTable> tables = getTables(dbXlsUrl);
        
        String tableConstantsUrl = FileUtil.appendPath(projectInfo.getConstantsPath(), TABLECONSTANTS_CLASS_NAME + ".java");
        TxtUtil.write(tableConstantsUrl, getConstantClassStr(projectInfo, tables));
        TxtUtil.write(FileUtil.appendPath(projectInfo.getFullServiceBeanUrl(), IDUTIL_CLASS_NAME), 
        		getIdUtilClassStr(projectInfo, tables));
        TxtUtil.write(projectInfo.getSqlUrl(), DBUtil.getSql(tables, dbType));
        
        writeMOClass(tables, projectInfo);
    }

    /**
     * 获取所有的表格信息
     * 首页有些会标注是否需要生成MO
     * 
     * @param xlsUrl
     * @param isCreateMO
     * @return
     */
    protected static List<XlsTable> getTables(String xlsUrl)
    {
        String[][][] arrays = Excel2007Util.read(xlsUrl);
        List<XlsTable> list = new ArrayList<XlsTable>();

        Map<String, String[]> map = new HashMap<String, String[]>();
        String[][] firstSheet = arrays[0];
        for (int i = 1, length = firstSheet.length; i < length; i++)
        {
            if (!Xls2CodeUtil.isEmptyLine(firstSheet[i]))
            {
                map.put(firstSheet[i][0], firstSheet[i]);
            }
        }

        for (int i = 1, length = arrays.length; i < length; i++)
        {
            String[][] tableArray = arrays[i];
            String name = Excel2007Util.getSheetName(xlsUrl, i);
            XlsTable table = new XlsTable(name, map.get(name));
            if (map.containsKey(name))
            {
                String[] temp = map.get(name);
                if (temp.length > 3 && "0".equals(temp[3]))
                {
                    table.setCreateMO(false);
                }
            }
            for (int j = 1, size = tableArray.length; j < size; j++)
            {
                String[] array = tableArray[j];
                if (!Xls2CodeUtil.isEmptyLine(array))
                {
                    table.addColumn(new XlsColumn(array));
                }
            }
            list.add(table);

        }
        return list;
    }
    
    /**
     * 生成IDUtil文件
     * 
     * @param tables
     * @return
     * @throws IOException
     */
    public static String getIdUtilClassStr(ProjectInfo4MemCache projectInfo, List<XlsTable> tables) throws IOException
    {
        StringBuilder idUtil = new StringBuilder();
        idUtil.append("package ").append(projectInfo.getServiceBeanPath()).append(Constants.SEMICOLON).append(Constants.ENTER);
        idUtil.append(Constants.ENTER);
        idUtil.append("public class IDUtil");
        idUtil.append(Constants.BLANK).append("{").append(Constants.ENTER);
        for (XlsTable table : tables)
        {
            if (table.getIdIndex() != null && !table.getIdIndex().isEmpty())
            {
                idUtil.append(Constants.GAP1).append("public static final int ").append(table.getName().toUpperCase())
                    .append(" = ").append(table.getIdIndex()).append(";").append(Constants.ENTER).append(Constants.ENTER);
            }
        }
        
        idUtil.append(Constants.GAP1).append("public static long getNextId(int type)");
        idUtil.append(Constants.BLANK).append("{").append(Constants.ENTER);
        idUtil.append(Constants.GAP2).append("Ids ids = IdsProxy.getIds(type);").append(Constants.ENTER);
        
        idUtil.append(Constants.GAP2).append("if (ids == null) ids = IdsProxy.createIds(type);").append(Constants.ENTER);
        idUtil.append(Constants.GAP2).append("synchronized (ids)");
        idUtil.append(Constants.BLANK).append("{").append(Constants.ENTER);
        idUtil.append(Constants.GAP3).append("long current = ids.getCurrent();").append(Constants.ENTER);
        idUtil.append(Constants.GAP3).append("ids.setCurrent(current + 1);").append(Constants.ENTER);
        idUtil.append(Constants.GAP3).append("return current;").append(Constants.ENTER);
        idUtil.append(Constants.GAP2).append("}").append(Constants.ENTER);
        idUtil.append(Constants.GAP1).append("}").append(Constants.ENTER);
        idUtil.append("}").append(Constants.ENTER);
        
        return idUtil.toString();
    }

    /**
     * 构造TableConstants文件
     * 
     * @param tables
     * @return
     * @throws IOException
     */
    public static String getConstantClassStr(ProjectInfo4MemCache projectInfo, Collection<XlsTable> tables) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.PACKAGE).append(Constants.BLANK).append(projectInfo.getConstantsUrl()).append(
                Constants.SEMICOLON).append(Constants.ENTER).append(Constants.ENTER);

        sb.append("import com.tuhanbao.util.db.table.CacheType;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.util.db.table.Column;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.util.db.table.ColumnFactory;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.io.impl.tableUtil.DataType;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.util.db.table.Table;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.util.db.table.TableRegister;").append(Constants.ENTER);
        sb.append(Constants.ENTER);
        sb.append(Xls2CodeUtil.getNotesInfo(Constants.CONSTANT_CLASS_NOTES, 0));
        sb.append(Constants.PUBLIC_CLASS).append(Constants.BLANK).append(TableUtil.TABLECONSTANTS_CLASS_NAME).append(
        		Constants.BLANK);
        sb.append(Constants.LEFT_BRACE).append(Constants.ENTER);

        sb.append(Constants.GAP1).append("public static final Column COUNT = ColumnFactory.createColumn(null, \"COUNT(1)\", DataType.INT);").append(Constants.ENTER);
        sb.append(Constants.ENTER);

        sb.append(Constants.GAP1).append("public static void init()");
        sb.append(Constants.BLANK).append(Constants.LEFT_BRACE).append(Constants.ENTER);
        for (XlsTable table : tables)
            sb.append(Constants.GAP2).append("TableRegister.register(").append(table.getName()).append(".TABLE);").append(
                    Constants.BLANK).append(Constants.ENTER);
        sb.append(Constants.ENTER);
        sb.append(Constants.GAP1).append(Constants.RIGHT_BRACE).append(Constants.ENTER);
        sb.append(Constants.ENTER);

        for (XlsTable t : tables)
        {
            sb.append(t.toString()).append(Constants.ENTER);
        }
        sb.append(Constants.RIGHT_BRACE);
        return sb.toString();
    }

    /**
     * 构造TableConstants文件
     * 
     * @param tables
     * @return
     * @throws IOException
     */
    public static String getConstantClassStr(String url, Collection<ImportTable> tables) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.PACKAGE).append(Constants.BLANK).append(url).append(
                Constants.SEMICOLON).append(Constants.ENTER).append(Constants.ENTER);
        
        sb.append("import java.util.HashMap;").append(Constants.ENTER);
        sb.append("import java.util.Map;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.util.util.clazz.ClazzUtil;").append(Constants.ENTER);

        sb.append("import com.tuhanbao.io.impl.tableUtil.DataType;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.util.db.table.CacheType;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.util.db.table.Column;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.util.db.table.ColumnFactory;").append(Constants.ENTER);
        sb.append("import com.tuhanbao.util.db.table.Table;").append(Constants.ENTER);
//        sb.append("import com.tuhanbao.util.db.table.TableRegister;").append(Constants.ENTER);
        sb.append(Constants.ENTER);
        sb.append(Xls2CodeUtil.getNotesInfo(Constants.CONSTANT_CLASS_NOTES, 0));
        sb.append(Constants.PUBLIC_CLASS).append(Constants.BLANK).append(TableUtil.TABLECONSTANTS_CLASS_NAME).append(
        		Constants.BLANK);
        sb.append(Constants.LEFT_BRACE).append(Constants.ENTER);

        sb.append(Constants.GAP1).append("public static final Column COUNT = ColumnFactory.createColumn(null, \"COUNT(1)\", DataType.INT);").append(Constants.ENTER);
        sb.append(Constants.ENTER);
        sb.append(Constants.GAP1).append("public static final Map<String, Table> TABLES = new HashMap<String, Table>();").append(Constants.ENTER);
        sb.append(Constants.ENTER);
        sb.append(Constants.GAP1).append("static { init(); }").append(Constants.ENTER);
        sb.append(Constants.ENTER);
        sb.append(Constants.GAP1).append("private TableConstants() {}").append(Constants.ENTER);
        sb.append(Constants.ENTER);

        sb.append(Constants.GAP1).append("public static final void register(Table table)");
        sb.append(Constants.BLANK).append("{").append(Constants.ENTER);
        sb.append(Constants.GAP2).append("TABLES.put(getClassName(table.getName()), table);").append(Constants.ENTER);
        sb.append(Constants.GAP1).append("}").append(Constants.ENTER);
        sb.append(Constants.ENTER);

        sb.append(Constants.GAP1).append("public static Table getTableByClassName(String name)");
        sb.append(Constants.BLANK).append("{").append(Constants.ENTER);
        sb.append(Constants.GAP2).append("return TABLES.get(name);").append(Constants.ENTER);
        sb.append(Constants.GAP1).append("}").append(Constants.ENTER);
        sb.append(Constants.ENTER);

        sb.append(Constants.GAP1).append("private static String getClassName(String tableName)");
        sb.append(Constants.BLANK).append("{").append(Constants.ENTER);
        sb.append(Constants.GAP2).append("if (tableName.startsWith(\"T_\") || tableName.startsWith(\"I_\"))");
        sb.append(Constants.BLANK).append("{").append(Constants.ENTER);
        sb.append(Constants.GAP3).append("tableName = tableName.substring(2);").append(Constants.ENTER);
        sb.append(Constants.GAP2).append("}").append(Constants.ENTER);
        sb.append(Constants.GAP2).append("return ClazzUtil.getClassName(tableName);").append(Constants.ENTER);
        sb.append(Constants.GAP1).append("}").append(Constants.ENTER);
        sb.append(Constants.ENTER);

        sb.append(Constants.GAP1).append("public static void init()");
        sb.append(Constants.BLANK).append(Constants.LEFT_BRACE).append(Constants.ENTER);
        for (ImportTable table : tables)
            sb.append(Constants.GAP2).append("register(").append(table.getTableName()).append(".TABLE);").append(
                    Constants.BLANK).append(Constants.ENTER);
        sb.append(Constants.GAP1).append(Constants.RIGHT_BRACE).append(Constants.ENTER);
        sb.append(Constants.ENTER);

        for (ImportTable t : tables)
        {
            sb.append(t.toString()).append(Constants.ENTER);
        }
        sb.append(Constants.RIGHT_BRACE);
        return sb.toString();
    }
    
    
    /**
     * 创建table的实体对象，MO和Proxy对象
     * 如果存在则不覆盖
     * 
     * @param tables
     * @param projectInfo
     * @throws IOException
     */
    public static void writeMOClass(List<XlsTable> tables, ProjectInfo4MemCache projectInfo) throws IOException
    {
        for (XlsTable table : tables)
        {
            if (table.isCreateMO())
            {
                //创建bean对象
                ClassInfo classInfo = toBeanClassInfo(projectInfo, table);
                String url = FileUtil.appendPath(projectInfo.getSrcPath(), 
                		Xls2JavaUtil.instance.getClassSrcUrl(classInfo));
                File f = new File(url);
                if (!f.exists())
                {
                    TxtUtil.write(url, classInfo.toString());
                }
                
                //创建MO对象
                classInfo = toMOClassInfo(projectInfo, table);
                url = FileUtil.appendPath(projectInfo.getSrcPath(), 
                		Xls2JavaUtil.instance.getClassSrcUrl(classInfo));
                TxtUtil.write(url, classInfo.toString());
                
                //创建proxy对象
                classInfo = toProxyClassInfo(projectInfo, table);
                url = FileUtil.appendPath(projectInfo.getSrcPath(), 
                		Xls2JavaUtil.instance.getClassSrcUrl(classInfo));
                f = new File(url);
                if (!f.exists())
                {
                    TxtUtil.write(url, classInfo.toString());
                }
            }
        }
    } 
    
    /**
     * 构造table的实体对象
     * @param table
     * @return
     */
    private static ClassInfo toBeanClassInfo(ProjectInfo4MemCache projectInfo, XlsTable table)
    {
        ClassInfo classInfo = new ClassInfo();
        String tableName = table.getName();
        String moClassName = getMOClassName(tableName);
        classInfo.setName(tableName + " extends " + moClassName);
        classInfo.setPackageInfo(projectInfo.getServiceBeanPath());
        for (String importInfo : BEANCLASS_IMPORTS)
        {
            classInfo.addImportInfo(importInfo);
        }
        classInfo.addImportInfo(projectInfo.getBaseBeanPath() + "." + getMOClassName(tableName));
        MethodInfo method = new MethodInfo();
        method.setArgs("MetaObject mo");
        method.setName(tableName);
        method.setPe(PackageEnum.PROTECTED);
        method.setMethodBody("super(mo);");
        classInfo.addMethodInfo(method);
        
        return classInfo;
    }

	/**
     * 构造MO对象
     * 
     * @param table
     * @return
     */
    private static ClassInfo toMOClassInfo(ProjectInfo4MemCache projectInfo, XlsTable table)
    {
        ClassInfo classInfo = new ClassInfo();
        String tableName = table.getName();
        String className = getMOClassName(tableName);
        classInfo.setName(className + " extends ServiceBean");
        classInfo.setPackageInfo(projectInfo.getBaseBeanPath());
        for (String importInfo : MOCLASS_IMPORTS)
        {
            classInfo.addImportInfo(importInfo);
        }
        classInfo.addImportInfo(getTableConstantsClassPath(projectInfo));
        
        MethodInfo method = new MethodInfo();
        method.setArgs("MetaObject mo");
        method.setName(className);
        method.setPe(PackageEnum.PROTECTED);
        method.setMethodBody("super(mo);");
        classInfo.addMethodInfo(method);
        
        boolean hasByteBuffer = false;

        for (XlsColumn col : table.getList())
        {
            String returnMethodType = col.getDataType().getDIYValue();
            classInfo.addImportInfo("com.tuhanbao.util.db.table.data." + returnMethodType);
            
            method = new MethodInfo();
            method.setName("get" + toMethodName(col.getColName()));
            method.setPe(PackageEnum.PUBLIC);
            method.setType(getReturnMethodType(col.getDataType()));
            StringBuilder methodBody = new StringBuilder();
            String methodType = getMethodType(col.getDataType());
            methodBody.append(methodType).append(" value = (").append(methodType).append(")getValue(TableConstants.").append(tableName).append(".").append(col.getName()).append(");").append(Constants.ENTER);
            methodBody.append(Constants.GAP2).append("if (value == null) return ").append(methodType).append(".NULL_VALUE;").append(Constants.ENTER);
            methodBody.append(Constants.GAP2).append("else return value.getValue();");
            method.setMethodBody(methodBody.toString());
            classInfo.addMethodInfo(method);
            
            if (!col.isPK())
            {
                method = new MethodInfo();
                method.setName("set" + toMethodName(col.getColName()));
                method.setPe(PackageEnum.PUBLIC);
                method.setType(VOID);
                method.setArgs(getReturnMethodType(col.getDataType()) + " value");
                method.setMethodBody("setValue(TableConstants." + tableName + "." + col.getName() + ", " + methodType + ".valueOf(value));");
                classInfo.addMethodInfo(method);
            }
            
            if (col.getFK() != null)
            {
                String[] fk = col.getFK();
                method = new MethodInfo();
                String moName = toMethodName(fk[0]);
                String fkName = toMethodName(fk[1]);
                method.setName("get" + fkName);
                method.setPe(PackageEnum.PUBLIC);
                method.setType(moName);
                method.setMethodBody("return " + moName + MANAGERCLASS_SUFFIX + ".get" + moName + "(get" +
                        toMethodName(col.getColName()) + "());");
                classInfo.addMethodInfo(method);
                classInfo.addImportInfo("com.threetorch.service.bean." + moName);
                classInfo.addImportInfo("com.threetorch.service.bean." + moName + MANAGERCLASS_SUFFIX);
            }
            
            if (col.getDataType() == DataType.BYTEARRAY) hasByteBuffer = true;
        }
        
        if (hasByteBuffer)
        {
            classInfo.addImportInfo("com.tuhanbao.util.ByteBuffer");
        }
        
        return classInfo;
    }
    
    private static String toMethodName(String s)
    {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    private static String getMethodType(DataType dataType)
    {
        return dataType.getDIYValue();
    }

    private static String getReturnMethodType(DataType dataType)
    {
        return dataType.getName();
    }
    
    private static String getMOClassName(String name)
    {
        return name + MOCLASS_SUFFIX;
    }

    /**
     * 构造proxy对象
     * @param table
     * @return
     */
    private static ClassInfo toProxyClassInfo(ProjectInfo4MemCache projectInfo, XlsTable table)
    {
        ClassInfo classInfo = new ClassInfo();
        String tableName = table.getName();
        String className = getManagerClassName(tableName);
        classInfo.setName(className);
        classInfo.setPackageInfo(projectInfo.getServiceBeanPath());
        classInfo.setFinal(true);
        for (String importInfo : MANAGERCLASS_IMPORTS)
        {
            classInfo.addImportInfo(importInfo);
        }
        classInfo.addImportInfo(getTableConstantsClassPath(projectInfo));
        classInfo.addImportInfo(projectInfo.getServiceBeanPath() + "." + tableName);
        
        //变量
//        private static final Table table = Tables.Player.TABLE;
        VarInfo var = new VarInfo();
        var.setName("table");
        var.setStatic(true);
        var.setFinal(true);
        var.setPe(PackageEnum.PRIVATE);
        var.setType("Table");
        var.setValue("TableConstants." + tableName + ".TABLE");
        classInfo.addVarInfo(var);
        
//        private PlayerManager()
//        {
//        }
        MethodInfo method = new MethodInfo();
        method.setName(className);
        method.setPe(PackageEnum.PRIVATE);
        classInfo.addMethodInfo(method);
        
//        public static User createUser(Map<Column, DataValue> properties)
//        {
//            MetaObject mo = MemCacheProxy.createMO(table, properties);
//            return getServiceBean(mo);
//        }
        
        method = new MethodInfo();
        method.setName("create" + tableName);
        method.setPe(PackageEnum.PUBLIC);
        method.setType(tableName);
        method.setStatic(true);
        method.setArgs("Map<Column, Object> properties");
        StringBuilder methodBody = new StringBuilder();
        methodBody.append("long id = IDUtil.getNextId(IDUtil." + tableName.toUpperCase() + ");\n");
        methodBody.append(Constants.GAP2).append("properties.put(TableConstants." + tableName + ".ID, id);\n");
        methodBody.append(Constants.GAP2).append("MetaObject mo = MemCacheProxy.createMO(table, properties);\n");
        methodBody.append(Constants.GAP2).append("return getServiceBean(mo);");
        method.setMethodBody(methodBody.toString());
        classInfo.addMethodInfo(method);
        
//        public static Player getPlayer(int key)
//        {
//            MetaObject mo = MemCacheProxy.getMO(table, IntValue.valueOf(key));
//            return getServiceBean(mo);
//        }
        method = new MethodInfo();
        String methodName = "get" + tableName;
        method.setName(methodName);
        method.setPe(PackageEnum.PUBLIC);
        method.setType(tableName);
        method.setStatic(true);
        method.setArgs(getPKArgs(table));
        methodBody = new StringBuilder();
        methodBody.append("if (key == LongValue.NULL_VALUE) return null;\n");
        methodBody.append(Constants.GAP2).append("MetaObject mo = MemCacheProxy.getMOByKey(table, " + getPKArgsStr(table, classInfo) + ");\n");
        methodBody.append(Constants.GAP2).append("return getServiceBean" + "(mo);");
        method.setMethodBody(methodBody.toString());
        classInfo.addMethodInfo(method);
        
//        private static Activity getServiceBean(MetaObject mo)
//        {
//            if (mo.getServiceBean() == null)
//            {
//                new Player(mo);
//            }
//            return (Activity)mo.getServiceBean();
//        }
        method = new MethodInfo();
        method.setName("getServiceBean");
        method.setPe(PackageEnum.PRIVATE);
        method.setType(tableName);
        method.setStatic(true);
        method.setArgs("MetaObject mo");
        methodBody = new StringBuilder();
        methodBody.append("if (mo == null) return null;").append(Constants.ENTER);
        methodBody.append(Constants.GAP2).append("if (mo.getServiceBean() == null)");
        methodBody.append(Constants.BLANK).append(Constants.LEFT_BRACE).append(Constants.ENTER);
        methodBody.append(Constants.GAP3 + "new " + tableName + "(mo);").append(Constants.ENTER);
        methodBody.append(Constants.GAP2).append(Constants.RIGHT_BRACE).append(Constants.ENTER);
        methodBody.append(Constants.GAP2).append("return (" + tableName + ")mo.getServiceBean();");
        method.setMethodBody(methodBody.toString());
        classInfo.addMethodInfo(method);

//        public static List<Player> getPlayer(SelectorFilter filter)
//        {
//            List<MetaObject> mos = MetaObjectCacheProxy.getMOs(table, filter);
//            List<Player> result = new ArrayList<Player>(mos.size());
//            int i = 0;
//            for (MetaObject mo : mos)
//            {
//                result.add(getServiceBean(mo));
//            }
//            
//            return result;
//        }
        method = new MethodInfo();
        method.setName(methodName);
        method.setPe(PackageEnum.PUBLIC);
        method.setType("List<" + tableName + ">");
        method.setStatic(true);
        method.setArgs("SelectorFilter filter");
        method.setMethodBody("List<MetaObject> mos = MemCacheProxy.getMOs(table, filter);" + Constants.ENTER
                + Constants.GAP2 + "List<" + tableName + "> result = new ArrayList<" + tableName + ">(mos.size());" + Constants.ENTER
                + Constants.GAP2 + "for (MetaObject mo : mos)" +  Constants.ENTER
                + Constants.GAP2 + "{" +  Constants.ENTER
                + Constants.GAP3 + "result.add(getServiceBean(mo));" + Constants.ENTER
                + Constants.GAP2 + "}" +  Constants.ENTER
                + Constants.GAP2 + "return result;" + Constants.ENTER);
        classInfo.addMethodInfo(method);
        
        
//        public static List<Player> getAllPlayer()
//        {
//            List<MetaObject> mos = MemCacheProxy.getMOs(table, null);
//            List<Player> list = new ArrayList<Player>();
//            for (MetaObject mo : mos)
//            {
//                list.add(getServiceBean(mo));
//            }
//            return list;
//        }
        method = new MethodInfo();
        method.setName("getAll" + tableName);
        method.setPe(PackageEnum.PUBLIC);
        method.setType("List<" + tableName + ">");
        method.setStatic(true);
        method.setMethodBody("List<MetaObject> mos = MemCacheProxy.getMOs(table, null);" + Constants.ENTER
                + Constants.GAP2 + "List<" + tableName + "> list = new ArrayList<" + tableName + ">();" + Constants.ENTER
                + Constants.GAP2 + "for (MetaObject mo : mos)" +  Constants.ENTER
                + Constants.GAP2 + "{" +  Constants.ENTER
                + Constants.GAP3 + "list.add(getServiceBean(mo));" +  Constants.ENTER
                + Constants.GAP2 + "}" +  Constants.ENTER
                + Constants.GAP2 + "return list;" + Constants.ENTER);
        classInfo.addMethodInfo(method);
        
        return classInfo;
    }
    
    private static String getPKArgs(XlsTable table)
    {
        for (XlsColumn c : table.getList())
        {
            if (c.isPK())
            {
                String returnMethodType = getReturnMethodType(c.getDataType());
                return returnMethodType + " key";
            }
        }
        
        return Constants.EMPTY;
    }
    
    private static String getPKArgsStr(XlsTable table, ClassInfo classInfo)
    {
        for (XlsColumn c : table.getList())
        {
            if (c.isPK())
            {
                String returnMethodType = c.getDataType().getDIYValue();
                classInfo.addImportInfo("com.tuhanbao.util.db.table.data." + returnMethodType);
                return returnMethodType + ".valueOf(key)";
            }
        }
        
        return Constants.EMPTY;
    }
    
    private static String getManagerClassName(String name)
    {
        return name + MANAGERCLASS_SUFFIX;
    }

    private static String getTableConstantsClassPath(
			ProjectInfo4MemCache projectInfo) {
		return StringUtil.appendStr(Constants.STOP_EN, projectInfo.getConstantsUrl(), TABLECONSTANTS_CLASS_NAME);
	}
}
