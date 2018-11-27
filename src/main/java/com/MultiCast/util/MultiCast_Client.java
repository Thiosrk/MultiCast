package com.MultiCast.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MultiCast_Client {

    // 使用常量作为本程序的多点广播IP地址
    private static final String BROADCAST_IP = "230.0.0.1";
    // 使用常量作为本程序的多点广播目的的端口
    private static final int BROADCAST_PORT = 30000;

    private MulticastSocket socket;
    private InetAddress broadcastAddress = null;
    // 定义一个用于接收的DatagramPacket对象
    private DatagramPacket inPacket = null;

    public void init(){
        try {
            socket = new MulticastSocket(BROADCAST_PORT);
            broadcastAddress = InetAddress.getByName(BROADCAST_IP);
            socket.joinGroup(broadcastAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void receive(){
        new Thread(){

            @Override
            public void run() {
                while(true){
                    try{
                        byte[] buf = new byte[128]; //接收数据缓冲
                        inPacket = new DatagramPacket(buf, buf.length); //接收数据的数据报
                        socket.receive(inPacket); //接收数据
                        System.out.println(new String(buf,"UTF-8")); //输出接收到的数据
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        MultiCast_Client m = new MultiCast_Client();
        m.init();
        m.receive();
    }

}
