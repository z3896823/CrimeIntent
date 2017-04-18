package org.zyb.crimeintent.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Utility {

    @SuppressWarnings("all")
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将数据库取得的日期字符串解析成格式化的Date对象
     * @param dateString dateString from the database
     * @return
     */
    public static Date stringToDate(String dateString){
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Date对象转换成格式化的String
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        return sdf.format(date);
    }

    /**
     * 获得String类型的当前时间
     * @return
     */
    public static String getNowTime(){
        return sdf.format(new Date());
    }
}
