package org.zyb.crimeintent.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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

import org.zyb.crimeintent.CrimePagerActivity;
import org.zyb.crimeintent.R;
import org.zyb.crimeintent.model.Crime;
import org.zyb.crimeintent.model.CrimeManager;
import org.zyb.crimeintent.util.Utility;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

    private final int REQUEST_CONTACT = 1;
    private final int REQUEST_DATE = 2;

    private final int PERMISSION_CONTACTS = 1;

    private LinearLayout ll_edit;
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
        crime = crimeManager.getCrimeById(getArguments().getLong("crimeId"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crimedetail,container,false);

        // init widgets
        tv_title = (TextView) v.findViewById(R.id.id_tv_title);
        ll_edit = (LinearLayout) v.findViewById(R.id.id_ll_edit);
        et_title = (EditText) v.findViewById(R.id.id_et_title);
        btn_enter = (Button) v.findViewById(R.id.id_btn_enter);
        btn_crimeDate = (Button) v.findViewById(R.id.id_btn_crimeDate);
        cb_isSolved = (CheckBox) v.findViewById(R.id.id_cb_isSolved);

        // 浏览模式
        tv_title.setText(crime.getTitle());
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏TextView，显示EditText和Button，并弹出软键盘
                tv_title.setVisibility(View.GONE);
                ll_edit.setVisibility(View.VISIBLE);
                et_title.setText(tv_title.getText());
                et_title.requestFocus();
                showSoftKeyBoard(et_title);
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
                hideSoftKeyBoard();
            }
        });
        // 默认将编辑控件隐藏，编辑时才显示
        ll_edit.setVisibility(View.GONE);

        // 但是如果标题为空，则调出编辑模式
        if (crime.getTitle() == null){
            tv_title.performClick();
        }

        // DatePicker
        btn_crimeDate.setText(crime.getDate());
        btn_crimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(Utility.stringToDate(crime.getDate()));
                dialog.setTargetFragment(CrimeDetailFragment.this, REQUEST_DATE);
                dialog.show(manager,"datePickerDialog");
            }
        });

        // checkBox
        cb_isSolved.setChecked(crime.getIsSolved());
        cb_isSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setIsSolved(isChecked);
                //向数据库提交
                crimeManager.updateCrime(crime);
            }
        });
        cb_isSolved.setSaveEnabled(false);//强制不缓存该view的临时数据

        // suspect
        btn_suspect = (Button) v.findViewById(R.id.id_btn_suspect);
        if (crime.getSuspect() != null){
            btn_suspect.setText(crime.getSuspect());
        }
        btn_suspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check permission
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},PERMISSION_CONTACTS);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CONTACT);
                }
            }
        });

        // report
        btn_report = (Button) v.findViewById(R.id.id_btn_report);
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND).setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, generateCrimeReport(crime));
                startActivity(intent);
            }
        });


        Log.d(TAG, crime.getId()+ "  view Created");
        Log.d(TAG, crime.getId()+ "   visibleToUser: "+getUserVisibleHint());
        return v;
    }

    @Override
    public void onResume() {
        Log.d(TAG, crime.getId()+"  onResumed");
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        hideSoftKeyBoard();
        Log.d(TAG, crime.getId()+ "  onPaused");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: ");
//        if (isVisibleToUser){
//            if (crime.getTitle() == null){
//                showSoftKeyBoard(et_title);
//            } else {
//                hideSoftKeyBoard();
//            }
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "detailFragment of "+crime.getId()+ " is destroyed");
    }

    /**
     * 接收来自DatePicker的数据
     * 先验证结果来自哪一方（requestCode），再验证是什么结果（resultCode）
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_DATE:
                if (resultCode == DatePickerFragment.RESULT_OK) {
                    Date date = (Date) intent.getSerializableExtra("crimeDate");

                    crime.setDate(Utility.dateToString(date));
                    crimeManager.updateCrime(crime);
                    btn_crimeDate.setText(crime.getDate());
                }
                break;
            case REQUEST_CONTACT:
                if (intent != null){
                    Uri uri = intent.getData();
                    String[] queryField = {ContactsContract.Contacts.DISPLAY_NAME};
                    Cursor cursor = getActivity().getContentResolver().query(uri,queryField,null,null,null);
                    if (cursor == null){
                        return;
                    }
                    cursor.moveToFirst();
                    String suspectName = cursor.getString(0);

                    crime.setSuspect(suspectName);
                    crimeManager.updateCrime(crime);
                    btn_suspect.setText(suspectName);
                    cursor.close();
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
                crimePagerActivity.onItemDeleted(crime);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 显示软键盘
     */
    private void showSoftKeyBoard(final EditText editText){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 300);
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyBoard(){
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(et_title.getWindowToken(),0);
    }

    /**
     * 根据传入的Crime对象生成一份report
     */
    private String generateCrimeReport(Crime crime){
        return "title:"+ crime.getTitle()+"\n"+"date:"+ crime.getDate()+"\n"+"isSolved:"+crime.getIsSolved();
    }

    /**
     * 运行时权限处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CONTACT);
                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
