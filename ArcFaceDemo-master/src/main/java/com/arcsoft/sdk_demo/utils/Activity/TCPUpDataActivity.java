package com.arcsoft.sdk_demo.utils.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.sdk_demo.R;
import com.arcsoft.sdk_demo.utils.Utils.TCPClient;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/4/17.
 */

public class TCPUpDataActivity extends Activity {
    private String TAG = "FuncTcpClient";
    @SuppressLint("StaticFieldLeak")
    public static Context context ;
    private Button btnStartClient,btnCloseClient, btnCleanClientSend, btnCleanClientRcv,btnClientSend,btnClientRandom;
    private TextView txtRcv,txtSend;
    private EditText editClientSend,editClientID, editClientPort,editClientIp;
    private static TCPClient tcpClient = null;
    private MyBtnClicker myBtnClicker = new MyBtnClicker();
    private final MyHandler myHandler = new MyHandler(this);
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    ExecutorService exec = Executors.newCachedThreadPool();
    private String name;
    private byte[] featureData;
    private String s;
    private byte[] bytes;

    private class MyBtnClicker implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_tcpClientConn:
                    Log.i(TAG, "onClick: 开始");
                    btnStartClient.setEnabled(false);
                    btnCloseClient.setEnabled(true);
                    btnClientSend.setEnabled(true);
                    tcpClient = new TCPClient(editClientIp.getText().toString(),getPort(editClientPort.getText().toString()));
                    exec.execute(tcpClient);
                    break;
                case R.id.btn_tcpClientClose:
                    tcpClient.closeSelf();
                    btnStartClient.setEnabled(true);
                    btnCloseClient.setEnabled(false);
                    btnClientSend.setEnabled(false);
                    break;
                case R.id.btn_tcpCleanClientRecv:
                    txtRcv.setText("");
                    break;
                case R.id.btn_tcpCleanClientSend:
                    txtSend.setText("");
                    break;
                case R.id.btn_tcpClientRandomID:
                    break;
                case R.id.btn_tcpClientSend:
                    Message message = Message.obtain();
                    message.what = 2;
                    //message.obj = editClientSend.getText().toString();
                   // myHandler.sendMessage(message);
                    if (((Application)TCPUpDataActivity.this.getApplicationContext()).mFaceDB.mRegister.size()>0){
                        name = ((Application)TCPUpDataActivity.this.getApplicationContext()).mFaceDB.mRegister.get(0).mName;
                        featureData = ((Application)TCPUpDataActivity.this.getApplicationContext()).mFaceDB.mRegister.get(0).mFaceList.get(0).getFeatureData();
                        Log.i("1111featureData長度",featureData.length+"");
                    }
                    if (featureData != null && featureData.length > 0){
                        s = byte2Base64StringFun(featureData);
                        Log.i("1111featureDatad s長度", base64String2ByteFun(s).length+"");
                        exec.execute(new Runnable() {
                            @Override
                            public void run() {
                                //tcpClient.send(editClientSend.getText().toString());
                                tcpClient.send(s);
                            }
                        });
                        message.obj = s;
                        myHandler.handleMessage(message);
                        featureData = null;
                        ((Application)context.getApplicationContext()).mFaceDB.delete(name);
                    }else {
                        message.obj = editClientSend.getText().toString();
                        myHandler.sendMessage(message);
                        tcpClient.send(editClientSend.getText().toString());
                    }

                    break;
            }
        }
    }
    //base64字符串转byte[]
    public static byte[] base64String2ByteFun(String base64Str){
        return Base64.decode(base64Str,Base64.DEFAULT);
    }
    //byte[]转base64
    public static String byte2Base64StringFun(byte[] b){
        return Base64.encodeToString(b,Base64.DEFAULT);
    }


    private class MyHandler extends android.os.Handler{
        private WeakReference<TCPUpDataActivity> mActivity;

        MyHandler(TCPUpDataActivity activity){
            mActivity = new WeakReference<TCPUpDataActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null){
                switch (msg.what){
                    case 1:
                        txtRcv.append(msg.obj.toString());
                        String s = txtRcv.getText().toString().replace("\r|\n","");
                        byte[] bytes = base64String2ByteFun(s);
                        Log.i("1111返回的bytes", bytes.length+"");
                            AFR_FSDKFace afr_fsdkFace = new AFR_FSDKFace();
                            afr_fsdkFace.setFeatureData(bytes);
                            ((Application)TCPUpDataActivity.this.getApplicationContext()).mFaceDB.addFace("11",afr_fsdkFace);
                        //byte[] bytes = msg.obj.toString().getBytes();
                        break;
                    case 2:
                        txtSend.append(msg.obj.toString());
                        break;
                }
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            switch (mAction){
                case "tcpClientReceiver":
                    String msg = intent.getStringExtra("tcpClientReceiver");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = msg;
                    myHandler.sendMessage(message);
                    break;
            }
        }
    }


    private int getPort(String msg){
        if (msg.equals("")){
            msg = "1234";
        }
        return Integer.parseInt(msg);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);
        context = this;
        bindID();
        bindListener();
        bindReceiver();
        Ini();
    }



    private void bindID(){
        btnStartClient = (Button) findViewById(R.id.btn_tcpClientConn);
        btnCloseClient = (Button) findViewById(R.id.btn_tcpClientClose);
        btnCleanClientRcv = (Button) findViewById(R.id.btn_tcpCleanClientRecv);
        btnCleanClientSend = (Button) findViewById(R.id.btn_tcpCleanClientSend);
        btnClientRandom = (Button) findViewById(R.id.btn_tcpClientRandomID);
        btnClientSend = (Button) findViewById(R.id.btn_tcpClientSend);
        editClientPort = (EditText) findViewById(R.id.edit_tcpClientPort);
        editClientIp = (EditText) findViewById(R.id.edit_tcpClientIp);
        editClientSend = (EditText) findViewById(R.id.edit_tcpClientSend);
        txtRcv = (TextView) findViewById(R.id.txt_ClientRcv);
        txtSend = (TextView) findViewById(R.id.txt_ClientSend);
    }
    private void bindListener(){
        btnStartClient.setOnClickListener(myBtnClicker);
        btnCloseClient.setOnClickListener(myBtnClicker);
        btnCleanClientRcv.setOnClickListener(myBtnClicker);
        btnCleanClientSend.setOnClickListener(myBtnClicker);
        btnClientRandom.setOnClickListener(myBtnClicker);
        btnClientSend.setOnClickListener(myBtnClicker );
    }
    private void bindReceiver(){
        IntentFilter intentFilter = new IntentFilter("tcpClientReceiver");
        registerReceiver(myBroadcastReceiver,intentFilter);
    }
    private void Ini(){
        btnCloseClient.setEnabled(false);
        btnClientSend.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(myBroadcastReceiver);
    }
}
