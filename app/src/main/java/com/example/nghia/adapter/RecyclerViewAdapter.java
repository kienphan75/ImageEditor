package com.example.nghia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.nghia.imageediter.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 18/07/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FilterHolder>{
    int itemData[] = {R.drawable.st1,
                    R.drawable.st2,
                    R.drawable.st3,
                    R.drawable.st4,
                    R.drawable.st5,
                    R.drawable.st6,
                    R.drawable.st7,
                    R.drawable.st8,
                    R.drawable.st9,
                    R.drawable.st10,
                    R.drawable.st11,
                    R.drawable.st12,
                    R.drawable.st13,
                    R.drawable.st14,};


    private Context mContext;
    public RecyclerViewAdapter (Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_edit, parent, false);
        FilterHolder viewHolder = new FilterHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, int position) {

        holder.imFilter.setImageResource(itemData[position]);

    }

    @Override
    public int getItemCount() {
        return itemData.length;
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        public ImageView imFilter;

        public FilterHolder(View itemView) {
            super(itemView);
            imFilter = (ImageView) itemView.findViewById(R.id.stickerviewimage_item);
        }
    }

}
