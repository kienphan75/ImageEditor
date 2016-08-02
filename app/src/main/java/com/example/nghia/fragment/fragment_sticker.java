package com.example.nghia.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.nghia.adapter.RecyclerViewAdapter;
import com.example.nghia.imageediter.R;
import com.example.nghia.utttilities.RecyclerItemClickListener;



public class fragment_sticker extends Fragment {

    public interface Send{
        public void guidata(int x);
    }

    Send send;
    RecyclerView recList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView= inflater.inflate(R.layout.imf_effect_factory,container,false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recList = (RecyclerView) fragmentView.findViewById(R.id.rc_filter);
        recList.setHasFixedSize(true);
        recList.setLayoutManager(layoutManager);

        RecyclerViewAdapter filterAdapter = new RecyclerViewAdapter(getActivity());
        recList.setAdapter(filterAdapter);

        recList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("MY",String.valueOf(position));
                        send.guidata(position);

                    }
                }));

        return fragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        send= (Send) activity;
    }
}
