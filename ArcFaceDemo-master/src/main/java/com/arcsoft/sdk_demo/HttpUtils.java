package com.arcsoft.sdk_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.sdk_demo.utils.bean.FaceFeatureData;
import com.arcsoft.sdk_demo.utils.bean.upBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.arcsoft.sdk_demo.Application.mFaceDB;

/**
 * Created by Administrator on 2018/6/28.
 */

public class HttpUtils {


    private static OkHttpClient okHttpClient;
    private static String result;
    private static String methodName;
    public static String name;
    private static byte[] featureData;
    private static String featuredata;

    public HttpUtils() {
    }

    //初始化
    public void getInstances(Context context) {
        if (okHttpClient == null) {
            File cacheDir = new File(context.getCacheDir(), "okhttp_cache");
            //File cacheDir = new File(getExternalCacheDir(), "okhttp");
            Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//日志级别，Body级别打印的信息最全面
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS) //链接超时
                    .readTimeout(10 * 1000, TimeUnit.MILLISECONDS) //读取超时
                    .writeTimeout(10 * 1000, TimeUnit.MILLISECONDS) //写入超时
//                    .addInterceptor(new HttpHeadInterceptor()) //应用拦截器：统一添加消息头
//                    .addNetworkInterceptor(new NetworkspaceInterceptor())//网络拦截器
                    .addInterceptor(httpLoggingInterceptor)//应用拦截器：打印日志
                    .cache(cache)  //设置缓存
                    .build();
        }
    }

    public void getData(final responseListener responses) {
        RequestBody requestBody = new FormBody.Builder()
                .add("GetFaceFeature", "").build();
        Request req = new Request.Builder().url("http://192.168.0.10/WebSite/OperatePolygon.asmx?").post(requestBody).build();
        Call call = okHttpClient.newCall(req);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responses.failure("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                boolean successful = response.isSuccessful();
                if (successful) {
                    String content = response.body().string();
                    responses.successful(content);
                }
            }
        });
    }

    public responseListener responses;

    public interface responseListener {
        void successful(String s);

        void failure(String s);
    }

    public static String getRemoteInfo(boolean action, Context context, String file) throws Exception {
        String WSDL_URI = "http://192.168.0.10/WebSite/OperatePolygon.asmx";//wsdl 的uri
        String namespace = "http://tempuri.org/";//namespace
        String soapcaction = "";
        if (action) {
            //要调用的方法名称
            methodName = "GetFaceFeature";
            soapcaction = "http://tempuri.org/GetFaceFeature";
        } else {
            methodName = "WriteFaceFeature";
            soapcaction = "http://tempuri.org/WriteFaceFeature";
        }

        SoapObject request = new SoapObject(namespace, methodName);
        upBean upBean = new upBean();
        //上传的数据
        if (!action) {
            upBean.setEmpid("6685D1");
            upBean.setEmpname("唐建冲");
            upBean.setEmphao("91499993");
            if (file != null && file.length() > 0) {
                Bitmap mBitmap = Application.decodeImage(file);
                String s = bitmapToBase64(mBitmap);
                upBean.setFaceimage(s);
                //request.addProperty("jsonInfo","{\"empid\":\"6685D2\",\"empname\":\"田\",\"emphao\":\"91499994\",\"faceimage\":\""+s+"\"}");
                request.addProperty("jsonInfo","{\"empid\":\"6685D2\",\"empname\":\""+name+"\",\"emphao\":\"91499994\",\"faceimage\":\""+s+"\"}");
            }
        }
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        httpTransportSE.call(soapcaction, envelope);//调用

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        result = object.getProperty(0).toString();
        Log.d("qeqrqrrqr", result);
        return result;

    }

    public static class QueryAddressTask extends AsyncTask<String, Integer, String> {
        private String result;
        private boolean isUpData;//true是下载数据，false是上传数据
        private Context context;
        private String file;

        public QueryAddressTask(boolean action, Context context ,String file) {
            isUpData = action;
            this.context = context;
            this.file = file;
        }

        @Override
        protected String doInBackground(String... params) {
            // 查询手机号码（段）信息*/
            try {
                result = getRemoteInfo(isUpData,context,file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将结果返回给onPostExecute方法
            return result;
        }

        @Override
        //此方法可以在主线程改变UI
        protected void onPostExecute(String result) {
            if (isUpData) {
                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(new StringReader(result))   ;
                try {
                    jsonReader.beginArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                List<FaceFeatureData> faceFeatureData = gson.fromJson(result, new TypeToken<List<FaceFeatureData>>() {
                }.getType());
                if (faceFeatureData != null) {
                    for (FaceFeatureData f : faceFeatureData
                            ) {
                        byte[] bytes = base64String2ByteFun(f.getFeaturedata());
                        featuredata = f.getFeaturedata();
                        Log.i("1111返回的bytes", bytes.length + "");
                        AFR_FSDKFace afr_fsdkFace = new AFR_FSDKFace();
                        afr_fsdkFace.setFeatureData(bytes);
                        mFaceDB.addFace(f.getEmp_name(), afr_fsdkFace);
                    }
                }
            } else {
                if (result != null) {
                    Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
                    Log.i("返回数据4", result);
                }
            }
        }
    }

    //base64字符串转byte[]
    public static byte[] base64String2ByteFun(String base64Str) {
        return Base64.decode(base64Str, Base64.DEFAULT);
    }

    //byte[]转base64
    public static String byte2Base64StringFun(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @SuppressLint("NewApi")
    public static String bitmapToBase64(Bitmap bitmap) {

        // 要返回的字符串
        String reslut = null;

        ByteArrayOutputStream baos = null;

        try {

            if (bitmap != null) {

                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();

                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return reslut;

    }
}



