package org.zyb.crimeintent.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import org.zyb.crimeintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DatePickerFragment extends DialogFragment {

    public static final int RESULT_OK = 1;

    /**
     * 专供其他类调用的静态实例化方法
     * @param date
     * @return
     */
    public static DatePickerFragment newInstance(Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable("crimeDate",date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_datepicker,null);

        Date crimeDate = (Date) getArguments().get("crimeDate");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(crimeDate);
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.id_dp_datePicker);
        datePicker.init(y,m,d,null);

        builder.setTitle("Date of crime")
                .setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int y = datePicker.getYear();
                        int m = datePicker.getMonth();
                        int d = datePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(y,m,d).getTime();
                        sentResult(DatePickerFragment.RESULT_OK,date);
                    }
                });
        return builder.create();
    }

    /**
     * 在本Fragment结束前回调上一个Fragment的onActivityResult方法将数据回传
     * @param resultCode the code to identify different result
     * @param date data to pass over
     */
    public void sentResult(int resultCode, Date date){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("crimeDate",date);
        getTargetFragment().onActivityResult(1,resultCode,intent);
    }
}
