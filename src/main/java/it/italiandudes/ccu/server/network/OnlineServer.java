package it.italiandudes.ccu.server.network;

import it.italiandudes.ccu.server.Server;
import it.italiandudes.idl.common.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("unused")
public final class OnlineServer {

    //Singleton
    private static OnlineServer instance;

    //Constructors
    private OnlineServer(int port){
        Thread onlineServerThread = new Thread(new OnlineServerClassThread(port));
        onlineServerThread.setDaemon(true);
        onlineServerThread.start();
    }

    //Methods
    @SuppressWarnings("InstantiationOfUtilityClass")
    public static OnlineServer getInstance(){
        if(instance ==null)
            instance = new OnlineServer(Server.getConnectionPort());
        return instance;
    }
    public static boolean isInstantiated(){
        return instance !=null;
    }

    //Thread Class
    private static final class OnlineServerClassThread implements Runnable {

        //Attributes
        private final ServerSocket serverSocket;

        //Constructors
        public OnlineServerClassThread(int port){
            try {
                serverSocket = new ServerSocket(port);
            }catch (IllegalArgumentException illegalArgumentException){
                throw new RuntimeException("Can't connect to server: invalid port number, must be between 0 and 65535, inclusive.", illegalArgumentException);
            }catch (IOException ioException){
                throw new RuntimeException("Connection error.", ioException);
            }
        }

        //Thread Method
        @Override @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            while(true){
                Socket incomingConnection;
                try {
                    incomingConnection = serverSocket.accept();
                    new Thread(new LoginHandler(incomingConnection)).start();
                }catch (IOException e){
                    Logger.log(e);
                }
            }
        }

    }
}