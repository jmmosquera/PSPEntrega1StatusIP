package iezv.jmm.pspappipstatus;

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
        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.textView3 = (TextView) findViewById(R.id.textView3);
        this.textView2 = (TextView) findViewById(R.id.textView2);
        this.textView = (TextView) findViewById(R.id.textView);
        this.button = (Button) findViewById(R.id.button);

        checkInternet();
        checkIP();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternet();
                checkIP();
            }
        });
        //CheckInternet.execute(new String[]{"...","..."});
    }

    public void checkInternet(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Runtime rt = Runtime.getRuntime();
                try{
                    proceso = rt.exec("/system/bin/ping -c 1 8.8.8.8");
                    int intento = proceso.waitFor();
                    if(intento==0){
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.greentick));
                    }else{
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }catch (InterruptedException ignore)
                {
                    ignore.printStackTrace();
                    System.out.println(" Exception:"+ignore);
                }
            }
        });
    }

    public void checkIP(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Runtime rt = Runtime.getRuntime();
                try{
                    proceso = rt.exec("ifconfig");

                    InputStream is = proceso.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    final BufferedReader br = new BufferedReader(isr);
                    String linea;
                    while((linea = br.readLine()) != null ){
                        if(linea.contains("wlan0")){
                            linea = br.readLine();
                            String ipwlan = linea.substring(20,35);
                            textView2.setText(ipwlan);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
