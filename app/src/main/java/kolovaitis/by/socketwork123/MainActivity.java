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
import java.util.Random;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> results;
    static TextView text;
    public static ClientThread clientThread;
    public boolean wasClosed;

    static TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wasClosed = false;
        MyTask.canWork=true;
        textView=((TextView)findViewById(R.id.textView));

        text = (TextView) findViewById(R.id.task);
        results = new ArrayList<>();

        startClient();
        work();
        for(int i=0;i<400;i++)
            MainActivity.animate();
    startAnimate();
   }
    @Override
    public void onResume(){
        super.onResume();
        if(wasClosed){
         Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

public static void animate(){

    textView.setText((char)(new Random().nextInt(1000)) + "" + textView.getText());
    if(textView.getText().length()>10000)textView.setText((textView.getText()+"").substring(1000));
}
    public void startClient() {
        MainActivity.clientThread = new ClientThread();
        Thread thread = new Thread(MainActivity.clientThread);
        thread.start();
    }
    public static void startAnimate() {

        new AnimateTask().execute();
    }
    public static void work() {
        new MyTask().execute();
    }

    public static void draw() {
        try {
            text.setTextSize(80);
            text.setText(results.get(results.size() - 1));
        } catch (ArrayIndexOutOfBoundsException e) {
        }


    }
public static void drawText(String s){
    text.setTextSize(40);
    text.setText(s);
}
    public void exit(View v) {

        clientThread.setFinished(true);
        MyTask.canWork = false;
        finish();
    }

    public void history(View v) {
        ScrollView scroll = new ScrollView(this);
        LinearLayout linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(linear);
        for (int i = 0; i < results.size(); i++) {
            if(!results.get(i).equals("")){TextView textView = new TextView(this);
            textView.setText(results.get(i));
            textView.setGravity(Gravity.CENTER);
            linear.addView(textView);}
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

    public void close(View v) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}

