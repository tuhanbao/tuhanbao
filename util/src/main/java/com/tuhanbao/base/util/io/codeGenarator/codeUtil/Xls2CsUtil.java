package com.tuhanbao.base.util.io.codeGenarator.codeUtil;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumVarInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.NotesInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.VarInfo;
import com.tuhanbao.base.util.objutil.StringUtil;

public class Xls2CsUtil extends Xls2CodeUtil
{
    public static final Xls2CsUtil instance = new Xls2CsUtil();
    
    private Xls2CsUtil()
    {
    }
    
    /**
     * cs不是以单个enum来写文件的，这里具体实现放在Xls2Enum实现
     * @param enumInfo
     * @return
     */
    @Override
    public String getEnumStr(EnumClassInfo enumInfo)
    {
        return null;
    }
    
    @Override
    public String getClassStr(ClassInfo classInfo)
    {
        return null;
    }
    
    /**
     * 把一个变量信息转换成c#代码
     */
    public String getVarInfoStr(VarInfo var)
    {
        return null;
    }
    
    /**
     * 吧一个枚举变量信息转换成java代码
     */
    public String getEnumVarInfoStr(EnumVarInfo var)
    {
        StringBuilder sb = new StringBuilder();
        String noteStr = getNoteStr(var.getNote());
        if (!StringUtil.isEmpty(noteStr)) sb.append(noteStr).append(Constants.GAP1);
        sb.append(var.getName().toUpperCase()).append(" = ");
        sb.append(var.getValue()).append(Constants.COMMA);
        return sb.toString();
    }

    /**
     * 将代码路径转换为包路径
     * @param url
     * @return
     */
    @Override
    public String getClassSrcUrl(ClassInfo classInfo)
    {
        return classInfo.getPackageInfo().replaceAll("\\.", "/") + "/" + classInfo.getClassName() + ".cs";
    }
    
    @Override
    public String getNoteStr(NotesInfo note)
    {
        if (note == null || note.getList() == null)
        {
            return Constants.EMPTY;
        }
        else 
        {
            String prefix = getGap(note.getGap());
            if (note.getList().size() == 1)
            {
                return "//" + note.getList().get(0) + Constants.ENTER;
            }
            else
            {
                StringBuilder sb = new StringBuilder();
                sb.append(prefix + "/// <summary>").append(Constants.ENTER);
                for (String s : note.getList())
                {
                    sb.append(prefix + "/// ").append(s).append(Constants.ENTER);
                }
                sb.append(prefix + "/// </summary>").append(Constants.ENTER);
                return sb.toString();
            }
        }
    }
}
