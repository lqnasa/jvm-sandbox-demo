package com.lee.attach;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class MyAgent {
    
    public static String className = "com.lee.attach.HelloServiceImpl";

    public static void agentmain(String args, Instrumentation inst) throws UnmodifiableClassException {
        Class[] allClass = inst.getAllLoadedClasses();
        for (Class c : allClass) {
            if (c.getName().equals(className)) {
                System.out.println("agent loaded");
                inst.addTransformer(new DynamicClassTransformer(), true);
                inst.retransformClasses(c);
            }
        }
    }
}
