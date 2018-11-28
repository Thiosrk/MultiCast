package com.MultiCast.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class MultiCast_Server {

    // 使用常量作为本程序的多点广播IP地址
    private static final String BROADCAST_IP = "230.0.0.1";
    // 使用常量作为本程序的多点广播目的的端口
    private static final int BROADCAST_PORT = 30000;

    private MulticastSocket socket;
    private InetAddress broadcastAddress = null;
    // 定义一个用于发送的DatagramPacket对象
    private DatagramPacket outPacket = null;

    private Boolean exit;


        private MultiCast_Server() {}

        private static class MultiCast_Server_Instance {
            private static final MultiCast_Server INSTANCE = new MultiCast_Server();
        }

        public static MultiCast_Server getInstance() {
            return MultiCast_Server_Instance.INSTANCE;
        }

    public void init(){
        System.out.println("server init!");
        exit = false;
        try {
            socket = new MulticastSocket(BROADCAST_PORT);
            broadcastAddress = InetAddress.getByName(BROADCAST_IP);
            socket.joinGroup(broadcastAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop(){
        System.out.println("stop server!");
//        Thread.currentThread().interrupt();
        exit = true;
        try {
            String message = "stop server!";
            byte[] buf = message.getBytes("UTF-8"); //发送信息
//            socket.setLoopbackMode(false);
            outPacket = new DatagramPacket(buf,buf.length,broadcastAddress,BROADCAST_PORT);
            socket.send(outPacket);
            socket.leaveGroup(broadcastAddress);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message){
        System.out.println("loop send");
        new Thread(){

            @Override
            public void run() {
//                Scanner s = new Scanner(System.in);
                while(!exit){
                    try {
                        byte[] buf = message.getBytes("UTF-8"); //发送信息
//            socket.setLoopbackMode(false);
                        outPacket = new DatagramPacket(buf,buf.length,broadcastAddress,BROADCAST_PORT);
                        socket.send(outPacket);
                        Thread.sleep(1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();

    }

    public static void main(String[] args) {
        MultiCast_Server m = MultiCast_Server.getInstance();
//        m.init();
//        m.send();
        m.stop();
    }




}
