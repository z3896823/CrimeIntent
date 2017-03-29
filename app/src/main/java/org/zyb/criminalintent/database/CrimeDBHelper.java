package org.zyb.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/28
 *     desc   : 一个通用的数据库操作类，并不跟此demo绑定
 *     version: 1.0
 * </pre>
 */

public class CrimeDBHelper extends SQLiteOpenHelper {

    private String createTable;
    private Context context;

    public CrimeDBHelper(Context context, String dbName, int version, String createTable){
        super(context, dbName,null,version);
        this.context = context;
        this.createTable = createTable;
    }

    //该方法会在调用getWritableDatabase的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
        Toast.makeText(context, "database created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Toast.makeText(context, "database upgraded", Toast.LENGTH_SHORT).show();
    }
}
