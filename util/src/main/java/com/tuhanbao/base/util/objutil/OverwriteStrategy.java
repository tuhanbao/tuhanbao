package com.tuhanbao.base.util.objutil;

public enum OverwriteStrategy {
    //当源文件存在时不做任何处理
    NEVER_COVER(0), 
    
    //当源文件存在时覆盖
    COVER(1), 
    
    //党源文件存在时生成一个bak文件
    BAK(2),
    
    //融合  TODO 暂未实现
    @Deprecated
    MEGER(3),
    
    //从文件末尾继续写
    ADD(4);
    
    public int value;
    
    private OverwriteStrategy(int value) {
        this.value = value;
    }
    
    public static OverwriteStrategy getOverwriteStrategy(int value) {
        for (OverwriteStrategy temp : OverwriteStrategy.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }
}
