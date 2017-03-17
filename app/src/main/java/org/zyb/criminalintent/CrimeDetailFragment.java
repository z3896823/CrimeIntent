package org.zyb.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.zyb.criminalintent.model.Crime;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CrimeDetailFragment extends Fragment {

    private Crime crime;

    private EditText et_input;
    private Button btn_crimeDate;
    private CheckBox cb_isSolved;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        et_input = (EditText) v.findViewById(R.id.id_et_);
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_crimeDate = (Button) v.findViewById(R.id.id_btn_crimeDate);
        btn_crimeDate.setText(crime.getDate().toString());
        btn_crimeDate.setEnabled(false);

        cb_isSolved = (CheckBox) v.findViewById(R.id.id_cb_isSolved);
        cb_isSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
            }
        });

        return v;
    }
}
