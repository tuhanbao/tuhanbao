import com.tuhanbao.autotool.AGCContext;
import com.tuhanbao.autotool.mvc.ProjectConfig;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.util.config.ConfigManager;
import com.tuhanbao.web.controller.helper.MyPropertyPlaceholderConfigurer;

//TODO ,  主键序列，分页，redis, 地区，枚举，常量（配置文件）
//
//update 
public class Test {
	
	public static void main(String args[]) {
	    
	    MyPropertyPlaceholderConfigurer.start();
	    
	    Context context = new AGCContext(ConfigManager.getConfig("solid"));
	    context.addFilterByName(ProjectConfig.FILTERS);
	    context.start();
//		ClassCreatorUtil.startCreateProject(ConfigManager.getConfig("solid"));
//	    System.out.println(HtmlGeneratorFactory.getHtmlString(new Component()));
	}
	
}
