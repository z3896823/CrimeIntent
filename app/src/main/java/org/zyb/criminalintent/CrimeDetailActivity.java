package org.zyb.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import org.zyb.criminalintent.fragment.CrimeDetailFragment;
import org.zyb.criminalintent.util.SingleFragmentActivity;

import java.util.UUID;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/17
 *     desc   : 托管CrimeDetailFragment
 *     version: 1.0
 * </pre>
 */

public class CrimeDetailActivity extends SingleFragmentActivity {

    /**
     * 根据Fragment需要的信息创建Fragment并返回给该Activity的onCreate方法
     * 使得要托管的Fragment在创立之初就获得了所需数据，避免了二者的交互
     * @return a Fragment with the data it needs to onCreate()
     */
    @Override
    protected Fragment createFragment(){
        UUID uuid = (UUID) getIntent().getSerializableExtra("crimeId");
        return CrimeDetailFragment.newInstance(uuid);
    }


    /**
     * 该静态方法向外声明本Activity需要获得什么样的数据，增强分工开发的信息透明性
     * 该方法可以有多个同名方法的重载
     * @param context the context to start this activity
     * @param uuid the data that this activity may need
     * @return a complete Intent to start this activity
     */
    public static Intent newIntent(Context context, UUID uuid){
        Intent intent = new Intent(context,CrimeDetailActivity.class);
        intent.putExtra("crimeId",uuid);
        return intent;
    }


}
