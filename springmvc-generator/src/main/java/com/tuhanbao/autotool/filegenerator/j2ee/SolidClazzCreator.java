package com.tuhanbao.autotool.filegenerator.j2ee;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.filegenerator.IFileGenerator;
import com.tuhanbao.autotool.mvc.ModuleManager;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.IEvent;
import com.tuhanbao.base.chain.event.CreateFileEvent;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.config.ConfigPattern;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.db.table.CacheType;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DBType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.io.txt.TxtUtil;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.thirdapi.cache.CacheManager;
import com.tuhanbao.web.filter.MyBatisSelectorFilter;
import com.tuhanbao.web.filter.SelectorFactory;

public class SolidClazzCreator implements IFileGenerator {

    private static String ORACLE_CUT_PAGE = "";
    private static String MYSQL_CUT_PAGE = "";

    private Map<String, String> REPLACE_MAP = new HashMap<String, String>();

    private Map<String, String> BASE_MAP = new HashMap<String, String>();

    private List<SolidObject> solidObjects;

    private List<ImportTable> tables;

    private SpringMvcProjectInfo project;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("  <sql id=\"CutPagePrefix\" >").append(Constants.ENTER);
        sb.append("    <if test=\"page != null\" >").append(Constants.ENTER);
        sb.append("      select * from ( select row_.*, rownum rownum_ from ( ").append(Constants.ENTER);
        sb.append("    </if>").append(Constants.ENTER);
        sb.append("  </sql>").append(Constants.ENTER);
        sb.append("  <sql id=\"CutPageSuffix\" >").append(Constants.ENTER);
        sb.append("    <if test=\"page != null\" >").append(Constants.ENTER);
        sb.append("      <![CDATA[ ) row_ ) where rownum_ > #{page.begin} and rownum_ <= #{page.end} ]]>").append(Constants.ENTER);
        sb.append("    </if>").append(Constants.ENTER);
        sb.append("  </sql>").append(Constants.ENTER);
        ORACLE_CUT_PAGE = sb.toString();

        sb = new StringBuilder();
        sb.append("  <sql id=\"CutPagePrefix\" ></sql>").append(Constants.ENTER);
        sb.append("  <sql id=\"CutPageSuffix\" >").append(Constants.ENTER);
        sb.append("    <if test=\"page != null\" >").append(Constants.ENTER);
        sb.append("      <![CDATA[ limit #{page.begin}, #{page.numPerPage} ]]>").append(Constants.ENTER);
        sb.append("    </if>").append(Constants.ENTER);
        sb.append("  </sql>").append(Constants.ENTER);
        MYSQL_CUT_PAGE = sb.toString();
    }

    public SolidClazzCreator(SpringMvcProjectInfo project, List<ImportTable> tables, List<SolidObject> solidObjects) {
        this.project = project;
        this.tables = tables;
        this.solidObjects = solidObjects;
        BASE_MAP.put("{config}", project.getConfigUrl());
        BASE_MAP.put("{impl}", project.getImplUrl());
        BASE_MAP.put("{res}", project.getResUrl());
        BASE_MAP.put("{controller}", project.getControllerUrl(null));
        BASE_MAP.put("{projectName}", project.getProjectName());
        BASE_MAP.put("{projectHead}", project.getPackageHead());
        REPLACE_MAP.put("{db}", getSpringDBXml());
        REPLACE_MAP.put("{serverManager}",
                Constants.GAP1 + "<bean class=\"" + project.getPackageHead() + ".impl." + project.getProjectName() + ".ServerManager\"></bean>");
        REPLACE_MAP.putAll(BASE_MAP);
    }

    public List<IEvent> getFileBean(Object args) {
        List<IEvent> list = new ArrayList<>();
        for (Entry<String, DBSrc> entry : ModuleManager.getModules().entrySet()) {
            String module = entry.getKey();
            DBType dbType = entry.getValue().getDbType();
            list.add(getMapperClass(module));
            list.add(getMapperXml(module, dbType));
            list.add(getServiceImpl(module, dbType));
        }
        list.add(getServerManager(tables));
        list.addAll(getDBConfigProperties());

        try {
            for (SolidObject so : solidObjects) {
                list.add(writeFile(so));
            }
        }
        catch (IOException e) {
            throw MyException.getMyException(e);
        }

        return list;
    }

    private CreateFileEvent writeFile(SolidObject so) throws IOException {
        String targetUrl = replace(so.getTargetUrl(), BASE_MAP);
        String key = so.getKey();
        String url = FileUtil.appendPath(project.getRootPath(), FileUtil.appendPath(targetUrl.replace(".", Constants.FILE_SEP), key));
        String oldTxt = TxtUtil.read(new File(FileUtil.appendPath(Constants.CONFIG_ROOT, "bak" + so.getUrl(), key)));
        return new CreateFileEvent(url, replace(oldTxt, REPLACE_MAP), so.getOverwriteStrategy());
    }

    private static String replace(String s, Map<String, String> map) {
        for (Entry<String, String> entry : map.entrySet()) {
            s = s.replace(entry.getKey(), entry.getValue());
        }
        return s;
    }

    private CreateFileEvent getMapperXml(String module, DBType dbType) {
        try {
            String mapperPath = ((SpringMvcProjectInfo)this.project).getMapperUrl(module);
            String mapperName = getMapperName(module);
            String url = FileUtil.appendPath(project.getSrcPath(),
                    FileUtil.appendPath(mapperPath.replace(".", Constants.FILE_SEP), mapperName + ".xml"));
            String oldTxt = TxtUtil.read(new File(FileUtil.appendPath(Constants.CONFIG_ROOT, "bak", "Mapper.xml")));

            String mapper = mapperPath + Constants.STOP_EN + mapperName;
            String newTxt = oldTxt.replace("{mapper}", mapper).replace("{mapper.name}", mapperName);

            if (dbType == DBType.ORACLE) {
                newTxt = newTxt.replace("{cutpage}", ORACLE_CUT_PAGE);
                newTxt = newTxt.replace("{mysql_insert}", "");
            }
            else {
                newTxt = newTxt.replace("{cutpage}", MYSQL_CUT_PAGE);
                newTxt = newTxt.replace("{mysql_insert}", "useGeneratedKeys=\"true\" keyProperty=\"keyValue\" ");
            }

            return new CreateFileEvent(url, newTxt);
        }
        catch (IOException e) {
            throw MyException.getMyException(e);
        }
    }

    private CreateFileEvent getMapperClass(String module) {
        try {
            String mapperPath = ((SpringMvcProjectInfo)this.project).getMapperUrl(module);
            String mapperName = getMapperName(module);

            String url = FileUtil.appendPath(project.getSrcPath(),
                    FileUtil.appendPath(mapperPath.replace(".", Constants.FILE_SEP), mapperName + ".java"));

            String oldTxt = TxtUtil.read(new File(FileUtil.appendPath(Constants.CONFIG_ROOT, "bak", "Mapper.java")));
            String newTxt = oldTxt.replace("{package}", mapperPath).replace("{mapper.name}", mapperName);

            return new CreateFileEvent(url, newTxt);
        }
        catch (IOException e) {
            throw MyException.getMyException(e);
        }
    }

    private String getMapperName(String module) {
        String mapperName = null;
        int lastIndex = module.lastIndexOf(".");
        if (lastIndex != -1) {
            mapperName = ClazzUtil.getClassName(module.substring(lastIndex + 1));
        }
        else {
            mapperName = ClazzUtil.getClassName(module);
        }
        mapperName += "Mapper";
        return mapperName;
    }

    private CreateFileEvent getServiceImpl(String module, DBType dbType) {
        try {
            String servicePath = ((SpringMvcProjectInfo)this.project).getServiceUrl(module);
            String mapperName = this.getMapperName(module);
            String url = FileUtil.appendPath(project.getSrcPath(),
                    FileUtil.appendPath(servicePath.replace(".", Constants.FILE_SEP), "ServiceImpl.java"));

            String mapper = ((SpringMvcProjectInfo)this.project).getMapperUrl(module) + Constants.STOP_EN + mapperName;
            String tableConstants = ((SpringMvcProjectInfo)this.project).getConstantsUrl() + Constants.STOP_EN + "TableConstants";

            String oldTxt = TxtUtil.read(new File(FileUtil.appendPath(Constants.CONFIG_ROOT, "bak", "ServiceImpl.java")));
            String newTxt = oldTxt.replace("{package}", ((SpringMvcProjectInfo)this.project).getServiceUrl(module));
            newTxt = newTxt.replace("{tableConstants}", tableConstants);
            newTxt = newTxt.replace("{mapper}", mapper).replace("{mapper.name}", mapperName);

            return new CreateFileEvent(url, newTxt);

        }
        catch (IOException e) {
            throw MyException.getMyException(e);
        }
    }

    private List<CreateFileEvent> getDBConfigProperties() {
        List<CreateFileEvent> list = new ArrayList<CreateFileEvent>();

        for (Entry<ConfigPattern, Map<String, DBSrc>> moduleEntry : ModuleManager.getAllModules().entrySet()) {
            Map<String, DBSrc> allDebugModules = moduleEntry.getValue();
            ConfigPattern cp = moduleEntry.getKey();
            String url = FileUtil.appendPath(project.getRootPath(), ((SpringMvcProjectInfo)this.project).getConfigUrl(), cp.getPath(), 
                    "db.properties");
            
            StringBuilder sb = new StringBuilder();
            for (Entry<String, DBSrc> entry : allDebugModules.entrySet()) {
                String module = entry.getKey();
                DBSrc src = entry.getValue();
                sb.append(getModuleStr1("db_url", module)).append("=").append(src.getUrl()).append(Constants.ENTER);
                sb.append(getModuleStr1("db_user", module)).append("=").append(src.getUser()).append(Constants.ENTER);
                sb.append(getModuleStr1("db_password", module)).append("=").append(src.getPassword()).append(Constants.ENTER);
                sb.append(Constants.ENTER);
            }
            list.add(new CreateFileEvent(url, sb.toString()));
        }

        return list;
    }

    private String getSpringDBXml() {

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, DBSrc> entry : ModuleManager.getModules().entrySet()) {
            String module = entry.getKey();
            String mapper = ((SpringMvcProjectInfo)this.project).getMapperUrl(module);

            String srcName = getModuleStr1("ds", module);
            String sqlSessionName = getModuleStr2("SqlSessionFactory", module);
            String tmName = getModuleStr2("TransactionManager", module);
            sb.append(Constants.GAP1).append("<bean id=\"").append(srcName).append("\" class=\"com.alibaba.druid.pool.DruidDataSource\">")
                    .append(Constants.ENTER);
            sb.append(Constants.GAP2).append("<property name=\"url\" value=\"${").append(getModuleStr1("db_url", module)).append("}\"></property>")
                    .append(Constants.ENTER);
            sb.append(Constants.GAP2).append("<property name=\"username\" value=\"${").append(getModuleStr1("db_user", module))
                    .append("}\"></property>").append(Constants.ENTER);
            sb.append(Constants.GAP2).append("<property name=\"password\" value=\"${").append(getModuleStr1("db_password", module))
                    .append("}\"></property>").append(Constants.ENTER);
            sb.append(Constants.GAP1).append("</bean>").append(Constants.ENTER);

            sb.append(Constants.GAP1).append("<bean id=\"").append(sqlSessionName).append("\" class=\"org.mybatis.spring.SqlSessionFactoryBean\">")
                    .append(Constants.ENTER);
            sb.append(Constants.GAP2).append("<property name=\"dataSource\" ref=\"").append(srcName).append("\" />").append(Constants.ENTER);
            sb.append(Constants.GAP2).append("<property name=\"configurationProperties\">").append(Constants.ENTER);
            sb.append(Constants.GAP2).append("<props>").append(Constants.ENTER);
            sb.append(Constants.GAP3).append("<prop key=\"jdbcTypeForNull\">NULL</prop>").append(Constants.ENTER);
            sb.append(Constants.GAP2).append("</props>").append(Constants.ENTER);
            sb.append(Constants.GAP2).append("</property>").append(Constants.ENTER);
            sb.append(Constants.GAP1).append("</bean>").append(Constants.ENTER);

            sb.append(Constants.GAP1).append("<bean class=\"org.mybatis.spring.mapper.MapperScannerConfigurer\">").append(Constants.ENTER);
            sb.append(Constants.GAP2).append("<property name=\"basePackage\" value=\"").append(mapper).append("\" />").append(Constants.ENTER);
            sb.append(Constants.GAP2).append("<property name=\"sqlSessionFactoryBeanName\" value=\"").append(sqlSessionName).append("\"></property>")
                    .append(Constants.ENTER);
            sb.append(Constants.GAP1).append("</bean>").append(Constants.ENTER);

            sb.append(Constants.GAP1).append("<bean id=\"").append(tmName)
                    .append("\" class=\"org.springframework.jdbc.datasource.DataSourceTransactionManager\">").append(Constants.ENTER);
            sb.append(Constants.GAP2).append("<property name=\"dataSource\" ref=\"").append(srcName).append("\"></property>").append(Constants.ENTER);
            sb.append(Constants.GAP1).append("</bean>").append(Constants.ENTER);
            sb.append(Constants.GAP1).append("<tx:annotation-driven transaction-manager=\"").append(tmName).append("\" />").append(Constants.ENTER);
        }
        return sb.toString();
    }

    private CreateFileEvent getServerManager(List<ImportTable> tables) {
        try {
            String url = FileUtil.appendPath(project.getSrcPath(), ((SpringMvcProjectInfo)this.project).getImplUrl().replace(".", Constants.FILE_SEP),
                    "ServerManager.java");

            String oldTxt = TxtUtil.read(new File(FileUtil.appendPath(Constants.CONFIG_ROOT, "bak", "ServerManager.java")));
            StringBuilder mapper = new StringBuilder();
            StringBuilder importMapper = new StringBuilder();
            StringBuilder redis = new StringBuilder();

            List<String> modules = new ArrayList<String>();
            for (ImportTable table : tables) {
                if (table.getCacheType() == CacheType.CACHE_ALL) {
                    String module = table.getModule();
                    if (!modules.contains(module)) modules.add(module);
                    String mapperName = getMapperName(module);

                    redis.append(Constants.GAP3).append("if (!CacheManager.hasCacheDataGroup(TableConstants.").append(table.getName())
                            .append(".TABLE)) {").append(Constants.ENTER);
                    redis.append(Constants.GAP4).append("CacheManager.save(").append(ClazzUtil.firstCharLowerCase(mapperName))
                            .append(".select(new MyBatisSelectorFilter(SelectorFactory.getTablesSelector(TableConstants.").append(table.getName())
                            .append(".TABLE))));").append(Constants.ENTER);
                    redis.append(Constants.GAP3).append("}").append(Constants.ENTER);
                }
            }

            for (String module : modules) {
                String mapperPath = ((SpringMvcProjectInfo)this.project).getMapperUrl(module);
                String mapperName = getMapperName(module);
                importMapper.append("import ").append(mapperPath).append(".").append(mapperName).append(";").append(Constants.ENTER);
                mapper.append(Constants.GAP1).append("@Autowired").append(Constants.ENTER);
                mapper.append(Constants.GAP1).append("private ").append(mapperName).append("<ServiceBean>").append(Constants.BLANK)
                        .append(ClazzUtil.firstCharLowerCase(mapperName)).append(";").append(Constants.ENTER);
            }

            if (!modules.isEmpty()) {
                importMapper.append("import ").append(SelectorFactory.class.getName()).append(";").append(Constants.ENTER);
                importMapper.append("import ").append(MyBatisSelectorFilter.class.getName()).append(";").append(Constants.ENTER);
                importMapper.append("import ").append(ServiceBean.class.getName()).append(";").append(Constants.ENTER);
                importMapper.append("import ").append(CacheManager.class.getName()).append(";").append(Constants.ENTER);
                importMapper.append("import com.hhnz.api.{projectName}.constants.TableConstants;").append(Constants.ENTER);
            }
            String newTxt = oldTxt.replace("{mapperImport}", importMapper.toString());
            newTxt = newTxt.replace("{mapper}", mapper.toString());
            newTxt = newTxt.replace("{redis}", redis.toString());
            newTxt = newTxt.replace("{projectName}", project.getProjectName());
            newTxt = newTxt.replace("{projectHead}", ((SpringMvcProjectInfo)this.project).getPackageHead());

            return new CreateFileEvent(url, newTxt);
        }
        catch (IOException e) {
            throw MyException.getMyException(e);
        }
    }

    private static String getModuleStr1(String s, String module) {
        if (StringUtil.isEmpty(module)) {
            return s;
        }
        else return s + "_" + module;
    }

    private static String getModuleStr2(String s, String module) {
        if (StringUtil.isEmpty(module)) {
            return ClazzUtil.firstCharLowerCase(s);
        }
        else return module + s;
    }
}
