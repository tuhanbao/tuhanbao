
import com.tuhanbao.util.config.ConfigManager;
import com.tuhanbao.util.log.LogManager;

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
