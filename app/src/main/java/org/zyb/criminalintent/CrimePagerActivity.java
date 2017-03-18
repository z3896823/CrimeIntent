package org.zyb.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.zyb.criminalintent.model.Crime;
import org.zyb.criminalintent.model.CrimeLab;
import org.zyb.criminalintent.util.SingleFragmentActivity;

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

        crimeList = CrimeLab.getCrimeLab(this).getCrimeList();

        FragmentManager fragmentManager = getSupportFragmentManager();

        vp_container.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                // 这个position从哪来的？
                Crime crime = crimeList.get(position);
                return CrimeDetailFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return crimeList.size();
            }
        });


        UUID crimeId = (UUID) getIntent().getSerializableExtra("crimeId");
        for (int i = 0; i<crimeList.size();i++){
            if (crimeList.get(i).getId().equals(crimeId)){
                vp_container.setCurrentItem(i);
                break;
            }
        }
    }
}
