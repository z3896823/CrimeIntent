package org.zyb.criminalintent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.zyb.criminalintent.CrimePagerActivity;
import org.zyb.criminalintent.R;
import org.zyb.criminalintent.model.Crime;
import org.zyb.criminalintent.model.CrimeManager;
import org.zyb.criminalintent.util.Utility;

import java.security.cert.CertSelector;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/16
 *     desc   : 显示item的detail
 *     version: 1.0
 * </pre>
 */

public class CrimeDetailFragment extends Fragment {

    private static final String TAG = "ybz";
    private Crime crime;

    private EditText et_title;
    private Button btn_crimeDate;
    private CheckBox cb_isSolved;

    public CrimeManager crimeManager = CrimeManager.getCrimeManager(getActivity());

    /**
     * 该静态方法供Activity在创建本Fragment的时候调用，使得本Fragment在创建之初，
     * 且在attach给Activity之前就获得需要的数据（使用setArguments()方法）
     * @param uuid the data it needs
     * @return a fragment with data
     */
    public static CrimeDetailFragment newInstance(UUID uuid){
        Bundle bundle = new Bundle();
        bundle.putSerializable("crimeId",uuid);

        CrimeDetailFragment crimeDetailFragment = new CrimeDetailFragment();
        crimeDetailFragment.setArguments(bundle);
        return crimeDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID crimeId = (UUID)getArguments().getSerializable("crimeId");
        crime = CrimeManager.getCrimeManager(getActivity()).getCrime(crimeId);//成功获取到Crime对象
        Log.d(TAG, "detailFragment of "+ crime.getTitle()+" is created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        et_title = (EditText) v.findViewById(R.id.id_et_title);
        et_title.setText(crime.getTitle());
        et_title.setSelection(et_title.getText().length());
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                //向数据库提交
                crimeManager.changeCrimeTitle(crime.getUuid(),s.toString());
            }
        });

        btn_crimeDate = (Button) v.findViewById(R.id.id_btn_crimeDate);
        btn_crimeDate.setText(crime.getDate());
        btn_crimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(Utility.stringToDate(crime.getDate()));
                dialog.setTargetFragment(CrimeDetailFragment.this, 1);
                dialog.show(manager,"datePickerDialog");
            }
        });

        cb_isSolved = (CheckBox) v.findViewById(R.id.id_cb_isSolved);
        cb_isSolved.setChecked(crime.getSolved());
        cb_isSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                //向数据库提交
                crimeManager.changeCrimeSolved(crime.getUuid(),crime.getSolved());
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "detailFragment of "+crime.getTitle()+ " is destroyed");
    }

    /**
     * 接收来自DatePickerFragment的数据
     * 先验证结果来自哪一方（requestCode），再验证是什么结果（resultCode）
     * @param requestCode identity who send this result
     * @param resultCode identify the specific result
     * @param data where the data is stored
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            switch (resultCode){
                case DatePickerFragment.RESULT_OK:
                    Date date = (Date) data.getSerializableExtra("crimeDate");
                    crimeManager.changeCrimeDate(crime.getUuid(),Utility.dateToString(date));
                    //由于btn不属于编辑型控件，需要手动更新其内容
                    btn_crimeDate.setText(crime.getDate());
                    break;
                default:
                    break;
            }
        }
    }

    // create menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.item_detail_fragment,menu);
    }

    // menu item clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_menu_delete:
                CrimePagerActivity crimePagerActivity = ((CrimePagerActivity)getActivity());
                crimePagerActivity.onItemDeleted(crime.getUuid());
                break;
            default:
                break;
        }
        return true;
    }
}
