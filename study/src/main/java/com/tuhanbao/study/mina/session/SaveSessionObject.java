package com.tuhanbao.study.mina.session;

import org.apache.mina.core.session.IoSession;

/**
 * 一般都是user或player对象需要实现此接口
 * 一个玩家对应一个session，需要在玩家对象中保留session的引用
 * 在删除session时，需要通知此类对象进行session删除
 * 
 * @author tuhanbao
 *
 */
public interface SaveSessionObject
{
    void removeSession();
    
    void setSession(IoSession session);
    
    IoSession getSession();
    
}
