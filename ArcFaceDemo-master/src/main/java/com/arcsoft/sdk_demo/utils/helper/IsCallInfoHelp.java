package com.arcsoft.sdk_demo.utils.helper;


import android.util.Log;

import com.arcsoft.sdk_demo.utils.bean.IsCallInfo;
import com.arcsoft.sdk_demo.utils.dao.IsCallInfoDao;

import java.util.List;

public class IsCallInfoHelp {
    private static IsCallInfoDao isCallInfoDao = DaoManager.getDaoSession().getIsCallInfoDao();
    public static void saveIsCallInfoToDB(IsCallInfo isCallInfo) {
        Log.e("储存的犯人数据",isCallInfo.toString());
        if (isCallInfo != null) {
            isCallInfoDao.insertOrReplace(isCallInfo);
        }
    }
    public static List<IsCallInfo> getIsCallInfoInfoToDB() {
        return isCallInfoDao.queryBuilder().list();
    }

}
