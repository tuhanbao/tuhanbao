package com.tuhanbao.test;

public class ITest {
    public void info(String msg) {
        System.out.println(msg);
    }
    


    public void error(Throwable e) {
        e.printStackTrace();
    }
}
