package com.arcsoft.sdk_demo.utils.Activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.arcsoft.sdk_demo.R;

public class SenerMangerActivity extends Activity implements SensorEventListener{

    private SensorManager senermanger;
    private Sensor defaultSensor;
    private TextView tvX;
    private TextView tvY;
    private TextView tvZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sener_manger);
        tvX = (TextView) findViewById(R.id.x);
        tvY = (TextView) findViewById(R.id.y);
        tvZ = (TextView) findViewById(R.id.z);
        senermanger = (SensorManager) getSystemService(SENSOR_SERVICE);
        defaultSensor = senermanger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[SensorManager.DATA_X];
        float y = sensorEvent.values[SensorManager.DATA_Y];
        float z = sensorEvent.values[SensorManager.DATA_Z];

        tvX.setText("x = "+x);
        tvY.setText("y = "+y);
        tvZ.setText("z = "+z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        senermanger.registerListener(this,defaultSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }


}
