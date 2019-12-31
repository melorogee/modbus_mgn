package com.xwtec.modules.switchcontrol.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Test {
    public static void main(String[] args) throws Exception{
//        sendBySocket("12 05 00 00 00 00 CF 69","192.168.0.101",28899);

    }


    public static String sendBySocket(String text, String ip, int port)
            throws Exception {
        Socket socket = new Socket(ip, port);
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "GBK");
        // 2.获取客户端输出流
        OutputStream dos = socket.getOutputStream();
        // 3.向服务端发送消息
        dos.write(text.getBytes());
        dos.flush();
        // 4.获取输入流，并读取服务器端的响应信息
        BufferedReader br = new BufferedReader(isr);
        String returnInfo = br.readLine();
        // 4.关闭资源
        br.close();
        isr.close();
        is.close();
        dos.close();
        socket.close();
        return returnInfo;
    }
}