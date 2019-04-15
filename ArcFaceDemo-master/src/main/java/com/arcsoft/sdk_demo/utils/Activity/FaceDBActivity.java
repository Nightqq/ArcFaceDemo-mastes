package com.arcsoft.sdk_demo.utils.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arcsoft.sdk_demo.R;
import com.arcsoft.sdk_demo.utils.bean.PrisonerInfo;
import com.arcsoft.sdk_demo.utils.helper.IsCallInfoHelp;
import com.arcsoft.sdk_demo.utils.helper.PrisonerInfoHelp;
import com.guo.android_extend.widget.ExtImageView;
import com.guo.android_extend.widget.HListView;

/**
 * Created by Administrator on 2018/4/2.
 * 人脸库
 */

public class FaceDBActivity extends Activity {

    private RegisterViewAdapter mRegisterViewAdapter;
    private HListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facedb);
        mRegisterViewAdapter = new RegisterViewAdapter(this);
        mListView = (HListView) findViewById(R.id.hlistView1);
        mListView.setAdapter(mRegisterViewAdapter);
        mListView.setOnItemClickListener(mRegisterViewAdapter);
    }

    class Holder {
        ExtImageView siv;
        TextView tv;
    }

    class RegisterViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
        Context mContext;
        LayoutInflater mLInflater;

        public RegisterViewAdapter(Context c) {
            // TODO Auto-generated constructor stub
            mContext = c;
            mLInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return ((Application) mContext.getApplicationContext()).mFaceDB.mRegister.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = null;
            if (convertView != null) {
                holder = (Holder) convertView.getTag();
            } else {
                convertView = mLInflater.inflate(R.layout.item_sample, null);
                holder = new Holder();
                holder.siv = (ExtImageView) convertView.findViewById(R.id.imageView1);
                holder.tv = (TextView) convertView.findViewById(R.id.textView1);
                convertView.setTag(holder);
            }

            if (!((Application) mContext.getApplicationContext()).mFaceDB.mRegister.isEmpty()) {
                FaceDB.FaceRegist face = ((Application) mContext.getApplicationContext()).mFaceDB.mRegister.get(position);
                holder.tv.setText(face.mName);
                //Log.i("每个名字的人脸个数：",""+face.mFaceList.size());
                //Bitmap bitmap = BitmapFactory.decodeByteArray(face.mFaceList.get(0).getFeatureData(), 0, face.mFaceList.get(0).getFeatureData().length);
                //holder.siv.setImageBitmap(bitmap);
                convertView.setWillNotDraw(false);
            }

            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("onItemClick", "onItemClick = " + position + "pos=" + mListView.getScroll());
            final String name = ((Application) mContext.getApplicationContext()).mFaceDB.mRegister.get(position).mName;
            final int count = ((Application) mContext.getApplicationContext()).mFaceDB.mRegister.get(position).mFaceList.size();
            new AlertDialog.Builder(FaceDBActivity.this)
                    .setTitle("删除注册名:" + name)
                    .setMessage("包含:" + count + "个注册人脸特征信息")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Application) mContext.getApplicationContext()).mFaceDB.delete(name);
                            PrisonerInfoHelp.deleteByName(name);
                            IsCallInfoHelp.deleteByName(name);
                            mRegisterViewAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
