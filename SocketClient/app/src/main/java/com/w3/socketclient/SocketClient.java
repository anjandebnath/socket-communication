package com.w3.socketclient;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class SocketClient {

    private static final int BUFFER_SIZE = 1024;

    private String mAddress;
    private int mPort;
    private int mCount;

    private String mResponse;
    private Socket mSocket;
    MainActivity mainActivity;

    public SocketClient(String addrs, int port, int count, MainActivity activity){

        this.mAddress = addrs;
        this.mPort = port;
        this.mCount = count;
        this.mainActivity = activity;

        Thread socketClientThread = new Thread(new SocketClientSenderThread(mCount));
        socketClientThread.start();

    }

    public void onDestroy() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private class SocketClientSenderThread extends Thread{


        int count = 0;
        Socket senderSocket;


        public SocketClientSenderThread(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            try {
                senderSocket = new Socket(mAddress, mPort);

                String msgToSend = "Hello " + count;
                //Send the message to the server
                OutputStream os = senderSocket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);



                String sendMessage = msgToSend + "\n";
                bw.write(sendMessage);
                bw.flush();
                System.out.println("Message sent to the server : "+sendMessage);

                //Get the return message from the server
                InputStream is = senderSocket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String message = br.readLine();
                System.out.println("Message received from the server : " +message);


                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.response.setText(message);
                    }
                });




            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.response.setText(e.getMessage());
                    }
                });
                System.out.println("Message Exception : " +e.getMessage());
            }finally {
                onDestroy();
            }
        }

    }

}
