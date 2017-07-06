package com.tuhanbao.base.util.io.codeGenarator.classUtil;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.io.codeGenarator.CodeType;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.CodeUtilManager;

/**
 * 不考虑内部类
 * @author tuhanbao
 *
 */
public class VarInfo
{
    private PackageEnum pe;
    
    private boolean isStatic = false;
    
    private boolean isFinal = false;
    
    protected String type;
    
    protected String value;
    
    protected String name;

    protected NotesInfo note;
    
    private List<String> annotations;
    
    @Override
    public String toString()
    {
        return CodeUtilManager.getCodeUtil(CodeType.JAVA).getVarInfoStr(this);
    }

    public PackageEnum getPe()
    {
        return pe;
    }

    public void setPe(PackageEnum pe)
    {
        this.pe = pe;
    }

    public boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(boolean isStatic)
    {
        this.isStatic = isStatic;
    }

    public boolean isFinal()
    {
        return isFinal;
    }

    public void setFinal(boolean isFinal)
    {
        this.isFinal = isFinal;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        if (value == null || Constants.EMPTY.equals(value))
        {
            this.isFinal = false;
            this.value = null;
        }
        else
        {
            this.value = value;
        }
    }

    public NotesInfo getNote()
    {
        return note;
    }

    public void setNote(NotesInfo note)
    {
        this.note = note;
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

	public List<String> getAnnotations() {
		return annotations;
	}
	
	public void addAnnotation(String annotation) {
		if (this.annotations == null) this.annotations = new ArrayList<String>();
		this.annotations.add(annotation);
	}
}
