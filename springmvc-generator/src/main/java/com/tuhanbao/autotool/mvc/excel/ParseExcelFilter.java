package com.tuhanbao.autotool.mvc.excel;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.autotool.mvc.ProjectConfig;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.util.io.excel.Excel2007Util;
import com.tuhanbao.base.util.objutil.StringUtil;

@FilterAnnotation("parseExcel")
public class ParseExcelFilter extends ExcelAGCFilter {

    @Override
    public void filter(Context context) {
        SpringMvcProjectInfo project = new SpringMvcProjectInfo(ProjectConfig.PROJECT_URL, ProjectConfig.PROJECT_NAME, ProjectConfig.PROJECT_HEAD);
        context.putAttr(PROJECT, project);
        
        //读取所有配置文件
        context.putAttr(CONFIG, initProjectByConfig(StringUtil.string2Array(ProjectConfig.CONFIG_URL)));
    }
    
    private Map<String, String[][]> initProjectByConfig(String ... configUrl) {
        Map<String, String[][]> configs = new HashMap<String, String[][]>();
        
        for (String url : configUrl) {
            int i = 0;
            for (String[][] arrays : Excel2007Util.read(url)) {
                configs.put(Excel2007Util.getSheetName(url, i++), arrays);
            }
        }
        return configs;
    }

}
