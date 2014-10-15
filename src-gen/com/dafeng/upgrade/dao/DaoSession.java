package com.dafeng.upgrade.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.dafeng.upgrade.dao.TbImgs;

import com.dafeng.upgrade.dao.TbImgsDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig tbImgsDaoConfig;

    private final TbImgsDao tbImgsDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        tbImgsDaoConfig = daoConfigMap.get(TbImgsDao.class).clone();
        tbImgsDaoConfig.initIdentityScope(type);

        tbImgsDao = new TbImgsDao(tbImgsDaoConfig, this);

        registerDao(TbImgs.class, tbImgsDao);
    }
    
    public void clear() {
        tbImgsDaoConfig.getIdentityScope().clear();
    }

    public TbImgsDao getTbImgsDao() {
        return tbImgsDao;
    }

}
