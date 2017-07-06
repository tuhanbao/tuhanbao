package com.tuhanbao.autotool.mvc.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.tuhanbao.autotool.AGCContext;
import com.tuhanbao.autotool.filegenerator.j2ee.SolidClazzCreator;
import com.tuhanbao.autotool.filegenerator.j2ee.SolidObject;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.objutil.ArrayUtil;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;
import com.tuhanbao.base.util.objutil.StringUtil;

@FilterAnnotation("solid")
public class SolidFileGeneratorFilter extends ExcelAGCFilter {

    @Override
    public void filter(Context context) {
              //生成init/config/bak下的文件，包括applicationContext-config,db,mapper,serverManager,serviceImpl等
        SolidClazzCreator solidClazzCreator = new SolidClazzCreator(this.getProject(context), this.getTables(context),
                getSolidObject(((AGCContext)context).getSolidConfig()));
        
        context.addEvent(solidClazzCreator.getFileBean(null));
    }
    
    private static List<SolidObject> getSolidObject(Config config) {
        List<SolidObject> list = new ArrayList<SolidObject>();
        if (config == null) return list;
        
        for (Entry<String, String> entry : config.getProperties().entrySet()) {
            String key = entry.getKey();
            String[] array = StringUtil.string2Array(entry.getValue());
            
            String url = ArrayUtil.indexOf(array, 0);
            String targetUrl = ArrayUtil.indexOf(array, 1);
            int needOverwrite = OverwriteStrategy.COVER.value;
            String needOverwriteStr = ArrayUtil.indexOf(array, 2);
            if (!StringUtil.isEmpty(needOverwriteStr)) {
                needOverwrite = Integer.valueOf(needOverwriteStr);
            }
            list.add(new SolidObject(key, url, targetUrl, needOverwrite));
        }
        
        return list;
    }

}
