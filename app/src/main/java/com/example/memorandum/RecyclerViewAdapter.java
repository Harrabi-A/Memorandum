package com.example.memorandum;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorandum.model.Memorandum;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context mContext;
    List<Memorandum> mlist;

    public RecyclerViewAdapter(Context mContext, List<Memorandum> list) {
        this.mContext = mContext;
        this.mlist = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.active_mem_recycler,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Memorandum memorandum = mlist.get(position);
        holder.memorandum = memorandum;
        holder.titolo.setText(mlist.get(position).getTitolo());
        holder.descrizione.setText(mlist.get(position).getDescrizione());
        holder.luogo.setText(mlist.get(position).getLuogo().getName());
    }

    @Override
    public int getItemCount() {
        return mlist.size() ;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titolo;
        private TextView descrizione;
        private TextView luogo;
        private Memorandum memorandum;

        public MyViewHolder(View itemView){
            super(itemView);
            titolo = (TextView) itemView.findViewById(R.id.txtMemTitle);
            descrizione = (TextView) itemView.findViewById(R.id.txtMemDesc);
            luogo = (TextView) itemView.findViewById(R.id.txtLocation);

            MyDatabaseHelper myDB = new MyDatabaseHelper(titolo.getContext());

            itemView.findViewById(R.id.imgbtnDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("demp","Clicked delete "+memorandum.getId());
                    myDB.deleteOneRow(memorandum);
                }
            });

            itemView.findViewById(R.id.imgbtnDone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("demp","Clicked done "+memorandum.getId());
                    memorandum.setStato("Completed");
                    myDB.updateData(memorandum);

                }
            });

        }
    }
}
