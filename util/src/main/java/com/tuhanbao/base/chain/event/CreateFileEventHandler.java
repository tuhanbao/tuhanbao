package com.tuhanbao.base.chain.event;

import java.io.IOException;

import com.tuhanbao.base.chain.IEventHandler;
import com.tuhanbao.base.util.io.txt.TxtUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;

public class CreateFileEventHandler implements IEventHandler<CreateFileEvent> {

    private static final String BAK = ".bak";
    
    @Override
    @SuppressWarnings("deprecation")
    public void handle(CreateFileEvent t) {
        try {
            String url = t.getUrl();
            OverwriteStrategy os = t.getOs();
            if (os == OverwriteStrategy.ADD) {
                TxtUtil.write(url, t.getText(), true);
                return;
            }
            else if (FileUtil.isExists(url)) {
                if (os == OverwriteStrategy.NEVER_COVER) {
                    //不写
                    return;
                }
                else if (os == OverwriteStrategy.BAK) {
                    //写一个备份
                    TxtUtil.write(url + BAK, t.getText());
                    return;
                }
                else if (os == OverwriteStrategy.MEGER) {
                    //暂未实现...有点难
                    return;
                }
            }
            
            //其他情况一律覆盖
            TxtUtil.write(url, t.getText());
        }
        catch (IOException e) {
            LogManager.error(e);
        }
    }

}
