package kolovaitis.by.socketwork123;

import android.os.AsyncTask;

import java.io.IOException;

public class AnimateTask extends AsyncTask {
    private static final int SLEEP_TIME = 1;


    @Override
    protected Object doInBackground(Object[] objects) {








        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
       for(int i=0;i<40;i++)
        MainActivity.animate();
        if(MyTask.canWork)MainActivity.startAnimate();
    }
}

