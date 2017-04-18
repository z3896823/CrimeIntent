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

    public void addCrime(Crime crime){
        crime.setDate(Utility.dateToString(new Date()));
        crimeList.add(crime);
        MyApplication.getDaoSession().getCrimeDao().insert(crime);
    }

    public void deleteCrime(Crime crime){
        crimeList.remove(crime);
        MyApplication.getDaoSession().getCrimeDao().delete(crime);
    }

    public void updateCrime(Crime crime){
        MyApplication.getDaoSession().getCrimeDao().update(crime);
    }

    public Crime getCrimeById(Long id){
        return MyApplication.getDaoSession().getCrimeDao().load(id);
    }

    private List<Crime> getCrimeListFromDB(){
        return MyApplication.getDaoSession().getCrimeDao().loadAll();
    }
}