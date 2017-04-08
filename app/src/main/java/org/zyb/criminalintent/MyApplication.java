package org.zyb.criminalintent;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/04/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
