package com.example.georgia.sps_app1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {

    public String TAG="com.example.georgia.sps_app1";
    private  String date;
    private String deviceId,manuf,model;
    private SensorManager mySensorManager;
    private SensorEventListener mySensorEventListener;
    private Sensor accelerometer;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private float aX = 0, aY=0, aZ=0;
    MyDbHandler myDbHandler;
    SensorsTable myTable=new SensorsTable();


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG,"Service started");
        myDbHandler=new MyDbHandler(this,null,null,1);
        mySensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // Set the wifi manager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mySensorEventListener=new SensorEventListener() {
            //Methods used to enact SensorEventLIstener
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.i(TAG,"Sensor event realised");
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                date=sdf.format(new Date());
                manuf= Build.MANUFACTURER;
                model=Build.MODEL;
                String modelName= manuf+" "+model;
                myTable.set_modelName(modelName);
                myTable.set_tmst(date);
                deviceId= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                myTable.set_deviceId(deviceId);

                // get the the x,y,z values of the accelerometer
                aX = event.values[0];
                aY = event.values[1];
                aZ = event.values[2];
                myTable.set_accVal0(Float.toString(aX));
                myTable.set_accVal1(Float.toString(aY));
                myTable.set_accVal2(Float.toString(aZ));

                // get the wifi info.
                wifiInfo = wifiManager.getConnectionInfo();
                // update the text.
                myTable.set_SSID( wifiInfo.getSSID());
                myTable.set_RSSI(Integer.toString(wifiInfo.getRssi()));
                myTable.setLocalTime(Long.toString(System.currentTimeMillis()));
                myDbHandler.addRow(myTable);
                mySensorManager.unregisterListener(mySensorEventListener);
               stopSelf();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };


        // if the default accelerometer exists
        if (mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // set accelerometer
            accelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mySensorManager.registerListener(mySensorEventListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }
        return Service.START_STICKY;
    }



    @Override
    public void onDestroy() {
        Log.i(TAG,"Service destroyed");
        super.onDestroy();
    }
}
