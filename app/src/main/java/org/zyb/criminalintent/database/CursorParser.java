package org.zyb.criminalintent.database;

import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import org.zyb.criminalintent.model.Crime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @SuppressWarnings("all")
    public static List<Crime> getCrimeList(Cursor cursor){
        List<Crime> crimeList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Crime crime = new Crime();
                // setUuid
                UUID uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex("UUID")));
                crime.setUuid(uuid);
                // setDate
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = sdf.parse(cursor.getString(cursor.getColumnIndex("date")));
                    crime.setDate(date);
                } catch (ParseException e) {
                    Log.d("ybz", "getCrime: date parse error");
                }
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
