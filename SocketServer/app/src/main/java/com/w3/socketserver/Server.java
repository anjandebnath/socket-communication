package com.w3.socketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Server {

	MainActivity activity;
	ServerSocket serverSocket;
	String message = "";
	static final int socketServerPORT = 8080;

	public Server(MainActivity activity) {
		this.activity = activity;
		Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
	}

	public int getPort() {
		return socketServerPORT;
	}

	public void onDestroy() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class SocketServerThread extends Thread {

		int count = 0;

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(socketServerPORT);

				while (true) {
					Socket socket = serverSocket.accept();
					count++;

					message += rcvMsg(socket);


					OutputStream outputStream;
					String messagetoSend = "Hello from Server, you are #" + count;
					try {
						outputStream = socket.getOutputStream();
						PrintStream printStream = new PrintStream(outputStream);
						printStream.print(messagetoSend);
						printStream.close();

						message += "replayed: " + messagetoSend + "\n";

						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								activity.msg.setText(message);
							}
						});

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						message += "Something wrong! " + e.toString() + "\n";
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								activity.msg.setText(message);
							}
						});
					}


					/*activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							activity.msg.setText(message+"\n");
						}
					});

					SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(
							socket, count);
					socketServerReplyThread.run();*/

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class SocketServerReplyThread extends Thread {

		private Socket hostThreadSocket;
		int cnt;

		SocketServerReplyThread(Socket socket, int c) {
			hostThreadSocket = socket;
			cnt = c;
		}

		@Override
		public void run() {

			String msgReply = "Hello from Server, you are #" + cnt;




			sendMessage(msgReply, hostThreadSocket);

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					activity.msg.setText(message);
				}
			});
		}

	}

	private void sendMessage(String messagetoSend, Socket socket){

		OutputStream outputStream;

		try {
			outputStream = socket.getOutputStream();
			PrintStream printStream = new PrintStream(outputStream);
			printStream.print(messagetoSend);
			printStream.close();

			message += "replayed: " + messagetoSend + "\n";

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					activity.msg.setText(message);
				}
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message += "Something wrong! " + e.toString() + "\n";
		}

	}

	private String rcvMsg(Socket socket) throws IOException {

		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String result = br.readLine() + " from "
				+ socket.getInetAddress() + ":"
				+ socket.getPort() + "\n";

		return result;
	}

	/*private String receiveMessage(Socket socket) throws IOException {

		String mResponse = null;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];

		int bytesRead;
		InputStream inputStream = socket.getInputStream();


		//notice: inputStream.read() will block if no data return

		while ((bytesRead = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, bytesRead);
			mResponse = byteArrayOutputStream.toString("UTF-8");
			mResponse+= " from "
					+ socket.getInetAddress() + ":"
					+ socket.getPort() + "\n";
		}

		return mResponse;

	}*/

	public String getIpAddress() {
		String ip = "";
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces
						.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface
						.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress
							.nextElement();

					if (inetAddress.isSiteLocalAddress()) {
						ip += "Server running at : "
								+ inetAddress.getHostAddress();
					}
				}
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}
		return ip;
	}
}
