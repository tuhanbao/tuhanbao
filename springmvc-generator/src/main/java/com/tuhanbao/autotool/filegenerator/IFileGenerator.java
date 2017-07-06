package com.tuhanbao.autotool.filegenerator;

import java.util.List;

import com.tuhanbao.base.chain.IEvent;

/**
 * 文本生成器
 * 
 * java代码，配置文件等一切自动生成文件的接口
 * @author Administrator
 *
 */
public interface IFileGenerator {
    //生成的文本
    List<IEvent> getFileBean(Object args);
}
