package org.zyb.criminalintent;

import android.support.v4.app.Fragment;

import org.zyb.criminalintent.util.SingleFragmentActivity;

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

    @Override
    protected Fragment createFragment(){
        return new CrimeDetailFragment();
    }
}
