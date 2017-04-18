package org.zyb.crimeintent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.zyb.crimeintent.CrimePagerActivity;
import org.zyb.crimeintent.R;
import org.zyb.crimeintent.model.Crime;
import org.zyb.crimeintent.model.CrimeManager;

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

    private CrimeManager crimeManager;

    private List<Crime> crimeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        crimeManager = CrimeManager.getCrimeManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crimelist,container,false);
        rv_crimeList = (RecyclerView) view.findViewById(R.id.id_rv_crimeList);

        // setLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_crimeList.setLayoutManager(layoutManager);

        // setAdapter
        crimeList = crimeManager.getCrimeList();
        CrimeAdapter adapter = new CrimeAdapter(crimeList);
        rv_crimeList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "CrimeListFragment onResumed");
        rv_crimeList.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //注意这个Inflater是menuInflater，不是之前用的Inflater
        inflater.inflate(R.menu.item_list_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_menu_add:
                Crime crime = new Crime();
                Log.d(TAG, "before add into db new crime id is :" + crime.getId());
                crimeManager.addCrime(crime);//greendao会不会自动创建id？
                Log.d(TAG, "after add into db new crime id is :" + crime.getId());
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    // Adapter
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
                    Intent intent = CrimePagerActivity.newIntent(getActivity(),crimeList.get(position).getId());
                    startActivity(intent);
                }
            });


            holder.cb_isSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = holder.getAdapterPosition();
                    Crime crime = crimeList.get(position);
                    crime.setIsSolved(isChecked);
                    //向数据库提交数据
                    crimeManager.updateCrime(crime);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = crimeList.get(position);
            holder.tv_title.setText(crime.getTitle());
            holder.tv_date.setText(crime.getDate());
            holder.cb_isSolved.setChecked(crime.getIsSolved());
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
