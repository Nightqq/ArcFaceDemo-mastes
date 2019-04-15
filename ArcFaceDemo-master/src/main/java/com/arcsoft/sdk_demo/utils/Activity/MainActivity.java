package com.arcsoft.sdk_demo.utils.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.liveness.ErrorInfo;
import com.arcsoft.liveness.LivenessEngine;
import com.arcsoft.sdk_demo.R;
import com.arcsoft.sdk_demo.utils.Utils.HttpUtils;
import com.arcsoft.sdk_demo.utils.bean.IsCallInfo;
import com.arcsoft.sdk_demo.utils.bean.PrisonerInfo;
import com.arcsoft.sdk_demo.utils.helper.IsCallInfoHelp;
import com.arcsoft.sdk_demo.utils.helper.PrisonerInfoHelp;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends Activity implements OnClickListener {
    private final String TAG = this.getClass().toString();

    private static final int REQUEST_CODE_IMAGE_CAMERA = 1;
    private static final int REQUEST_CODE_IMAGE_OP = 2;
    private static final int REQUEST_CODE_OP = 3;
    private String file;
    private TextView mTime;
    private LivenessEngine livenessEngine;
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.setContentView(R.layout.main_test);
        this.setContentView(R.layout.activity_main);
        IsCallInfoHelp.deleteALl();
        View data = findViewById(R.id.data);
        data.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TCPUpDataActivity.class));
            }
        });
        View v = this.findViewById(R.id.main_face_entry);
        v.setOnClickListener(this);
        v = this.findViewById(R.id.main_face_down);
        v.setOnClickListener(this);
        v = this.findViewById(R.id.main_face_identify);
        v.setOnClickListener(this);
        v = this.findViewById(R.id.main_face_db);
        v.setOnClickListener(this);
        v = this.findViewById(R.id.featuredata);
        v.setOnClickListener(this);
        mTime = (TextView) this.findViewById(R.id.main_time);

        new TimeCount(countdown() * 1000, 1000).start();

        //活体检测激活
        livenessEngine = new LivenessEngine();

    }



    private int countdown() {
        int i = getHour() * 60 * 60 + getMintue() * 60 + getSecond();
        int nine_time = 21 * 3600;
        int count_time = 0;
        if (i < nine_time) {
            count_time = nine_time - i;
        } else if (i - nine_time <= 3600) {
            // TODO: 2018\8\20 0020  点名时间
        } else {
            count_time = nine_time + 24 * 3600 - i;
        }
        return count_time;
    }

    // 将秒转化成小时分钟秒
    public String FormatMiss(long miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }


    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); //millisInFuture总计时长，countDownInterval时间间隔(一般为1000ms)
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTime.setText(FormatMiss(millisUntilFinished / 1000));
            // mTextView.setText(message + ": " + millisUntilFinished / 1000 + "s后消失");
        }

        @Override
        public void onFinish() {

        }
    }

    /**
     * 获取当前是几点
     */
    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前是几分
     */
    public static int getMintue() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 获取当前秒
     */
    public static int getSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_OP && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            file = getPath(mPath);
            Bitmap bmp = Application.decodeImage(file);
            if (bmp == null || bmp.getWidth() <= 0 || bmp.getHeight() <= 0) {
                Log.e(TAG, "error");
            } else {
                Log.i(TAG, "bmp [" + bmp.getWidth() + "," + bmp.getHeight());
            }
            startRegister(bmp, file);
        } else if (requestCode == REQUEST_CODE_OP) {
            Log.i(TAG, "RESULT =" + resultCode);
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            String path = bundle.getString("imagePath");
            Log.i(TAG, "path=" + path);
        } else if (requestCode == REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            Uri mPath = ((Application) (MainActivity.this.getApplicationContext())).getCaptureImage();
            file = getPath(mPath);
            Bitmap bmp = Application.decodeImage(file);
            startRegister(bmp, file);
        }
    }

    @Override
    public void onClick(View paramView) {
        // TODO Auto-generated method stub
        switch (paramView.getId()) {
            case R.id.main_face_down:
               HttpUtils.QueryAddressTask queryAddressTask = new HttpUtils.QueryAddressTask(true, MainActivity.this, null);
               queryAddressTask.execute();
                /*Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        final long activeCode = livenessEngine.activeEngine(MainActivity.this,FaceDB.live_appid,
                                FaceDB.live_key).getCode();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(activeCode == ErrorInfo.MOK) {
                                    Toast.makeText(MainActivity.this, "活体引擎激活成功", Toast.LENGTH_SHORT).show();
                                } else if(activeCode == ErrorInfo.MERR_AL_BASE_ALREADY_ACTIVATED){
                                    Toast.makeText(MainActivity.this, "活体引擎已激活", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "活体引擎激活失败，errorcode：" + activeCode, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });*/
                break;
            case R.id.featuredata:
               /* HttpUtils.QueryAddressTask queryAddressTask = new HttpUtils.QueryAddressTask(false, MainActivity.this, file);
                queryAddressTask.execute();*/
                break;
            case R.id.main_face_identify:
                if (((Application) getApplicationContext()).mFaceDB.mRegister.isEmpty()) {
                    Toast.makeText(this, "没有注册人脸，请先注册！", Toast.LENGTH_SHORT).show();
                } else {
                    //默认使用前置摄像头
                    startDetector(1);
					/*new AlertDialog.Builder(this)
							.setTitle("请选择相机")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setItems(new String[]{"后置相机", "前置相机"}, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											startDetector(which);
										}
									})
							.show();*/
                }
                break;
            case R.id.main_face_entry:
                new AlertDialog.Builder(this)
                        .setTitle("请选择注册方式")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(new String[]{"打开图片", "拍摄照片"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 1:
                                        //相机的启动
                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                        ContentValues values = new ContentValues(1);
                                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                        ((Application) (MainActivity.this.getApplicationContext())).setCaptureImage(uri);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
                                        break;
                                    case 0:
                                        Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
                                        getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
                                        getImageByalbum.setType("image/jpeg");
                                        startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
                                        break;
                                    default:
                                        ;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.main_face_db:
                if (((Application) this.getApplicationContext()).mFaceDB.mRegister.isEmpty()) {
                    Toast.makeText(MainActivity.this, "没有数据，请先录入人脸", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, FaceDBActivity.class);
                startActivity(intent);
            default:
                ;
        }
    }

    /**
     * @param uri
     * @return
     */
    private String getPath(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(this, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(this, contentUri, selection, selectionArgs);
                }
            }
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param mBitmap
     */
    private void startRegister(Bitmap mBitmap, String file) {
        Intent it = new Intent(MainActivity.this, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", file);
        it.putExtras(bundle);
        startActivityForResult(it, REQUEST_CODE_OP);
    }

    private void startDetector(int camera) {
        // TODO: 2018\8\21 0021 初始化点名数据暂在此次处理
        initCall();
        Intent it = new Intent(MainActivity.this, DetecterActivity.class);
        it.putExtra("Camera", camera);
        startActivityForResult(it, REQUEST_CODE_OP);
    }

    private void initCall() {
        List<PrisonerInfo> prisonerInfoToDB = PrisonerInfoHelp.getPrisonerInfoToDB();
        if (prisonerInfoToDB != null && prisonerInfoToDB.size() > 0) {
            for (PrisonerInfo prisonerInfo : prisonerInfoToDB) {
                IsCallInfo isCallInfo = new IsCallInfo();
                isCallInfo.setId(prisonerInfo.getId());
                isCallInfo.setCrime_id(prisonerInfo.getCrime_id());
                isCallInfo.setCrime_jianqu(prisonerInfo.getCrime_jianqu());
                isCallInfo.setCrime_xb(prisonerInfo.getCrime_xb());
                isCallInfo.setCrime_name(prisonerInfo.getCrime_name());
                isCallInfo.setIscall(false);
                isCallInfo.setCrime_photo("无");
                isCallInfo.setCallphoto_time("无");
                IsCallInfoHelp.saveIsCallInfoToDB(isCallInfo);
            }
        }
    }


}

