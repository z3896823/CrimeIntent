package org.zyb.crimeintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.zyb.crimeintent.dao.Crime;
import org.zyb.crimeintent.dao.CrimeManager;
import org.zyb.crimeintent.fragment.CrimeDetailFragment;

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

    private static final String TAG = "ybz";
    private ViewPager vp_container;

    private CrimeManager crimeManager ;

    private List<Crime> crimeList;

    public static Intent newIntent(Context context, Long id){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra("crimeId",id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crimepager);
        vp_container = (ViewPager) findViewById(R.id.id_vp_container);

        crimeManager = CrimeManager.getCrimeManager();
        crimeList = crimeManager.getCrimeList();

        // setAdapter
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = crimeList.get(position);
                return CrimeDetailFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return crimeList.size();
            }

            @Override
            public int getItemPosition(Object object){
                return PagerAdapter.POSITION_NONE;
            }
        };
        vp_container.setAdapter(adapter);

        // setCurrentItem
        Long id = getIntent().getLongExtra("crimeId",0);
        for (int i = 0; i<crimeList.size();i++){
            if (crimeList.get(i).getId().equals(id)){
                vp_container.setCurrentItem(i);
                break;
            }
        }
    }

    /**
     * 当一页Pager被删除后，通知adapter
     * @param crime 要删除的item的UUID
     */
    public void onItemDeleted(Crime crime){
        crimeManager.deleteCrime(crime);
        vp_container.getAdapter().notifyDataSetChanged();
    }
}
