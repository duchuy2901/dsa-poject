package ChatFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ClientChatHandler extends Thread {
    public Socket socket;
    public String name;
    private PrintWriter out;
    private BufferedReader in;

    public ClientChatHandler(Socket socket) {
        this.socket = socket;
    }
}
