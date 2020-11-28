package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    String filename;
    MediaRecorder recorder;
    MediaPlayer player;
    Thread thread;
    boolean isRecording=false;
   float time=0;
    TextView text;
ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = findViewById(R.id.progressBar2);
        text = findViewById(R.id.textView2);
        pb.setMax(32000);
    }



    public void Rec (View v)
    {

        Date now = new Date();
        time=0;
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        filename =Environment.getExternalStorageDirectory().toString()+
        "/Download/"+format.format(now)+".mp4";

        try {
            isRecording=true;
            recorder= new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(filename);
            recorder.prepare();
            recorder.start();

            thread = new Thread (new Runnable() {
                @Override
                public void run() {
                    while (isRecording)
                    {
                        time+=0.1;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               if (recorder!=null)
                                    pb.setProgress(recorder.getMaxAmplitude());
                                   text.setText(String.valueOf(time));
                           }
                       });
                    }
                }
            });
            thread.start();

        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }


    }
    public void stop (View v) {
            if (recorder != null) {
                isRecording = false;
                recorder.stop();
                recorder = null;
            }
        }

    public void playAudio(View v)
    {
        try
        {
            player = new MediaPlayer();
            player.setDataSource(filename);
            player.prepare();
            player.start();
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void stopAudio(View v) {
        if (player != null) {
            player.stop();
            player=null;
        }
    }
}