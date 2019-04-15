package com.arcsoft.sdk_demo.utils.helper;

import android.util.Log;

import com.arcsoft.sdk_demo.utils.bean.PrisonerInfo;
import com.arcsoft.sdk_demo.utils.dao.PrisonerInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class PrisonerInfoHelp {

    private static PrisonerInfoDao prisonerInfoDao = DaoManager.getDaoSession().getPrisonerInfoDao();

    public static void savePrisonerInfoToDB(PrisonerInfo prisonerInfo) {
        Log.e("储存的犯人数据", prisonerInfo.toString());
        if (prisonerInfo != null) {
            prisonerInfoDao.insertOrReplace(prisonerInfo);
        }
    }

    public static List<PrisonerInfo> getPrisonerInfoToDB() {
        return prisonerInfoDao.queryBuilder().list();
    }

    public static PrisonerInfo getprisoner(String name) {
        QueryBuilder queryBuilder = prisonerInfoDao.queryBuilder();
        queryBuilder.where(PrisonerInfoDao.Properties.Crime_name.eq(name));
        List list = queryBuilder.list();
        if (list!=null&&list.size()>0){
            return (PrisonerInfo) list.get(queryBuilder.list().size() - 1);
        }else {
            return null;
        }

    }

    public static void deleteByName(String name) {
        PrisonerInfo getprisoner = getprisoner(name);
        if (getprisoner!=null){
            prisonerInfoDao.delete(getprisoner);
        }
    }


}
