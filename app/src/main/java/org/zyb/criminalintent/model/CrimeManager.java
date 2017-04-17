package org.zyb.criminalintent.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.zyb.criminalintent.database.CrimeDBHelper;
import org.zyb.criminalintent.database.CursorParser;
import org.zyb.criminalintent.util.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.SimpleFormatter;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/16
 *     desc   : 模型类的控制类，使用单例模式，作为程序与数据库沟通的桥梁，负责维护数据库中的数据和内存中的数据的一致性
 *     version: 2.0
 * </pre>
 *
 * 注意，这里的设计是想把该类对象作为单例，但是其他方法却是通过对象调用的方式来访问
 * 注意到只有getCrimeLab是静态方法，另外两个get不是静态的，必须通过对象来调用
 *
 * 该类对数据库提交程序对数据的修改，同时也负责维护内存中数据的变化，保持和数据库的一致性。
 *
 */

public class CrimeManager {

    private static final String TAG = "ybz";
    private static CrimeManager mCrimeManager; // memory leak here

    private List<Crime> crimeList = new ArrayList<>();

    private SQLiteDatabase crimeDB;

    private static final String DB_NAME = "crimeDB";
    private int version = 1;
    private String createTable = "create table Crime (" +
            "id integer primary key autoincrement," +
            "UUID text," +
            "title text," +
            "date text," +
            "isSolved int)";

    /**
     * 构造函数
     * @param context
     */
    private CrimeManager(Context context){
        // create or get database here
        crimeDB = new CrimeDBHelper(context,DB_NAME,version,createTable).getWritableDatabase();
    }

    /**
     * 单例的入口
     * @param context
     * @return
     */
    public static CrimeManager getCrimeManager(Context context){
        if (mCrimeManager == null){
            mCrimeManager = new CrimeManager(context);
        }
        return mCrimeManager;
    }

    //强制从数据库取数据
    public List<Crime> getCrimeListFromDB(){
        Cursor cursor = crimeDB.rawQuery("select * from Crime",null);
        List<Crime> crimeList1 = CursorParser.getCrimeList(cursor);
        cursor.close();
        return crimeList1;
    }

    /**
     * 获取CrimeList
     * 首次从数据库中读取，之后返回内存中的数据
     * @return crimeList
     */
    public List<Crime> getCrimeList(){
        if (crimeList.size() == 0) {
            // query database here
            Cursor cursor = crimeDB.rawQuery("select * from Crime",null);
            crimeList = CursorParser.getCrimeList(cursor);
            cursor.close();
            // 如果数据库为空，对列表执行初始化，但是该初始化并不向数据库写入数据
            if (crimeList.isEmpty()){
                Crime crime = new Crime();
                crime.setTitle("this is a title");
                crime.setDate(Utility.getNowTime());
                crime.setSolved(true);
                crimeList.add(crime);
            }
            Log.d(TAG, "CrimeList get from database:");
            for (Crime crime:crimeList){
                Log.d(TAG, "crime title: "+ crime.getTitle()+ "  uuid: "+crime.getUuid().toString());
            }
        }

        return crimeList;
    }

    /**
     * 创建新的Crime对象，并同步更新数据库和内存中的数据
     * @return UUID of new Crime
     */
    public UUID addCrime(){
        // add into database and update crimeList in memory
        Crime crime = new Crime();
        crimeList.add(crime);
        crimeDB.execSQL("insert into Crime (UUID, title, date ,isSolved) values (?,?,?,?)",
                new String[]{crime.getUuid().toString(),"",crime.getDate(),"0"});
        Log.d(TAG, crime.getTitle()+ " is added to database by CrimeManager");
        return crime.getUuid();
    }


    /**
     * 根据UUID删除Crime对象
     * @param uuid
     */
    public void deleteCrime(UUID uuid){
        // 删除数据库中的数据
        crimeDB.execSQL("delete from crime where uuid = ? ",new String[]{uuid.toString()});
        // 更新内存中的crimeList
        Crime crime = getCrime(uuid);
        crimeList.remove(crime);
    }

    /**
     * 根据UUID查询列表中的Crime对象并返回
     * @param uuid
     * @return
     */
    public Crime getCrime(UUID uuid) {
        //为了防止上一个活动被回收后此处出现空指针，先判断下crimeList还在不在内存中
        if (crimeList == null){
            crimeList = getCrimeList();
        }
        for (Crime crime : crimeList) {
            if (crime.getUuid().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }

    /**
     * 根据title查询列表中的Crime对象并返回
     * @param title
     * @return
     */
    public Crime getCrime(String title){
        for (Crime crime:crimeList){
            if (crime.getTitle().equals(title)){
                return crime;
            }
        }
        return null;
    }

    /**
     * 根据UUID更改对应对象的title
     * @param uuid
     * @param title
     */
    public void changeCrimeTitle(UUID uuid,String title){
        crimeDB.execSQL("update crime set title = ? where UUID = ?",new String[]{title,uuid.toString()});
        //更新内存中的crimeList
        Crime crime = getCrime(uuid);
        crime.setTitle(title);
        Log.d(TAG, uuid.toString()+ "   title change to:   "+ title);
    }

    /**
     * 根据UUID更改对应对象的isSolved状态
     * @param uuid
     * @param isSolved
     */
    public void changeCrimeSolved(UUID uuid,boolean isSolved){
        if (isSolved){
            crimeDB.execSQL("update crime set isSolved = ? where UUID = ?",new String[]{"1",uuid.toString()});
        } else {
            crimeDB.execSQL("update crime set isSolved = ? where UUID = ?",new String[]{"0",uuid.toString()});
        }
        Crime crime = getCrime(uuid);
        crime.setSolved(isSolved);
    }

    /**
     * 根据UUID更改对应对象的date
     * @param uuid
     * @param date
     */
    public void changeCrimeDate(UUID uuid,String date){
        crimeDB.execSQL("update Crime set date = ? where UUID = ?",new String[]{date,uuid.toString()});
        Crime crime = getCrime(uuid);
        crime.setDate(date);
    }

}
