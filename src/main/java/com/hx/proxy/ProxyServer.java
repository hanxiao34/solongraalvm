package com.hx.proxy;

import com.hx.config.Config;

import javax.net.ServerSocketFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.hx.proxy.Common.*;

/**
 * Open a SSL TCP port and provide the HTTP Proxy service.
 */
public class ProxyServer extends Thread {

    public static void main(String[] args) throws Exception {



//        setDefaultProperties("javax.net.ssl.keyStore", "keystore");
//        setDefaultProperties("javax.net.ssl.keyStorePassword", "changeit");
//        setDefaultProperties("javax.net.ssl.trustStore", "keystore");
//        setDefaultProperties("javax.net.ssl.trustStorePassword", "changeit");

        ServerSocket ss = ServerSocketFactory.getDefault()
                .createServerSocket(9998);
        ss.setReceiveBufferSize(1024 * 1024);
        while (true) {
            Socket s = ss.accept();
            System.out.println("xxxxxxxxxxxxx");
            new ProxyServer(s).start();
        }
    }


    Socket cSock;
    Socket sSock;
    InputStream cin;
    OutputStream cout;

    public ProxyServer(Socket s) {
        this.cSock = s;
    }

    String getHost(String command) {
        int start = command.indexOf("//");
        if (start <= 0) {
            throw new IllegalArgumentException();
        }
        start += 2;
        int end = command.indexOf("/", start + 1);
        if (end < 0) {
            end = command.length();
        }
        return command.substring(start, end);
    }

    public void run() {

        try {
            cin = new BufferedInputStream(cSock.getInputStream());
            cout = cSock.getOutputStream();

            String firstLine = readLine(cin);

            if (firstLine.toUpperCase().startsWith("CONNECT")) {
                doConnect(firstLine);
            } else {
                doProxy(firstLine);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                cSock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doProxy(String firstLine)
            throws UnknownHostException, IOException {
        System.out.println("doProxy:"+firstLine);
        String host = Config.proxyserver;
        int port = Config.proxyport;
//        int ind = host.indexOf(':');
//        if (ind > 0) {
//            port = Integer.valueOf(host.substring(ind));
//
//            host = host.substring(0, ind);
//        }
        sSock = new Socket(host, port);
        sSock.setReceiveBufferSize(1024 * 1024);
        InputStream sin = sSock.getInputStream();
        OutputStream sout = sSock.getOutputStream();

        sout.write(firstLine.replaceFirst("http:\\/\\/[^\\/]+","").getBytes());

        doTransfer(sin, sout);
    }

    private void doConnect(String firstLine)
            throws IOException {
        String hostPort = getConnectHostPort(firstLine);
        int ind = hostPort.indexOf(':');
        String host = hostPort.substring(0, ind);
        int port = Integer.valueOf(hostPort.substring(ind + 1));
        sSock = new Socket(host, port);
        sSock.setReceiveBufferSize(1024 * 1024);
        InputStream sin = sSock.getInputStream();
        OutputStream sout = sSock.getOutputStream();

        while (!readLine(cin).trim().isEmpty())
            ;
        cout.write("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
        cout.flush();

        doTransfer(sin, sout);

    }

    private void doTransfer(InputStream sin, OutputStream sout) {
        concurrent(
                () -> {
                    try {
                        transfer(cin, sout);
                    } catch (IOException e) {

                        close();
                    }
                },
                () -> {
                    try {
                        transfer(sin, cout);
                    } catch (IOException e) {

                        close();
                    }
                }

        );
    }

    String getConnectHostPort(String command) {
        return command.split("\\s")[1];
    }

    void close() {
        closeAll(cSock, sSock);
    }

}