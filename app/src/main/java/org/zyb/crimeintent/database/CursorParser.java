package org.zyb.crimeintent.database;

import android.database.Cursor;

import org.zyb.crimeintent.model.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/29
 *     desc   : 对查询数据库获得的Cursor对象进行处理，返回一个CrimeList对象
 *     version: 1.0
 * </pre>
 */

public class CursorParser {

    public static List<Crime> getCrimeList(Cursor cursor){
        List<Crime> crimeList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Crime crime = new Crime();
                // setUuid
                UUID uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex("UUID")));
                crime.setUuid(uuid);
                // setDate
                String s = cursor.getString(cursor.getColumnIndex("date"));
                crime.setDate(s);
                // setTitle
                String title = cursor.getString(cursor.getColumnIndex("title"));
                crime.setTitle(title);
                // setSolved
                int isSolved = cursor.getInt(cursor.getColumnIndex("isSolved"));
                if (isSolved == 0){
                    crime.setSolved(false);
                } else {
                    crime.setSolved(true);
                }
                // add to list
                crimeList.add(crime);
            } while (cursor.moveToNext());
        }
        return crimeList;
    }
}
