package com.lee.attach;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class DynamicClassTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        try {
            CtClass ctClass = ClassPool.getDefault().get("com.lee.attach.HelloServiceImpl");
            String methodName = "sayHello";
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
            System.out.println(ctMethod.getName());
            ctMethod.insertBefore("System.out.println(\" sayHello\");");
            ctClass.writeFile();
            return ctClass.toBytecode();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


}
