package com.lee.attach;

import java.util.List;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;


public class AttachMain {

    public static void main(String args[]) throws AttachNotSupportedException {
        VirtualMachine vm;
        List<VirtualMachineDescriptor> vmList = VirtualMachine.list();
        if (vmList != null) {
            for (int i = 0; i < vmList.size(); i++) {
                System.out.println("[" + i + "]  " + vmList.get(i).displayName() + " ,id:" + vmList.get(i).id()
                        + " ,provider:" + vmList.get(i).provider());
            }
            try {
                int num = System.in.read() - 48;
                if (num != -1 && num < vmList.size()) {
                    vm = VirtualMachine.attach(vmList.get(num));
                    vm.loadAgent("/home/tanfeng/myagent.jar");
                    System.in.read();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
