
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.log.LogManager;

public class ServerManager
{
    /**
     * 服务器启动
     */
    public static void start()
    {
        ConfigManager.init(null);
        LogManager.init(null);
    }
}
