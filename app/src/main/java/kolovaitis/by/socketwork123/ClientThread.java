package kolovaitis.by.socketwork123;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private boolean finished = false;
    private static final int SERVER_PORT = 4444;
    private String data;
    private static final String SERVER_IP = "46.101.96.234";

    @Override
    public void run() {
        try {
            //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            socket = new Socket(SERVER_IP, SERVER_PORT);
            data = "";
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            while (finished==false) {
                try {
                    Thread.sleep(1000);

                        data = reader.readLine();
                        if (data == null) {
                            data = "Server is not ready";
                            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        }


                } catch (Exception e) {
                    data="Error";
                }
            }
            socket.close();
        } catch (IOException e) {
            data ="Connection failed";
        }
    }

    public boolean getFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getCurrentData() throws IOException {
        return data;
    }


}
