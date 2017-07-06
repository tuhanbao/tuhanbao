package com.tuhanbao.base.util.io.codeGenarator.classUtil;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.io.codeGenarator.CodeType;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.CodeUtilManager;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;


public class ClassInfo 
{
    private String packageInfo;
    
    private List<String> importInfos = new ArrayList<String>();
    
    private NotesInfo notes;
    
    private List<String> annotations;
    
    private List<MethodInfo> methodInfos = new ArrayList<MethodInfo>();

    //不仅包含名称，还包含extends,implements等其他所有信息(位于public class和{之间的所有东西)
    private String name;
    
    private List<VarInfo> varInfos = new ArrayList<VarInfo>();
    
    private boolean isFinal = false;
    
    private boolean isStatic = false;
    
    private boolean isAbstract= false;
    
    private boolean isInterface = false;
    
    private List<ClassInfo> innerClass;
    
    //静态代码块
    private String staticStr;
    
    //默认为public
    private PackageEnum pe = PackageEnum.PUBLIC;

    public void setFinal(boolean isFinal)
    {
        this.isFinal = isFinal;
    }

    public void setStatic(boolean isStatic)
    {
    	this.isStatic = isStatic;
    }

    public void setAbstract(boolean isAbstract)
    {
    	this.isAbstract = isAbstract;
    }
    
    public void setInterface(boolean isInterface)
    {
    	this.isInterface = isInterface;
    }

    public String getPackageInfo()
    {
        return packageInfo;
    }

    public void setPackageInfo(String packageInfo)
    {
        this.packageInfo = packageInfo;
    }

    public List<String> getImportInfo()
    {
        return importInfos;
    }

    public void addImportInfo(String importInfo)
    {
        String[] iis = StringUtil.string2Array(importInfo);
        for (String ii : iis) {
            if (!this.importInfos.contains(ii)) {
                this.importInfos.add(ii);
            }
        }
    }

    public void addImportInfo(Class<?> clazz) {
        this.addImportInfo(clazz.getName());
    }

    public NotesInfo getNotes()
    {
        return notes;
    }

    public void setNotes(NotesInfo notes)
    {
        this.notes = notes;
    }

    public List<MethodInfo> getMethodInfos()
    {
        return methodInfos;
    }

    public void addMethodInfo(MethodInfo methodInfo)
    {
        this.methodInfos.add(methodInfo);
        if (this.isInterface) {
        	methodInfo.setInterfaceMethod(true);
        }
    }
    
    public boolean constainsMethodName(String name) {
    	for (MethodInfo methodInfo : methodInfos) {
    		if (methodInfo.getName().equals(name)) {
    			return true;
    		}
    	}
    	return false;
    }

    public List<VarInfo> getVarInfos()
    {
        return varInfos;
    }

    public void addVarInfo(VarInfo varInfo)
    {
        this.varInfos.add(varInfo);
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setPE(PackageEnum pe) {
    	this.pe = pe;
    }

    public void setName(String name)
    {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getClassName()
    {
        int indexOf = this.name.indexOf(Constants.BLANK);
        if (indexOf != -1)
        {
            return this.name.substring(0, indexOf);
        }
        return name;
    }
    
    public void addInnerClass(ClassInfo clazz) {
    	if (innerClass == null) innerClass = new ArrayList<ClassInfo>();
    	this.innerClass.add(clazz);
    }

    public VarInfo getVarInfo(String type)
    {
        for (VarInfo var : varInfos)
        {
            if (var.getName().equals(type))
            {
                return var;
            }
        }
        return null;
    }

	public List<String> getAnnotations() {
		return annotations;
	}
	
	public void addAnnotation(String annotation) {
		if (this.annotations == null) this.annotations = new ArrayList<String>();
		this.annotations.add(annotation);
	}

	public List<String> getImportInfos() {
		return importInfos;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public List<ClassInfo> getInnerClass() {
		return innerClass;
	}

	public PackageEnum getPe() {
		return pe;
	}
	
	public String toString() {
		return CodeUtilManager.getCodeUtil(CodeType.JAVA).getClassStr(this);
	}
	
	public String getPath() {
		return FileUtil.appendPath(this.getPackageInfo().replace(".", Constants.FILE_SEP), this.getClassName() + ".java");
	}
	
	public void setStaticStr(String str) {
		this.staticStr = str;
	}
	
	public String getStaticStr() {
		return this.staticStr;
	}
}
