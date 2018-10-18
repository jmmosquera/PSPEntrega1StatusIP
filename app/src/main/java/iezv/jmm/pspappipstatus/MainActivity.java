package iezv.jmm.pspappipstatus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private android.widget.TextView textView;
    private android.widget.TextView textView2;
    private android.widget.TextView textView3;
    public static android.widget.ImageView imageView;
    private android.widget.Button button;
    private Process proceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageView = findViewById(R.id.imageView);
        this.textView3 = findViewById(R.id.textView3);
        this.textView2 = findViewById(R.id.textView2);
        this.textView = findViewById(R.id.textView);
        this.button = findViewById(R.id.button);

        checkInternet();
        checkIP();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternet();
                checkIP();
            }
        });
    }

    public void checkInternet(){
        CheckInternet check = new CheckInternet();
        check.execute();
    }

    public void checkIP(){
        CheckIP check = new CheckIP();
        check.execute();
    }

    private class CheckIP extends AsyncTask<Integer, Integer, String>{

        CheckIP(){}

        @Override
        protected String doInBackground(Integer... integers) {
            Runtime rt = Runtime.getRuntime();
            String ipwlan = "";
            try{
                proceso = rt.exec("ifconfig");
                InputStream is = proceso.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                final BufferedReader br = new BufferedReader(isr);
                String linea;
                while((linea = br.readLine()) != null ){
                    if(linea.contains("wlan0")){
                        linea = br.readLine();
                        ipwlan = linea.substring(20,35);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return ipwlan;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView2.setText(s);
        }
    }

    private class CheckInternet  extends AsyncTask<Integer, Integer, Integer>{

        CheckInternet(){}

        @Override
        protected Integer doInBackground(Integer... integers) {
            int intento = 0;
            Runtime rt = Runtime.getRuntime();
            try{
                proceso = rt.exec("/system/bin/ping -c 1 8.8.8.8");
                intento = proceso.waitFor();
            }catch (IOException e){
                e.printStackTrace();
            }catch (InterruptedException ignore)
            {
                ignore.printStackTrace();
                System.out.println(" Exception:"+ignore);
            }
            return intento;
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            if(i==0){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.greentick));
            }else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));
            }
        }
    }
}
