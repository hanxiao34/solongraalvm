package com.hx.proxy;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

public class Proxy2 {

    private static int port=80;
    private static String host="127.0.0.1";
    private static String CL="Content-Length: ";

    public static  void startProxy(String h,int p){
        host=h;
        port=p;

        ServerSocket serverSocket = null;
        System.out.println("start proxy server to :"+host+":"+p);
        try {

            serverSocket = new ServerSocket(9999);
            for (; ; ) {
                new SocketHandle(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    static class SocketHandle extends Thread {

        private Socket socket;

        public SocketHandle(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String id= UUID.randomUUID().toString();
            OutputStream clientOutput = null;
            InputStream clientInput = null;
            Socket proxySocket = null;
            InputStream proxyInput = null;
            OutputStream proxyOutput = null;
            try {
                clientInput = socket.getInputStream();
                clientOutput = socket.getOutputStream();
//                System.out.println("proxy://"+ host +":"+port);
//                StringBuilder headStr = new StringBuilder();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(clientInput));
//                while(reader.ready()) {
//                    String line = reader.readLine();
//                    headStr.append(line+"\r\n");
//                    if (line.length()==0){
//                        break;
//                    }
//                }

                proxySocket = new Socket(host, port);

                proxyInput = proxySocket.getInputStream();
                proxyOutput = proxySocket.getOutputStream();
                System.out.println("=================");
                System.out.println("proxy "+id+" "+host+":"+port);
//                proxyOutput.write(headStr.toString().getBytes());
                int i=0;
                byte[] buffer=new byte[1024];
//                proxyOutput.write('\r');
//                proxyOutput.write('\n');
                boolean ishead=true;
                while ((i = clientInput.read(buffer)) != -1) {
                    proxyOutput.write(buffer);

                    if (ishead) {
                        String l=new String(buffer);
                        ishead=false;
                        String[] split = l.split("\r\n");
                        System.out.println(split[0]);
                        System.out.println(split[1]);
                    }
                    if (i == 0 || i < buffer.length) {

                        break;
                    }
                }

                //新开线程转发客户端请求至目标服务器
//                new ProxyHandleThread(clientInput, proxyOutput).start();
                //转发目标服务器响应至客户端
//                buffer=new byte[1024];
//                while ((i = proxyInput.read(buffer)) != -2) {
////                    String c=new String(buffer);
//
////                    System.out.println(c);
//                    clientOutput.write(buffer);
////                    if (c.contains(CL)){
////                        c=c.substring(c.indexOf(CL)+CL.length());
////                        if (c.contains("\r\n")){
////                            c=c.substring(0,c.indexOf("\r\n"));
////                        }
////                        System.out.println("c length====="+c);
////                        int l= Integer.parseInt(c);
////                        buffer=new byte[l];
////                    }
//                    sleep(0);
//                }
                while (true) {
                    clientOutput.write(proxyInput.read());
//                    Thread.sleep(0);
                }
            }catch (SocketException E){
//                E.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (proxyInput != null) {
                    try {
                        proxyOutput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (proxyOutput != null) {
                    try {
                        proxyOutput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (proxySocket != null) {
                    try {
                        proxySocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (clientInput != null) {
                    try {
                        clientInput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (clientOutput != null) {
                    try {
                        clientOutput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("request finsh:"+id);
            }

        }
    }
}
