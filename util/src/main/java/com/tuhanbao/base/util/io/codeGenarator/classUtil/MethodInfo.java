package com.tuhanbao.base.util.io.codeGenarator.classUtil;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.Constants;

public class MethodInfo
{
    private PackageEnum pe;
    
    private boolean isStatic = false;
    
    private boolean isFinal = false;
    
    private boolean isAbstract = false;

    private boolean isInterfaceMethod = false;
    
    private String type;
    
    private String args;

    private NotesInfo note;
    
    private String methodBody;
    
    private String name;
    
    private String exceptions;
    
    private List<String> annotations;
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if (note != null)
        {
            sb.append(note).append(Constants.ENTER);
        }
        
        if (annotations != null) {
        	for (String annotation : this.annotations) {
        		sb.append(Constants.TAB_SPACE).append(annotation).append(Constants.ENTER);	
        	}
        }
        
        sb.append(Constants.TAB_SPACE);
        
        if (!isInterfaceMethod) {
        	
        	if (pe != null)
        	{
        		sb.append(pe.name().toLowerCase()).append(Constants.BLANK);
        	}
        	
        	if (isStatic)
        	{
        		sb.append(Constants.STATIC).append(Constants.BLANK);
        	}
        	
        	if (isAbstract)
        	{
        		sb.append(Constants.ABSTRACT).append(Constants.BLANK);
        	}
        	
        	if (isFinal)
        	{
        		sb.append(Constants.FINAL).append(Constants.BLANK);
        	}
        }
        
        if (type != null)
        {
            sb.append(type).append(Constants.BLANK);
        }
        sb.append(name).append(Constants.LEFT_PARENTHESE);
        
        if (args != null)
        {
            sb.append(args);
        }
        sb.append(Constants.RIGHT_PARENTHESE);
        if (exceptions != null) {
        	sb.append(Constants.BLANK).append(Constants.THROWS).append(Constants.BLANK).append(exceptions);
        }
        
        if (isAbstract || isInterfaceMethod) {
        	sb.append(Constants.SEMICOLON).append(Constants.ENTER);
        }
        else {
	        sb.append(Constants.BLANK).append(Constants.LEFT_BRACE).append(Constants.ENTER);
	        
	        if (methodBody != null)
	        {
	            sb.append(getPrefix(2));
	            sb.append(methodBody);
	        }
	        
	        sb.append(Constants.ENTER).append(getPrefix(1)).append(Constants.RIGHT_BRACE).append(Constants.ENTER);
        }
        return sb.toString();
    }
    
    private static String getPrefix(int gap)
    {
        String s = Constants.EMPTY;
        for (int i = 0; i < gap; i++)
        {
            s += Constants.TAB_SPACE;
        }
        
        return s;
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

    public boolean isInterfaceMethod() {
		return isInterfaceMethod;
	}

	public void setInterfaceMethod(boolean isInterfaceMethod) {
		this.isInterfaceMethod = isInterfaceMethod;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getArgs()
    {
        return args;
    }

    public void setArgs(String args)
    {
        this.args = args;
    }

    public NotesInfo getNote()
    {
        return note;
    }

    public void setNote(NotesInfo note)
    {
        this.note = note;
    }

    public String getMethodBody()
    {
        return methodBody;
    }

    public void setMethodBody(String methodBody)
    {
        this.methodBody = methodBody.trim();
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	public List<String> getAnnotations() {
		return annotations;
	}
	
	public void addAnnotation(String annotation) {
		if (this.annotations == null) this.annotations = new ArrayList<String>();
		this.annotations.add(annotation);
	}
}
