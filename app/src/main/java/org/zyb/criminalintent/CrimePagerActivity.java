package org.zyb.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.zyb.criminalintent.fragment.CrimeDetailFragment;
import org.zyb.criminalintent.model.Crime;
import org.zyb.criminalintent.model.CrimeManager;

import java.util.List;
import java.util.UUID;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/18
 *     desc   : 本Activity不跟特定的Fragment绑定，所以不用继承SingleFragmentActivity
 *     version: 1.0
 * </pre>
 */

public class CrimePagerActivity extends AppCompatActivity {

    private ViewPager vp_container;

    private List<Crime> crimeList;

    public static Intent newIntent(Context context, UUID uuid){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra("crimeId",uuid);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crimepager);

        vp_container = (ViewPager) findViewById(R.id.id_vp_container);

        crimeList = CrimeManager.getCrimeManager(this).getCrimeList();

        FragmentManager fragmentManager = getSupportFragmentManager();

        vp_container.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = crimeList.get(position);
                //根据Fragment的特性，每次会实例化三个Fragment，先实例化本身，在分别实例化左边和右边的
                return CrimeDetailFragment.newInstance(crime.getUuid());
            }

            @Override
            public int getCount() {
                return crimeList.size();
            }
        });

        UUID crimeId = (UUID) getIntent().getSerializableExtra("crimeId");
        for (int i = 0; i<crimeList.size();i++){
            if (crimeList.get(i).getUuid().equals(crimeId)){
                vp_container.setCurrentItem(i);
                break;
            }
        }
    }
}
