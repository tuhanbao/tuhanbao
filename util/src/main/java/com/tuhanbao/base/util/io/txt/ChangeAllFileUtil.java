package com.tuhanbao.base.util.io.txt;

import java.io.File;
import java.io.IOException;

public abstract class ChangeAllFileUtil
{
    private ChangeTxtUtil changeTxtUtil = new ChangeTxtUtil(){

        @Override
        protected String change(String s)
        {
            return changeString(s);
        }
        
    };
    
    public abstract boolean checkFile(File file);

    public abstract String changeString(String s);
    
    public void change(File file) throws IOException
    {
        if (!file.exists()) return;
        if (file.isDirectory())
        {
            for (File f : file.listFiles())
            {
                change(f);
            }
        }
        else if (file.isFile() && checkFile(file))
        {
            changeTxtUtil.change(file.getPath(), file.getPath());
        }
    }
}
