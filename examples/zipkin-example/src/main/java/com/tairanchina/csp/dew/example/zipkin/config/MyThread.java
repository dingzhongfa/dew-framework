package com.tairanchina.csp.dew.example.zipkin.config;

/**
 * Created by 迹_Jason on 2017/8/1.
 */
public class MyThread extends Thread {
    private int ticket = 5;

    public void run() {
        for (int i = 0; i < 30; i++) {
            if (ticket > 0) {
                ClientMultiThreadedExecution.get("http://192.168.111.224:8891/start",null);
            }
        }
    }
}

class ThreadDemo {
    public static void main(String[] args) {
        new MyThread().start();
        new MyThread().start();
        new MyThread().start();
    }
}
