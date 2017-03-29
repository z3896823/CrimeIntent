package org.zyb.criminalintent.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.zyb.criminalintent.database.CrimeDBHelper;
import org.zyb.criminalintent.database.CursorParser;
import org.zyb.criminalintent.util.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.SimpleFormatter;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/16
 *     desc   : 模型类的控制类，使用单例模式
 *     version: 1.0
 * </pre>
 *
 * 注意，这里的设计是想把该类对象作为单例，但是其他方法却是通过对象调用的方式来访问
 * 注意到只有getCrimeLab是静态方法，另外两个get不是静态的，必须通过对象来调用
 *
 * 在本demo中，当数据发生变化时，永远是先更新数据(先set)，再获取数据(get),而不是直接使用更改过的数据
 */

public class CrimeManager {

    private static CrimeManager sCrimeManager; // memory leak here

    private List<Crime> crimeList;

    private Context mContext;// an application level context

    private SQLiteDatabase crimeDB;

    private static final String DB_NAME = "crimeDB";
    private int version = 1;
    private String createTable = "create table Crime (" +
            "id integer primary key autoincrement," +
            "UUID text," +
            "title text," +
            "date text," +
            "isSolved int)";

    private CrimeManager(Context context){
        // create or get database here
        mContext = context.getApplicationContext();
        crimeDB = new CrimeDBHelper(context,DB_NAME,version,createTable).getWritableDatabase();
    }

    public static CrimeManager getCrimeManager(Context context){
        if (sCrimeManager == null){
            sCrimeManager = new CrimeManager(context);
        }
        return sCrimeManager;
    }

    public List<Crime> getCrimeList(){
        // query database here
        Cursor cursor = crimeDB.rawQuery("select * from Crime",null);
        crimeList = CursorParser.getCrimeList(cursor);
        //首次载入时进行列表的初始化，但是该初始化并未向数据库写入数据
        if (crimeList.isEmpty()){
            Crime crime = new Crime();
            crime.setTitle("this is a title");
            crime.setDate(Utility.getNowTime());
            crime.setSolved(true);
            crimeList.add(crime);
        }
        return crimeList;
    }
    //增
    public void addCrime(Crime crime){
        // add into database and query again
        UUID uuid = crime.getUuid();
        String title = crime.getTitle();

        String date = crime.getDate();

        if (crime.getSolved()) {
            crimeDB.execSQL("insert into Crime (UUID, title, date ,isSolved) values (?,?,?,?)",
                    new String[]{uuid.toString(),title,date,"1"});
        } else {
            crimeDB.execSQL("insert into Crime (UUID, title, date ,isSolved) values (?,?,?,?) ",
                    new String[]{uuid.toString(),title,date,"0"});
        }
        // 更新crimeList
        crimeList = getCrimeList();
    }
    //删
    public void deleteCrime(UUID crimeId){
        // delete a crime record
        crimeDB.execSQL("delete from crime where uuid = ?",new String[]{crimeId.toString()});
        // 更新crimeList
        crimeList = getCrimeList();
    }
    //查
    public Crime getCrime(UUID uuid) {
        // 很明显，getCrime前必须先getCrimeList
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
    //改title
    public void changeCrimeTitle(UUID uuid,String s){
        crimeDB.execSQL("update crime set title = ? where UUID = ?",new String[]{s,uuid.toString()});
    }
    //改isSolved
    public void changeCrimeSolved(UUID uuid,boolean isSolved){
        if (isSolved){
            crimeDB.execSQL("update crime set isSolved = ? where UUID = ?",new String[]{"1",uuid.toString()});
        } else {
            crimeDB.execSQL("update crime set isSolved = ? where UUID = ?",new String[]{"0",uuid.toString()});
        }
    }
    //改date
    public void changeCrimeDate(UUID uuid,String date){
        crimeDB.execSQL("update Crime set date = ? where UUID = ?",new String[]{date,uuid.toString()});
    }

}
