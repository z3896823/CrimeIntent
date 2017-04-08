package org.zyb.criminalintent;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    private static final String TAG = "ybz";
    private ViewPager vp_container;

    private CrimeManager crimeManager ;

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

        crimeManager = CrimeManager.getCrimeManager(this);
        crimeList = crimeManager.getCrimeList();
        vp_container = (ViewPager) findViewById(R.id.id_vp_container);

        // create adapter
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = crimeList.get(position);
                return CrimeDetailFragment.newInstance(crime.getUuid());
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

        // tell viewPager where to start
        UUID crimeId = (UUID) getIntent().getSerializableExtra("crimeId");
        List<Crime> crimeList = crimeManager.getCrimeList();
        for (int i = 0; i<crimeList.size();i++){
            if (crimeList.get(i).getUuid().equals(crimeId)){
                vp_container.setCurrentItem(i);
                break;
            }
        }
    }

    /**
     * 当一页Pager被删除后，通知adapter
     * @param uuid 要删除的item的UUID
     */
    public void onItemDeleted(UUID uuid){
        crimeManager.deleteCrime(uuid);
        vp_container.getAdapter().notifyDataSetChanged();
    }
}
