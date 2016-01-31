package kolovaitis.by.socketwork123;

import android.os.AsyncTask;

import java.io.IOException;

/**
 *
 */
    public class MyTask extends AsyncTask {
        private static final int SLEEP_TIME = 1000;
    public static boolean canWork=true;
    String currentData;
        @Override
        protected Object doInBackground(Object[] objects) {





            try {currentData=MainActivity.clientThread.getCurrentData();


            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(currentData!=null){MainActivity.results.add(currentData);
                MainActivity.draw();}else MainActivity.drawText("Connecting...");
            if(canWork)MainActivity.work();
        }
    }
