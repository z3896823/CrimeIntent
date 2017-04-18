package org.zyb.crimeintent;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;

import org.zyb.crimeintent.dao.DaoMaster;
import org.zyb.crimeintent.dao.DaoSession;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/04/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MyApplication extends Application {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        initDatabase();
    }

    private void initDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"crime.db",null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession (){
        return daoSession;
    }
}
