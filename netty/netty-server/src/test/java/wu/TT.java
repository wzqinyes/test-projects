package wu;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class TT {


    public static void main(String[] args) throws IOException {

        System.out.println(Integer.MAX_VALUE/1024/1024);
        ServerSocket serverSocket = new ServerSocket(9909);//1
        //Socket clientSocket = serverSocket.accept();

        Map<String, String> map = new HashMap<String, String>(){
            {
                put("a", "aa");
                put("b", "bb");
            }
        };

        System.out.println(map.get("a"));

    }
}
