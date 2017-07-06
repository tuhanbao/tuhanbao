package com.tuhanbao.base.chain.event;

import com.tuhanbao.base.chain.IEvent;
import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;

public class CreateFileEvent implements IEvent {
    private String url;
    
    private String text;
    
    private static int num = 1;
    
    private OverwriteStrategy os;

    public CreateFileEvent(ClassInfo clazz, ProjectInfo project) {
        this(clazz, project, OverwriteStrategy.COVER);
    }

    public CreateFileEvent(ClassInfo clazz, ProjectInfo project, OverwriteStrategy os) {
        this(getFullPath(project, clazz), clazz.toString(), os);
    }
    
    public CreateFileEvent(String url, String text) {
        this(url, text, OverwriteStrategy.COVER);
    }
    
    public CreateFileEvent(String url, String text, OverwriteStrategy os) {
        if (url != null && url.contains("db_debug")) {
            LogManager.info(null);
        }
        
        if (os == null) os = OverwriteStrategy.COVER;
        this.url = url;
        this.text = text;
        this.os = os;
        
        num++;
        if (num > 1000) {
            LogManager.info(num + "  " + url);
        }
    }
    
    
    private static String getFullPath(ProjectInfo project, ClassInfo clazz) {
        return FileUtil.appendPath(project.getSrcPath(), clazz.getPath());
    }

    public String getUrl() {
        return url;
    }

    public OverwriteStrategy getOs() {
        return os;
    }

    public String getText() {
        return text;
    }
}
