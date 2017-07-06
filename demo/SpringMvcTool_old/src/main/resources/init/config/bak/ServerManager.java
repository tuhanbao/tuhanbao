package {projectHead}.impl.{projectName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.tuhanbao.web.IServerManager;
{mapperImport}

public class ServerManager implements ApplicationListener<ContextRefreshedEvent> {
    private static boolean isInit = false;
    	
    @Autowired
    private IServerManager serverManger;
    
{mapper}
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        init();
    }
    
    public void init() {
        if (!isInit) {
            //初始化redis缓存
{redis}

            if (serverManger != null) serverManger.init();
            isInit = true;
        }
    }
    
}
