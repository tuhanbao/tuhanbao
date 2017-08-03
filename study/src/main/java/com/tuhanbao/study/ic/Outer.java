package com.tuhanbao.study.ic;

public class Outer {
    private String name;
    
    private class Inner {
        private void print() {
            System.out.println(name);
        }
    }
    
    public void test() {
        new Inner().print();
    }
    
}
