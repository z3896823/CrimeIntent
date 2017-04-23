package org.zyb.crimeintent.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

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

    /**
     * 对Bitmap进行缩放，防止出现如下错误：
     * Bitmap too large to be uploaded into a texture (3120x4160, max=4096x4096)
     * @param photo  数据源
     * @param SCALE  缩放倍数
     * @return 缩放后的Bitmap
     */
    public static Bitmap setScaleBitmap(Bitmap photo, int SCALE) {
        if (photo != null) {
            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
            //这里缩小了1/2,但图片过大时仍然会出现加载不了,但系统中一个BITMAP最大是在10M左右,我们可以根据BITMAP的大小
            //根据当前的比例缩小,即如果当前是15M,那如果定缩小后是6M,那么SCALE= 15/6
            Bitmap smallBitmap = zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
            //释放原始图片占用的内存，防止out of memory异常发生
            photo.recycle();
            return smallBitmap;
        }
        return null;
    }

    private static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
}
