package com.arcsoft.sdk_demo.utils.helper;


import android.util.Log;

import com.arcsoft.sdk_demo.utils.bean.IsCallInfo;
import com.arcsoft.sdk_demo.utils.dao.IsCallInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class IsCallInfoHelp {
    private static IsCallInfoDao isCallInfoDao = DaoManager.getDaoSession().getIsCallInfoDao();

    public static void saveIsCallInfoToDB(IsCallInfo isCallInfo) {
        // Log.e("储存的犯人数据", isCallInfo.toString());
        if (isCallInfo != null) {
            isCallInfoDao.insertOrReplace(isCallInfo);
        }
    }

    public static List<IsCallInfo> getIsCallInfoInfoToDB() {
        return isCallInfoDao.queryBuilder().list();
    }

    public static void deleteByName(String name) {
        QueryBuilder<IsCallInfo> isCallInfoQueryBuilder = isCallInfoDao.queryBuilder();
        isCallInfoQueryBuilder.where(IsCallInfoDao.Properties.Crime_name.eq(name));
        if (isCallInfoQueryBuilder.list() != null && isCallInfoQueryBuilder.list().size() > 0) {
            IsCallInfo callInfo = isCallInfoQueryBuilder.list().get(0);
            if (callInfo != null) {
                isCallInfoDao.delete(callInfo);
            }
        }
    }

    public static void deleteALl() {
        isCallInfoDao.deleteAll();
    }


    //传入姓名返回是否点名
    public static IsCallInfo isCall(String name) {
        QueryBuilder<IsCallInfo> isCallInfoQueryBuilder = isCallInfoDao.queryBuilder();
        isCallInfoQueryBuilder.where(IsCallInfoDao.Properties.Crime_name.eq(name));
        List<IsCallInfo> list = isCallInfoQueryBuilder.list();
        IsCallInfo isCallInfo;
        if (list != null && list.size() > 0) {
            isCallInfo = list.get(0);
            return isCallInfo;
        } else {
            return null;
        }
    }


}
