package org.zyb.criminalintent.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.zyb.criminalintent.R;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/17
 *     desc   : 专门用来生成托管碎片的活动的抽象类
 *     version: 1.0
 * </pre>
 *
 * 由于所有的活动类都只是为了托管碎片，那么就可以把他们的共同代码抽象出来封装一下
 *
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.id_fl_container);

        if(fragment == null){
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.id_fl_container,fragment).commit();
        }
    }
}
