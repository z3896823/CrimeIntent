package org.zyb.criminalintent.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.zyb.criminalintent.CrimePagerActivity;
import org.zyb.criminalintent.R;
import org.zyb.criminalintent.model.Crime;
import org.zyb.criminalintent.model.CrimeManager;
import org.zyb.criminalintent.util.Utility;

import java.util.Date;
import java.util.UUID;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/16
 *     desc   : 该Fragment显示item的detail，并配合CrimePagerActivity，使用ViewPager
 *     version: 1.0
 * </pre>
 */

public class CrimeDetailFragment extends Fragment {

    private static final String TAG = "ybz";
    private Crime crime;

    private EditText et_title;
    private Button btn_crimeDate;
    private CheckBox cb_isSolved;
    private Button btn_suspect;
    private Button btn_report;
    private Button btn_enter;
    private TextView tv_title;

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

    /**
     * 根据argument得到UUID，然后从crimeList中索引到具体的Crime对象
     * @param savedInstanceState null
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(null);
        setHasOptionsMenu(true);
        UUID crimeId = (UUID)getArguments().getSerializable("crimeId");
        crime = CrimeManager.getCrimeManager(getActivity()).getCrime(crimeId);//成功获取到Crime对象
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crimedetail,container,false);

        // 编辑模式
        final LinearLayout ll_edit = (LinearLayout) v.findViewById(R.id.id_ll_edit);
        et_title = (EditText) v.findViewById(R.id.id_et_title);
        et_title.setText(crime.getTitle());
        btn_enter = (Button) v.findViewById(R.id.id_btn_enter);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = et_title.getText().toString();
                crimeManager.changeCrimeTitle(crime.getUuid(), newTitle);
                ll_edit.setVisibility(View.GONE);
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(et_title.getText());
            }
        });
        // 默认将编辑控件隐藏，编辑时才显示
        ll_edit.setVisibility(View.GONE);

        //浏览模式
        tv_title = (TextView) v.findViewById(R.id.id_tv_title);
        tv_title.setText(crime.getTitle());
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏TextView，显示EditText和Button，并弹出软键盘
                tv_title.setVisibility(View.GONE);
                ll_edit.setVisibility(View.VISIBLE);
                et_title.setText(tv_title.getText());
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_title, 0);
            }
        });

        //选择日期的btn，将会弹出DatePicker
        // DatePicker button
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

        // checkBox
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

        btn_report = (Button) v.findViewById(R.id.id_btn_report);
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);

            }
        });

        btn_suspect = (Button) v.findViewById(R.id.id_btn_suspect);
        btn_suspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(null);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "detailFragment of "+crime.getUuid()+ " is destroyed");
    }

    /**
     * 接收来自DatePicker的数据
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
                if (tv_title.getVisibility() == View.GONE){
                    Toast.makeText(getActivity(), "请先完成编辑", Toast.LENGTH_SHORT).show();
                    break;
                }
                CrimePagerActivity crimePagerActivity = ((CrimePagerActivity)getActivity());
                crimePagerActivity.onItemDeleted(crime.getUuid());
                break;
            default:
                break;
        }
        return true;
    }
}
