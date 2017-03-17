package org.zyb.criminalintent.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/16
 *     desc   : 单例模式，模型类
 *     version: 1.0
 * </pre>
 *
 * 注意，这里的设计是想把该类对象作为单例，但是其他方法却是通过对象调用的方式来访问
 * 注意到只有getCrimeLab是静态方法，另外两个get不是静态的，必须通过对象来调用
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;//注意哦，这是个本类对象,作用相当于一个manager类

    private List<Crime> crimeList;

    private CrimeLab(Context context){
        crimeList = new ArrayList<>();

        for (int i = 0;i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("crime #"+(i+1));
            crime.setSolved(i%2 == 0);
            crimeList.add(crime);
        }
    }

    public static CrimeLab getCrimeLab(Context context){
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimeList(){
        return crimeList;
    }

    public Crime getCrime(UUID uuid) {
        for (Crime crime : crimeList) {
            if (crime.getId().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }
}
