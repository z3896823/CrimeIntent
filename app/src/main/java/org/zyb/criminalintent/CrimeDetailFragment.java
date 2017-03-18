package org.zyb.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.zyb.criminalintent.model.Crime;
import org.zyb.criminalintent.model.CrimeLab;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable("crimeId");
        crime = CrimeLab.getCrimeLab(getActivity()).getCrime(crimeId);//成功获取到Crime对象
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        et_title = (EditText) v.findViewById(R.id.id_et_title);
        et_title.setText(crime.getTitle());
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_crimeDate = (Button) v.findViewById(R.id.id_btn_crimeDate);
        btn_crimeDate.setText(crime.getDate().toString());
        btn_crimeDate.setEnabled(false);

        cb_isSolved = (CheckBox) v.findViewById(R.id.id_cb_isSolved);
        cb_isSolved.setChecked(crime.getSolved());
        cb_isSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 该静态方法供Activity在创建本Fragment的时候调用，使得本Fragment在创建之初，
     * 且在attach给Activity之前就获得需要的数据（使用setArguments()方法）
     *
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
}
