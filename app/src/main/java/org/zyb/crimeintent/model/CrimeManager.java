package org.zyb.crimeintent.model;

import org.zyb.crimeintent.MyApplication;
import org.zyb.crimeintent.util.Utility;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/04/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CrimeManager {

    private List<Crime> crimeList;
    private static CrimeManager crimeManager;

    private CrimeManager(){
    }

    public static CrimeManager getCrimeManager(){
        if (crimeManager == null){
            crimeManager = new CrimeManager();
        }
        return crimeManager;
    }

    public List<Crime> getCrimeList() {
        if (crimeList == null){
            crimeList = getCrimeListFromDB();
        }
        return crimeList;
    }

    /**
     * 添加数据
     *
     * 内存中和数据库中同步添加
     */
    public void addCrime(Crime crime){
        crime.setDate(Utility.dateToString(new Date()));
        crimeList.add(crime);
        MyApplication.getDaoSession().getCrimeDao().insert(crime);
    }

    /**
     * 删除数据
     *
     * 内存中和数据库中同步删除
     */
    public void deleteCrime(Crime crime){
        crimeList.remove(crime);
        MyApplication.getDaoSession().getCrimeDao().delete(crime);
    }

    /**
     * 更新数据
     *
     * 内存中的数据本身就已改变，不需要重复更新
     */
    public void updateCrime(Crime crime){
        MyApplication.getDaoSession().getCrimeDao().update(crime);
    }

    /**
     * 根据id返回Crime对象
     */
    public Crime getCrimeById(Long id){
        return MyApplication.getDaoSession().getCrimeDao().load(id);
    }

    /**
     * 从数据库中获取完整数据
     */
    private List<Crime> getCrimeListFromDB(){
        return MyApplication.getDaoSession().getCrimeDao().loadAll();
    }
}
