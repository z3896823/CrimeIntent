package org.zyb.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.zyb.criminalintent.model.Crime;
import org.zyb.criminalintent.model.CrimeLab;

import java.util.List;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CrimeListFragment extends Fragment {

    private static final String TAG = "ybz";

    private RecyclerView rv_crimeList;

    private CrimeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crimelist,container,false);
        rv_crimeList = (RecyclerView) view.findViewById(R.id.id_rv_crimeList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rv_crimeList.setLayoutManager(layoutManager);

        CrimeLab crimeLab = CrimeLab.getCrimeLab(getActivity());
        List<Crime> crimeList = crimeLab.getCrimeList();
        adapter = new CrimeAdapter(crimeList);
        rv_crimeList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.CrimeHolder>{

        private List<Crime> crimeList;

        private CrimeAdapter(List<Crime> crimeList) {
            this.crimeList = crimeList;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_crime_list,parent,false);
            final CrimeHolder holder = new CrimeHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
//                    Intent intent = CrimeDetailActivity.newIntent(getActivity(),crimeList.get(position).getId());
                    Intent intent = CrimePagerActivity.newIntent(getActivity(),crimeList.get(position).getId());
                    startActivity(intent);
                }
            });

            holder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Toast.makeText(getActivity(), crimeList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                }
            });


            return holder;
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = crimeList.get(position);
            holder.tv_title.setText(crime.getTitle());
            holder.tv_date.setText(crime.getDate().toString());
            holder.cb_isSolved.setChecked(crime.getSolved());
        }

        @Override
        public int getItemCount(){
            return crimeList.size();
        }

        class CrimeHolder extends RecyclerView.ViewHolder{

            TextView tv_title;
            TextView tv_date;
            CheckBox cb_isSolved;

            private CrimeHolder(View itemView) {
                super(itemView);
                tv_title = (TextView) itemView.findViewById(R.id.id_tv_crimeTitle);
                tv_date = (TextView) itemView.findViewById(R.id.id_tv_crimeDate);
                cb_isSolved = (CheckBox) itemView.findViewById(R.id.id_cb_isSolved);
            }
        }

    }

}
