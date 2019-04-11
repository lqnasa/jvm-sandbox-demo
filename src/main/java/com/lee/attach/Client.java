package com.lee.attach;

public class Client {

    /**
     *@param args
     */
    public static void main(String[] args) {
        HelloServiceImpl service = new HelloServiceImpl();
        while (true) {
            service.sayHello();
            try {
                synchronized (service) {
                    service.wait(3000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
