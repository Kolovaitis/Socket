package kolovaitis.by.socketwork123;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
public static ArrayList<String>results=new ArrayList<>();
    static TextView text;
    private static ClientThread clientThread;

    private static final int SLEEP_TIME = 1000;


    private static final int READ_ATTEMPTS = 10;
MyTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.task);
task=new MyTask();
        new MainTask().execute();

    }
    public void reuse(){
try{        text.setText(results.get(results.size() - 1));}catch (ArrayIndexOutOfBoundsException e){}

        new MyTask().execute();
    }
    public void exit(View v){
        results.clear();
        text.setText("");
        finish();
    }
    public void history(View v){
        ScrollView scroll=new ScrollView(this);
        LinearLayout linear=new LinearLayout(this);
        linear.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(linear);
        for(int i=0;i<results.size();i++){
            TextView textView=new TextView(this);
            textView.setText(results.get(i));
            textView.setGravity(Gravity.CENTER);
            linear.addView(textView);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("History")

                .setView(scroll)
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void close(View v){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
    class ClientThread implements Runnable {
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
                String s;

                while (!data.equals("-")) {
                    try {
                        Thread.sleep(SLEEP_TIME);
                        if(!finished) {
                            s = reader.readLine();
                            if(s == null){
                                data = "Connection is failed";
                                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            }
                            data = s;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                socket.close();
            } catch (IOException e) {
                data = e.getMessage();
            }
        }

        public boolean getFinished(){
            return this.finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public String getString() throws IOException {
            return data;
        }



    }
    public class MyTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {





            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


                try {

                   results.add(clientThread.getString());
                } catch (IOException e) {
                    e.printStackTrace();
                }








        return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            reuse();
        }
    }
    public class MainTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
           MainActivity.clientThread = new ClientThread();
            Thread thread = new Thread(clientThread);
            thread.start();
            reuse();
            return null;
        }
    }
}