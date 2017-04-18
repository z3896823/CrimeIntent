package org.zyb.crimeintent;

import android.support.v4.app.Fragment;

import org.zyb.crimeintent.fragment.CrimeListFragment;
import org.zyb.crimeintent.util.SingleFragmentActivity;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/16
 *     desc   : 托管CrimeListFragment
 *     version: 1.0
 * </pre>
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
