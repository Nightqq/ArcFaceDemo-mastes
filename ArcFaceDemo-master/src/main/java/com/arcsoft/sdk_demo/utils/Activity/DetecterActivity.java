package com.arcsoft.sdk_demo.utils.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcsoft.ageestimation.ASAE_FSDKAge;
import com.arcsoft.ageestimation.ASAE_FSDKEngine;
import com.arcsoft.ageestimation.ASAE_FSDKError;
import com.arcsoft.ageestimation.ASAE_FSDKFace;
import com.arcsoft.ageestimation.ASAE_FSDKVersion;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKError;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKVersion;
import com.arcsoft.genderestimation.ASGE_FSDKEngine;
import com.arcsoft.genderestimation.ASGE_FSDKError;
import com.arcsoft.genderestimation.ASGE_FSDKFace;
import com.arcsoft.genderestimation.ASGE_FSDKGender;
import com.arcsoft.genderestimation.ASGE_FSDKVersion;
import com.arcsoft.sdk_demo.R;
import com.arcsoft.sdk_demo.utils.Utils.AudioPlayUtils;
import com.arcsoft.sdk_demo.utils.Utils.TextToSpeechUtils;
import com.arcsoft.sdk_demo.utils.bean.IsCallInfo;
import com.arcsoft.sdk_demo.utils.helper.IsCallInfoHelp;
import com.guo.android_extend.java.AbsLoop;
import com.guo.android_extend.java.ExtByteArrayOutputStream;
import com.guo.android_extend.tools.CameraHelper;
import com.guo.android_extend.widget.CameraFrameData;
import com.guo.android_extend.widget.CameraGLSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView.OnCameraListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gqj3375 on 2017/4/28.
 */

public class DetecterActivity extends Activity implements OnCameraListener, View.OnTouchListener, Camera.AutoFocusCallback {
    private final String TAG = this.getClass().getSimpleName();
    private int mWidth, mHeight, mFormat;
    private CameraSurfaceView mSurfaceView;
    private CameraGLSurfaceView mGLSurfaceView;
    private Camera mCamera;
    AFT_FSDKVersion version = new AFT_FSDKVersion();
    AFT_FSDKEngine engine = new AFT_FSDKEngine();
    ASAE_FSDKVersion mAgeVersion = new ASAE_FSDKVersion();
    ASAE_FSDKEngine mAgeEngine = new ASAE_FSDKEngine();
    ASGE_FSDKVersion mGenderVersion = new ASGE_FSDKVersion();
    ASGE_FSDKEngine mGenderEngine = new ASGE_FSDKEngine();
    List<AFT_FSDKFace> result = new ArrayList<>();
    List<ASAE_FSDKAge> ages = new ArrayList<>();
    List<ASGE_FSDKGender> genders = new ArrayList<>();
    int mCameraID;
    int mCameraRotate;
    boolean mCameraMirror;
    byte[] mImageNV21 = null;
    FRAbsLoop mFRAbsLoop = null;
    AFT_FSDKFace mAFT_FSDKFace = null;
    private boolean flag0 = true;//锁
    Handler mHandler;
    Runnable hide = new Runnable() {
        @Override
        public void run() {
            mTextView.setAlpha(0.5f);
            mImageView.setImageAlpha(128);
        }
    };

    Runnable open_loop = new Runnable() {
        @Override
        public void run() {
            mImageNV21 = null;
            mFRAbsLoop = new FRAbsLoop();
            mFRAbsLoop.start();
        }
    };
    private TextToSpeechUtils textToSpeechUtils;


    class FRAbsLoop extends AbsLoop {
        AFR_FSDKVersion version = new AFR_FSDKVersion();
        //ASAE_FSDKEngine asae_fsdkEngine = new ASAE_FSDKEngine();
        //ASGE_FSDKEngine asge_fsdkEngine = new ASGE_FSDKEngine();
        AFR_FSDKEngine engine = new AFR_FSDKEngine();
        AFR_FSDKFace result = new AFR_FSDKFace();
        List<FaceDB.FaceRegist> mResgist = ((Application) DetecterActivity.this.getApplicationContext()).mFaceDB.mRegister;
        List<ASAE_FSDKFace> face1 = new ArrayList<>();
        List<ASGE_FSDKFace> face2 = new ArrayList<>();
        private AudioPlayUtils audioPlayUtils;

        @Override
        public void setup() {
            AFR_FSDKError error = engine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
            Log.d(TAG, "AFR_FSDK_InitialEngine = " + error.getCode());
            error = engine.AFR_FSDK_GetVersion(version);
            Log.d(TAG, "FR=" + version.toString() + "," + error.getCode()); //(210, 178 - 478, 446), degree = 1　780, 2208 - 1942, 3370
        }

        @Override
        public void loop() {
            if (mImageNV21 != null&&flag0) {
                flag0=false;
                long time = System.currentTimeMillis();
                AFR_FSDKError error = engine.AFR_FSDK_ExtractFRFeature(mImageNV21, mWidth, mHeight, AFR_FSDKEngine.CP_PAF_NV21, mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree(), result);
                Log.d(TAG, "AFR_FSDK_ExtractFRFeature cost :" + (System.currentTimeMillis() - time) + "ms");
                Log.d(TAG, "Face=" + result.getFeatureData()[0] + "," + result.getFeatureData()[1] + "," + result.getFeatureData()[2] + "," + error.getCode());
                AFR_FSDKMatching score = new AFR_FSDKMatching();
                //ASGE_FSDKError asge_fsdkError = asge_fsdkEngine.ASGE_FSDK_GenderEstimation_Image(mImageNV21, mWidth, mHeight, AFR_FSDKEngine.CP_PAF_NV21, face2, genders);
                //ASAE_FSDKError asae_fsdkError = asae_fsdkEngine.ASAE_FSDK_AgeEstimation_Image(mImageNV21, mWidth, mHeight, AFR_FSDKEngine.CP_PAF_NV21, face1, ages);
                //Log.d(TAG, "111111111111111"+asae_fsdkError.getCode()+"2222222222222222"+asge_fsdkError.getCode());
                float max = 0.0f;
                int flag = 0;
                String name = null;
                for (FaceDB.FaceRegist fr : mResgist) {
                    for (AFR_FSDKFace face : fr.mFaceList) {
                        error = engine.AFR_FSDK_FacePairMatching(result, face, score);
                        Log.d(TAG, "Score:" + score.getScore() + ", AFR_FSDK_FacePairMatching=" + error.getCode());
//						if (max < score.getScore()) {
//							max = score.getScore();
//							name = fr.mName;
//						}
                        if (score.getScore() > 0.6) {
                            max = score.getScore();
                            name = fr.mName;
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 1) {
                        break;
                    }
                }
                //age & gender
                face1.clear();
                face2.clear();
                face1.add(new ASAE_FSDKFace(mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree()));
                face2.add(new ASGE_FSDKFace(mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree()));
                ASAE_FSDKError error1 = mAgeEngine.ASAE_FSDK_AgeEstimation_Image(mImageNV21, mWidth, mHeight, AFT_FSDKEngine.CP_PAF_NV21, face1, ages);
                ASGE_FSDKError error2 = mGenderEngine.ASGE_FSDK_GenderEstimation_Image(mImageNV21, mWidth, mHeight, AFT_FSDKEngine.CP_PAF_NV21, face2, genders);
//				Log.d(TAG, "ASAE_FSDK_AgeEstimation_Image:" + error1.getCode() + ",ASGE_FSDK_GenderEstimation_Image:" + error2.getCode());
                //Log.d(TAG, "age:" + ages.get(0).getAge() + ",gender:" + genders.get(0).getGender());
                //final String age = ages.get(0).getAge() == 0 ? "年龄未知" : ages.get(0).getAge() + "岁";
                //final String gender = genders.get(0).getGender() == -1 ? "性别未知" : (genders.get(0).getGender() == 0 ? "男" : "女");
                //crop
                byte[] data = mImageNV21;
                YuvImage yuv = new YuvImage(data, ImageFormat.NV21, mWidth, mHeight, null);
                ExtByteArrayOutputStream ops = new ExtByteArrayOutputStream();
                yuv.compressToJpeg(mAFT_FSDKFace.getRect(), 80, ops);
                final Bitmap bmp = BitmapFactory.decodeByteArray(ops.getByteArray(), 0, ops.getByteArray().length);
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (max > 0.6f) {
                    //点名成功，判断是否已经点名，语音提示
                    IsCallInfo callInfo = IsCallInfoHelp.isCall(name);
                    if (callInfo.getIscall()) {
                       /* if (audioPlayUtils == null || !audioPlayUtils.isPlaying()) {
                            audioPlayUtils = new AudioPlayUtils(DetecterActivity.this, R.raw.call_repeat);
                            audioPlayUtils.play();
                        }*/
                        textToSpeechUtils.notifyNewMessage(name+"请不要重复点名");
                        flag0=true;
                        mFRAbsLoop.shutdown();
                        mHandler.removeCallbacks(open_loop);
                        mHandler.postDelayed(open_loop, 3000);
                        return;
                    }
                    callInfo.setIscall(true);
                    IsCallInfoHelp.saveIsCallInfoToDB(callInfo);
                   /* if (audioPlayUtils == null || !audioPlayUtils.isPlaying()) {
                        audioPlayUtils = new AudioPlayUtils(DetecterActivity.this, R.raw.call_success);
                        audioPlayUtils.play();
                    }*/
                    textToSpeechUtils.notifyNewMessage(name+"点名成功");
                    //fr success.
                    final float max_score = max;
                    Log.d(TAG, "fit Score:" + max + ", NAME:" + name);
                    final String mNameShow = name;
                    mHandler.removeCallbacks(hide);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            init();
                            mTextView.setAlpha(1.0f);
                            mTextView.setText(mNameShow);
                            mTextView.setTextColor(Color.RED);
                            mTextView1.setVisibility(View.VISIBLE);
                            mTextView1.setText("相似度：" + (float) ((int) (max_score * 1000)) / 10.0 + "% 已通过");
                            mTextView1.setTextColor(Color.RED);
                            mImageView.setRotation(mCameraRotate);
                            if (mCameraMirror) {
                                mImageView.setScaleY(-1);
                            }
                            mImageView.setImageAlpha(255);
                            mImageView.setImageBitmap(bmp);
                        }
                    });
                    //拍照保存上传
                    takePictures();

                    mHandler.removeCallbacks(open_loop);
                    mHandler.postDelayed(open_loop, 4000);
                } else {
                    //未识别语音提示
                    /*if (audioPlayUtils == null || !audioPlayUtils.isPlaying()) {
                        audioPlayUtils = new AudioPlayUtils(DetecterActivity.this, R.raw.call_failure);
                        audioPlayUtils.play();
                    }*/
                    textToSpeechUtils.notifyNewMessage("人脸识别失败");
                    final String mNameShow = "未识别";
                    DetecterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setAlpha(1.0f);
                            mTextView1.setVisibility(View.VISIBLE);
                            //mTextView1.setText( gender + "," + age);
                            mTextView1.setTextColor(Color.RED);
                            mTextView.setText(mNameShow);
                            mTextView.setTextColor(Color.RED);
                            mImageView.setImageAlpha(255);
                            mImageView.setRotation(mCameraRotate);
                            if (mCameraMirror) {
                                mImageView.setScaleY(-1);
                            }
                            mImageView.setImageBitmap(bmp);
                        }
                    });
                    mHandler.removeCallbacks(open_loop);
                    mHandler.postDelayed(open_loop, 1500);
                }
                mFRAbsLoop.shutdown();
                mImageNV21 = null;
                flag0=true;

            }
        }

        //线程结束，关闭引擎
        @Override
        public void over() {
            AFR_FSDKError error = engine.AFR_FSDK_UninitialEngine();
            Log.d(TAG, "AFR_FSDK_UninitialEngine : " + error.getCode());
        }
    }

    private void takePictures() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                // TODO: 2018\9\10 0010  保存上传
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mCamera.startPreview();
            }
        });
    }

    private TextView mTextView;
    private TextView mTextView1;
    private ImageView mImageView;
    private TextView rolltitle;
    private TextView mCallAll;
    private TextView mCallYes;
    private TextView mCallNo;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCameraID = getIntent().getIntExtra("Camera", 0) == 0 ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        mCameraRotate = getIntent().getIntExtra("Camera", 0) == 0 ? 90 : 270;
        mCameraMirror = getIntent().getIntExtra("Camera", 0) == 0 ? false : true;
        mWidth = 1280;
        mHeight = 960;
        mFormat = ImageFormat.NV21;
        mHandler = new Handler();
        setContentView(R.layout.activity_camera);
        new AudioPlayUtils(this, R.raw.start_call).play();
        mGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.glsurfaceView);
        mGLSurfaceView.setOnTouchListener(this);
        mSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
        mSurfaceView.setOnCameraListener(this);
        mSurfaceView.setupGLSurafceView(mGLSurfaceView, true, mCameraMirror, mCameraRotate);
        mSurfaceView.debug_print_fps(true, false);

        //snap
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setText("");
        mTextView1 = (TextView) findViewById(R.id.textView1);
        mTextView1.setText("");

        rolltitle = (TextView) findViewById(R.id.roll_title);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mCallAll = ((TextView) findViewById(R.id.call_All_num));
        mCallYes = ((TextView) findViewById(R.id.call_yes));
        mCallNo = ((TextView) findViewById(R.id.call_no));

        AFT_FSDKError err = engine.AFT_FSDK_InitialFaceEngine(FaceDB.appid, FaceDB.ft_key, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5);
        Log.d(TAG, "AFT_FSDK_InitialFaceEngine =" + err.getCode());
        err = engine.AFT_FSDK_GetVersion(version);
        Log.d(TAG, "AFT_FSDK_GetVersion:" + version.toString() + "," + err.getCode());

        ASAE_FSDKError error = mAgeEngine.ASAE_FSDK_InitAgeEngine(FaceDB.appid, FaceDB.age_key);
        Log.d(TAG, "ASAE_FSDK_InitAgeEngine =" + error.getCode());
        error = mAgeEngine.ASAE_FSDK_GetVersion(mAgeVersion);
        Log.d(TAG, "ASAE_FSDK_GetVersion:" + mAgeVersion.toString() + "," + error.getCode());

        ASGE_FSDKError error1 = mGenderEngine.ASGE_FSDK_InitgGenderEngine(FaceDB.appid, FaceDB.gender_key);
        Log.d(TAG, "ASGE_FSDK_InitgGenderEngine =" + error1.getCode());
        error1 = mGenderEngine.ASGE_FSDK_GetVersion(mGenderVersion);
        Log.d(TAG, "ASGE_FSDK_GetVersion:" + mGenderVersion.toString() + "," + error1.getCode());

        mFRAbsLoop = new FRAbsLoop();
        mFRAbsLoop.start();

        textToSpeechUtils = new TextToSpeechUtils(this);
    }

    private String crime_name = "";
    private int mCall_y = 0;
    private int mCall_n = 0;

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        mCall_y = 0;
        mCall_n = 0;
        crime_name = "";
        List<IsCallInfo> isCallInfoInfoToDB = IsCallInfoHelp.getIsCallInfoInfoToDB();
        if (isCallInfoInfoToDB != null && isCallInfoInfoToDB.size() > 0) {
            mCallAll.setText(isCallInfoInfoToDB.size() + "");
            for (IsCallInfo isCallInfo : isCallInfoInfoToDB) {
                if (isCallInfo.getIscall()) {
                    mCall_y++;
                } else {
                    mCall_n++;
                    crime_name = crime_name + isCallInfo.getCrime_name() + "、";
                }
            }
            mCallYes.setText(mCall_y + "");
            mCallNo.setText(mCall_n + "");
            rolltitle.setText("未点名人员:" + crime_name);
        }
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFRAbsLoop.shutdown();
        AFT_FSDKError err = engine.AFT_FSDK_UninitialFaceEngine();
        Log.d(TAG, "AFT_FSDK_UninitialFaceEngine =" + err.getCode());

        ASAE_FSDKError err1 = mAgeEngine.ASAE_FSDK_UninitAgeEngine();
        Log.d(TAG, "ASAE_FSDK_UninitAgeEngine =" + err1.getCode());

        ASGE_FSDKError err2 = mGenderEngine.ASGE_FSDK_UninitGenderEngine();
        Log.d(TAG, "ASGE_FSDK_UninitGenderEngine =" + err2.getCode());
    }

    @Override
    public Camera setupCamera() {
        mCamera = Camera.open(mCameraID);
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mWidth, mHeight);
            parameters.setPreviewFormat(mFormat);

            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                Log.d(TAG, "SIZE:" + size.width + "x" + size.height);
            }
            for (Integer format : parameters.getSupportedPreviewFormats()) {
                Log.d(TAG, "FORMAT:" + format);
            }

            List<int[]> fps = parameters.getSupportedPreviewFpsRange();
            for (int[] count : fps) {
                Log.d(TAG, "T:");
                for (int data : count) {
                    Log.d(TAG, "V=" + data);
                }
            }
            //parameters.setPreviewFpsRange(15000, 30000);
            //parameters.setExposureCompensation(parameters.getMaxExposureCompensation());
            //parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            //parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
            //parmeters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            //parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            //parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCamera != null) {
            mWidth = mCamera.getParameters().getPreviewSize().width;
            mHeight = mCamera.getParameters().getPreviewSize().height;
        }
        return mCamera;
    }

    @Override
    public void setupChanged(int format, int width, int height) {

    }

    @Override
    public boolean startPreviewLater() {

        return false;
    }


    @Override
    public Object onPreview(byte[] data, int width, int height, int format, long timestamp) {
        AFT_FSDKError err = engine.AFT_FSDK_FaceFeatureDetect(data, width, height, AFT_FSDKEngine.CP_PAF_NV21, result);
        Log.d(TAG, "AFT_FSDK_FaceFeatureDetect =" + err.getCode());
        Log.d(TAG, "Face=" + result.size());
        for (AFT_FSDKFace face : result) {
            Log.d(TAG, "Face:" + face.toString());
        }
        if (mImageNV21 == null) {
            if (!result.isEmpty()) {
                mAFT_FSDKFace = result.get(0).clone();
                mImageNV21 = data.clone();
            } else {
                mHandler.postDelayed(hide, 3000);
            }
        }
        //copy rects
        Rect[] rects = new Rect[result.size()];
        for (int i = 0; i < result.size(); i++) {
            rects[i] = new Rect(result.get(i).getRect());
        }
        //clear result.
        result.clear();
        //return the rects for render.
        return rects;
    }

    /**
     * 渲染之前？
     *
     * @param data
     */
    @Override
    public void onBeforeRender(CameraFrameData data) {

    }

    /**
     * 渲染之后？
     *
     * @param data
     */
    @Override
    public void onAfterRender(CameraFrameData data) {
        mGLSurfaceView.getGLES2Render().draw_rect((Rect[]) data.getParams(), Color.GREEN, 2);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CameraHelper.touchFocus(mCamera, event, v, this);
        return false;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            Log.d(TAG, "Camera Focus SUCCESS!");
        }
    }
}
