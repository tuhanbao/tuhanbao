package com.sztx.se.rpc.dubbo.registry;



import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.zookeeper.ZookeeperRegistryFactory;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter;
import com.tuhanbao.base.util.objutil.ReflectUtil;

public class SeZookeeperRegistryFactory extends ZookeeperRegistryFactory {

	public static final String EXTENSION_NAME = "zookeeper";
	
	@Override
	public Registry createRegistry(URL url) {
		ZookeeperTransporter zookeeperTransporter =  (ZookeeperTransporter) ReflectUtil.getFieldValue(this, "zookeeperTransporter");
        return new SeZookeeperRegistry(url, zookeeperTransporter);
    }
}
