package com.arcsoft.sdk_demo.utils.Utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by admin on 2017/11/19.
 * 声音播放类
 */

public class AudioPlayUtils {
    Context mContext = null;
    AudioManager audioManager;
    MediaPlayer playerSound;
    Thread playThread;
    private boolean isRun = true;

    /**
     * 播放声音的资源ID
     */
    private int mRawId;
    private boolean isLoop;
    private boolean isPLayComplete;
    public AudioPlayUtils(Context context, int rawId) {
        mContext = context;
        mRawId = rawId;
        isPLayComplete=false;
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMicrophoneMute(false);
        audioManager.setSpeakerphoneOn(true);
        //使用扬声器播放，即使已经插入耳机
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    }
    class PlayThread implements Runnable {
        @Override
        public void run() {
            if (isRun) {
                if (audioManager.isSpeakerphoneOn()) {
                  //  LogUtils.e("扬声器打开了1");
                } else {
                    audioManager.setSpeakerphoneOn(false);
                 //   LogUtils.e("扬声器关闭了");
                    if (audioManager.isSpeakerphoneOn()) {
                      //  LogUtils.e("扬声器打开了2");
                    } else {
                      //  LogUtils.e("扬声器还是没打开");
                    }
                }
                playerSound = MediaPlayer.create(mContext, mRawId);
                if (isLoop) {
                    playerSound.setLooping(true);
                }
                playerSound.start();
                playerSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        isplay=false;
                        if (playerSound!=null){
                            playerSound.release();
                        }
                       // LogUtils.a( isPLayComplete);
                    }
                });
            }
        }
    }
    public boolean isPLayComplete() {//循环播放是否运行
        return isPLayComplete;
    }
    public void setPLayComplete(boolean PLayComplete) {
        isPLayComplete = PLayComplete;
    }
    public void play() {
        playThread = new Thread(new PlayThread());
        playThread.run();
        isplay=true;
    }
    private boolean isplay = true;
    public boolean isPlaying(){
        return isplay;
    }

    public void play(boolean flag) {
        isPLayComplete = true;
        isLoop = flag;
        play();
    }
    public void stop() {
        isRun = false;
        isPLayComplete = false;
        if (playerSound != null) {
            if (playerSound.isLooping()) {
                playerSound.setLooping(false);
            }
            playerSound.stop();
        }
        if (playThread.isAlive()){
            playThread.interrupt();
        }
        audioManager = null;
        playerSound = null;
    }
}
