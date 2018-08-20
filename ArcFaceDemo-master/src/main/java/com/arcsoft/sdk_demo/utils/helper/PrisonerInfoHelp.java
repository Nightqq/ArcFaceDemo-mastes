package com.arcsoft.sdk_demo.utils.helper;

import android.util.Log;

import com.arcsoft.sdk_demo.utils.bean.PrisonerInfo;
import com.arcsoft.sdk_demo.utils.dao.PrisonerInfoDao;

import java.util.List;

public class PrisonerInfoHelp {

    private static PrisonerInfoDao prisonerInfoDao = DaoManager.getDaoSession().getPrisonerInfoDao();

    public static void savePrisonerInfoToDB(PrisonerInfo prisonerInfo) {
        Log.e("储存的犯人数据",prisonerInfo.toString());
        if (prisonerInfo != null) {
            prisonerInfoDao.insertOrReplace(prisonerInfo);
        }
    }
    public static List<PrisonerInfo> getPrisonerInfoToDB() {
        return prisonerInfoDao.queryBuilder().list();
    }
}
