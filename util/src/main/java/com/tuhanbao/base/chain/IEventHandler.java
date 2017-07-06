package com.tuhanbao.base.chain;

public interface IEventHandler<T extends IEvent> {
    void handle(T t);
}
