package com.MultiCast.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MultiCast_Server {

    // 使用常量作为本程序的多点广播IP地址
    private static final String BROADCAST_IP = "230.0.0.1";
    // 使用常量作为本程序的多点广播目的的端口
    public static final int BROADCAST_PORT = 30000;

    private MulticastSocket socket;
    private InetAddress broadcastAddress = null;
    // 定义一个用于发送的DatagramPacket对象
    private DatagramPacket outPacket = null;

    public void send(String message){
        try {
            socket = new MulticastSocket(BROADCAST_PORT);
            broadcastAddress = InetAddress.getByName(BROADCAST_IP);
            socket.joinGroup(broadcastAddress);
            socket.setLoopbackMode(false);
            outPacket = new DatagramPacket(new byte[0],0,broadcastAddress,BROADCAST_PORT);
            outPacket.setData(null);
            socket.send(outPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }




}
