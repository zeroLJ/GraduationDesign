package com.zero.voicenote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by ljl on 2018/2/12.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.Holder> {
    private List<Map<String, Object>> data;
    private Context context;
    public DataAdapter(Context context, List<Map<String, Object>> data){
        this.context = context;
        this.data = data;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_data, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick((int)v.getTag());
            }
        });
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.num.setText(String.valueOf(position+1));
        holder.view.setTag(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView num;
        View view;
        public Holder(View itemView) {
            super(itemView);
            view = itemView;
            num = itemView.findViewById(R.id.title);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onClick(int position) {

        }
    };

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
