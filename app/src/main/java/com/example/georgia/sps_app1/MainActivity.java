package com.example.georgia.sps_app1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //Declaration of variables
    public TextView action;
    private AlarmManager myAlarm;
    private PendingIntent myIntent;
    public String TAG="com.example.georgia.sps_app1";
    private static final long INTERVAL= 30*1000L ;
    MyDbHandler myDbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Line to keep screen on permanently
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        myDbHandler=new MyDbHandler(this,null,null,1);
        createDirectory();
        action=(TextView)findViewById(R.id.Activity);
        Log.i(TAG,"App started");

        //Set repeating alarm every 30 seconds
        myAlarm=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this,MyService.class);
        myIntent=PendingIntent.getService(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        myAlarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),INTERVAL,myIntent);

    }

    public void cancelAlarm(){
        if(myAlarm!=null){
            myAlarm.cancel(myIntent);
        }
    }


    @Override
    protected void onStop() {
        cancelAlarm();
        if (isExternalStorageWritable() && fileExistsCheck()){
            addToFile();
        }
        super.onStop();
    }


    //Methodos gia dimiourgia tou fakelou MySensorData
    public void createDirectory(){
        File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"SmartPhoneSensing");
        if(!f.exists()) {
            boolean b = f.mkdirs();
            if (b)
                Log.i(TAG, "Directory created");
        }
        else
            Log.i(TAG,"Directory already exists");
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.i(TAG,"External writable");
            return true;
        }
        else{
            Log.i(TAG,"External not writable");
            return false;
        }
    }


    public boolean fileExistsCheck(){
        String filename="CollectedData.txt";
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartPhoneSensing/",filename);
        if(file.exists()){
            Log.i(TAG,"File already exists!");
        }
        else{
            Log.i(TAG,"File does not exist!");
            createFile();
        }
        return true;
    }


    public void createFile(){
        File myFile;
        FileOutputStream outputStream;
        String filename="CollectedData.txt";
        String myString="COLUMN_ID,COLUMN_DEVICEID,COLUMN_MODEL,COLUMN_TIMESTAMP,COLUMN_ACCELEROMETER0,COLUMN_ACCELEROMETER1,COLUMN_ACCELEROMETER2,COLUMN_SSID," +
                "COLUMN_RSSI,COLUMN_LOCALTIME \n";
        try{
            myFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartPhoneSensing/",filename);
            outputStream=new FileOutputStream(myFile);
            outputStream.write(myString.getBytes());
            outputStream.close();
            Log.i(TAG,"File created");
        }
        catch(IOException e){
            e.printStackTrace();
            Log.i(TAG,e.toString());
        }
    }


    public void addToFile(){
        String myString=myDbHandler.DatabaseToString();
        String filename="CollectedData.txt";
        try {
            String file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartPhoneSensing/" + filename;
            Log.i(TAG,file + " added");
            FileWriter fw = new FileWriter(file,true);
            fw.write( myString + "\n");
            fw.close();
            myDbHandler.deleteAll();
        }
        catch (IOException ioe) {
            Log.i(TAG,"ERROR ADDING");
            Log.i(TAG,ioe.toString());
        }
    }


}
