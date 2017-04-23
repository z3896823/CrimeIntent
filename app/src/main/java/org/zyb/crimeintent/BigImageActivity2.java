package org.zyb.crimeintent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.zyb.crimeintent.model.Crime;
import org.zyb.crimeintent.model.CrimeManager;
import org.zyb.crimeintent.util.Utility;

import java.io.FileNotFoundException;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/04/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BigImageActivity2 extends AppCompatActivity {

    public static Intent newIntent(Context context, Long crimeId){
        Intent intent = new Intent(context,BigImageActivity2.class);
        intent.putExtra("crimeId",crimeId);
        return intent;
    }

    private Crime crime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        crime = CrimeManager.getCrimeManager().getCrimeById(getIntent().getLongExtra("crimeId",0));
        Log.d("ybz", "load with Bitmap. imageLoc = "+ crime.getImageLoc());

        ImageView iv_bigImage = (ImageView) findViewById(R.id.id_iv_bigImage);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(Uri.parse(crime.getImageLoc())));
            bitmap = Utility.setScaleBitmap(bitmap,2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        iv_bigImage.setImageBitmap(bitmap);
    }
}
