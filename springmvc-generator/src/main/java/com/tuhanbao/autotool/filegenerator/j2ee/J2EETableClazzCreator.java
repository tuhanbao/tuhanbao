package com.tuhanbao.autotool.filegenerator.j2ee;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.autotool.filegenerator.ClazzCreator;
import com.tuhanbao.autotool.mvc.J2EETable;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.IEvent;
import com.tuhanbao.base.chain.event.CreateFileEvent;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;

public abstract class J2EETableClazzCreator extends ClazzCreator {
	
	protected SpringMvcProjectInfo project;
	
	protected OverwriteStrategy os;
	
    public J2EETableClazzCreator(SpringMvcProjectInfo project) {
		this(project, OverwriteStrategy.COVER);
	}

    public J2EETableClazzCreator(SpringMvcProjectInfo project, OverwriteStrategy os) {
        super(project);
        this.project = project;
        this.os = os;
    }
    
    
    public List<IEvent> getFileBean(Object args) {
        ClassInfo clazz = toClazz(args);
        CreateFileEvent fb = new CreateFileEvent(clazz, project, this.os);
        ArrayList<IEvent> list = new ArrayList<>();
        list.add(fb);
        return list;
    }


    @Override
    public ClassInfo toClazz(Object args) {
        if (!(args instanceof J2EETable)) {
            throw new MyException("args must be a j2eeTable");
        }
        return this.table2Class((J2EETable)args);
    }

    public abstract ClassInfo table2Class(J2EETable table);
}
