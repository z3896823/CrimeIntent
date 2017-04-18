package org.zyb.crimeintent.fragment;

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

import org.zyb.crimeintent.CrimePagerActivity;
import org.zyb.crimeintent.R;
import org.zyb.crimeintent.dao.Crime;
import org.zyb.crimeintent.dao.CrimeManager;
import org.zyb.crimeintent.util.Utility;

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

    private TextView tv_title;

    private Button btn_enter;


    public CrimeManager crimeManager;

    /**
     * 该静态方法供Activity在创建本Fragment的时候调用，使得本Fragment在创建之初，
     * 且在attach给Activity之前就获得需要的数据（使用setArguments()方法）
     * @param id the data it needs
     * @return a fragment with data
     */
    public static CrimeDetailFragment newInstance(Long id){
        Bundle bundle = new Bundle();
        bundle.putLong("crimeId",id);
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
        crimeManager = CrimeManager.getCrimeManager();
        Long id = getArguments().getLong("crimeId");
        crime = crimeManager.getCrimeById(id);//成功获取到Crime对象
        Log.d(TAG, "detailFragment of "+crime.getId()+ " is created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crimedetail,container,false);
        v.setSaveEnabled(false);

        // init widget
        tv_title = (TextView) v.findViewById(R.id.id_tv_title);
        final LinearLayout ll_edit = (LinearLayout) v.findViewById(R.id.id_ll_edit);
        et_title = (EditText) v.findViewById(R.id.id_et_title);
        btn_enter = (Button) v.findViewById(R.id.id_btn_enter);

        //浏览模式
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

        // 编辑模式
        et_title.setText(crime.getTitle());
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = et_title.getText().toString();
                crime.setTitle(newTitle);
                crimeManager.updateCrime(crime);
                ll_edit.setVisibility(View.GONE);
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(et_title.getText());
            }
        });
        // 默认将编辑控件隐藏，编辑时才显示
        ll_edit.setVisibility(View.GONE);


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
        cb_isSolved.setChecked(crime.getIsSolved());
        cb_isSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setIsSolved(isChecked);
                //向数据库提交
                crimeManager.updateCrime(crime);
            }
        });

        return v;
    }

    @Override
    public void onPause(){
        //如果编辑框可见，则此时在添加数据，那么获取编辑框内容对数据进行更新
        if (et_title.getVisibility()!=View.GONE) {
            String newTitle = et_title.getText().toString();
            crime.setTitle(newTitle);
            crimeManager.updateCrime(crime);
        }
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(null);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "detailFragment of "+crime.getId()+ " is destroyed");
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
                    crime.setDate(Utility.dateToString(date));
                    crimeManager.updateCrime(crime);
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
                crimePagerActivity.onItemDeleted(crime);
                break;
            default:
                break;
        }
        return true;
    }
}
