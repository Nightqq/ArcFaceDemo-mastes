package com.arcsoft.sdk_demo.utils.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.arcsoft.sdk_demo.utils.bean.IsCallInfo;
import com.arcsoft.sdk_demo.utils.bean.PrisonerInfo;

import com.arcsoft.sdk_demo.utils.dao.IsCallInfoDao;
import com.arcsoft.sdk_demo.utils.dao.PrisonerInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig isCallInfoDaoConfig;
    private final DaoConfig prisonerInfoDaoConfig;

    private final IsCallInfoDao isCallInfoDao;
    private final PrisonerInfoDao prisonerInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        isCallInfoDaoConfig = daoConfigMap.get(IsCallInfoDao.class).clone();
        isCallInfoDaoConfig.initIdentityScope(type);

        prisonerInfoDaoConfig = daoConfigMap.get(PrisonerInfoDao.class).clone();
        prisonerInfoDaoConfig.initIdentityScope(type);

        isCallInfoDao = new IsCallInfoDao(isCallInfoDaoConfig, this);
        prisonerInfoDao = new PrisonerInfoDao(prisonerInfoDaoConfig, this);

        registerDao(IsCallInfo.class, isCallInfoDao);
        registerDao(PrisonerInfo.class, prisonerInfoDao);
    }
    
    public void clear() {
        isCallInfoDaoConfig.clearIdentityScope();
        prisonerInfoDaoConfig.clearIdentityScope();
    }

    public IsCallInfoDao getIsCallInfoDao() {
        return isCallInfoDao;
    }

    public PrisonerInfoDao getPrisonerInfoDao() {
        return prisonerInfoDao;
    }

}
