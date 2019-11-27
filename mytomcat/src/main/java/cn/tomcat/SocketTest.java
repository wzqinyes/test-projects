package cn.tomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTest {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket socket = serverSocket.accept();

        InputStream inputStream = socket.getInputStream();
        byte[] b = new byte[inputStream.available()];
        inputStream.read(b);

        System.out.println(new String(b, "UTF-8"));

        OutputStream outputStream = socket.getOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        ps.println("HTTP/1.1 200 OK");
        ps.println("Content-Type:text/html;charset=UTF-8");
        ps.println();
        ps.println("<html><body><h1>Hello World!</h1></body><html>");
        ps.flush();
        ps.close();

        inputStream.close();
        socket.close();
        serverSocket.close();
    }
}
