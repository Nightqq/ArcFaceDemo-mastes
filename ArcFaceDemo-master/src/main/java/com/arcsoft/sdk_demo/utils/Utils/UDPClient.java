package com.arcsoft.sdk_demo.utils.Utils;

import android.content.Intent;
import android.util.Log;

import com.arcsoft.sdk_demo.utils.Activity.UDPUpDateActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2018/4/16.
 */

public class UDPClient implements Runnable {
    final static int udpPort = 11111;
    final static String hostIp = "192.168.0.245";
    private static DatagramSocket socket = null;
    private static DatagramPacket packetSend,packetRcv;
    private boolean udpLife = true; //udp生命线程
    private byte[] msgRcv = new byte[1024]; //接收消息

    public UDPClient(){
        super();
    }

    //返回udp生命线程因子是否存活
    public boolean isUdpLife(){
        if (udpLife){
            return true;
        }

        return false;
    }

    //更改UDP生命线程因子
    public void setUdpLife(boolean b){
        udpLife = b;
    }

    //发送消息
    public String send(String msgSend){
        InetAddress hostAddress = null;

        try {
            //hostAddress = InetAddress.getByName(hostIp);
            InetSocketAddress SerAddr = new InetSocketAddress("192.168.0.245",11111); //设置服务的IP地址和端口
            Socket socket = new Socket();
            socket.connect(SerAddr);
        } catch (UnknownHostException e) {
            Log.i("udpClient","未找到服务器");
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

/*        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            Log.i("udpClient","建立发送数据报失败");
            e.printStackTrace();
        }*/

        //packetSend = new DatagramPacket(msgSend.getBytes() , msgSend.getBytes().length,hostAddress,udpPort);


//        try {
//            socket.send(packetSend);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.i("udpClient","发送失败"+e.getMessage());
//        }
        //   socket.close();
        return msgSend;
    }

    @Override
    public void run() {

        try {
            socket = new DatagramSocket(11111);
            socket.setSoTimeout(3000);//设置超时为3s
        } catch (SocketException e) {
            Log.i("udpClient","建立接收数据报失败");
            e.printStackTrace();
        }
        packetRcv = new DatagramPacket(msgRcv,msgRcv.length);
        while (udpLife){
            try {
                Log.i("udpClient", "UDP监听");
                socket.receive(packetRcv);
                Log.i("消息来源的IP与port",packetRcv.getSocketAddress().toString()+""+packetRcv.getPort());
                String RcvMsg = new String(packetRcv.getData(),packetRcv.getOffset(),packetRcv.getLength());
                //将收到的消息发给主界面
                Intent RcvIntent = new Intent();
                RcvIntent.setAction("udpRcvMsg");
                RcvIntent.putExtra("udpRcvMsg", RcvMsg);
                UDPUpDateActivity.context.sendBroadcast(RcvIntent);

                Log.i("Rcv",RcvMsg);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        Log.i("udpClient","UDP监听关闭");
        socket.close();
    }
}
