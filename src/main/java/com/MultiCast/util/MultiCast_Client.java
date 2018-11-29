package com.MultiCast.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.List;

public class MultiCast_Client {

    // 使用常量作为本程序的多点广播IP地址
    private static final String BROADCAST_IP = "230.0.0.1";
    // 使用常量作为本程序的多点广播目的的端口
    private static final int BROADCAST_PORT = 30000;

    private MulticastSocket socket;
    private InetAddress broadcastAddress = null;
    // 定义一个用于接收的DatagramPacket对象
    private DatagramPacket inPacket = null;

    private Boolean exit;

    private HashMap<Long,String> wordsMap;

    long i;

    private MultiCast_Client() {}

    private static class MultiCast_Client_Instance {
        private static final MultiCast_Client INSTANCE = new MultiCast_Client();
    }

    public static MultiCast_Client getInstance() {
        return MultiCast_Client_Instance.INSTANCE;
    }

    public void init(){
        i = 0;
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
        exit = true;
        System.out.println("stop receive!");
        try {
            socket.leaveGroup(broadcastAddress);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive(){
        new Thread(){
            @Override
            public void run() {
                wordsMap = new HashMap<>();
                while(!exit){
                    i++;
                    try{
                        byte[] buf = new byte[128]; //接收数据缓冲
                        inPacket = new DatagramPacket(buf, buf.length); //接收数据的数据报
                        socket.receive(inPacket); //接收数据
                        String word = new String(buf,"UTF-8");
                        System.out.println(word); //输出接收到的数据
                        Thread.sleep(500);
                        if (i<=50){
                            wordsMap.put(i,word);
                            System.out.println("just put: "+i+":"+word);
                        }else {
                            System.out.println("delete: "+(i-50)+":"+wordsMap.get(i-50));
                            wordsMap.remove(i-50);
                            wordsMap.put(i,word);
                            System.out.println("put: "+i+":"+word);
                            System.out.println("size: "+wordsMap.size());
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    public HashMap<Long , String> getWordsMap() {
        return wordsMap;
    }

    public static void main(String[] args) {
        MultiCast_Client m = new MultiCast_Client();
        m.init();
        m.receive();
//        m.stop();
    }

}
