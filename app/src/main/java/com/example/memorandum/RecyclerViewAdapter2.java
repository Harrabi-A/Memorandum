package com.example.memorandum;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorandum.fragments.MemorandumFragment;
import com.example.memorandum.model.Memorandum;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.MyViewHolder> {
    Context mContext;
    List<Memorandum> mlist;

   private OnItemClickListener mListener;

   public interface OnItemClickListener{
       void onItemDeleteClick(int position);
   }

   public void setOnItemClickListener(OnItemClickListener listener){mListener=listener;}

    public RecyclerViewAdapter2(Context mContext, List<Memorandum> list) {
        this.mContext = mContext;
        this.mlist = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.list_items,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v,mListener);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Memorandum memorandum = mlist.get(position);
        holder.memorandum = memorandum;
        holder.titolo.setText(mlist.get(position).getTitolo());
        holder.stato.setText(mlist.get(position).getStato());
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
        private TextView stato;
        private ImageButton btnDelete;
        private Memorandum memorandum;

        public MyViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);
            titolo = (TextView) itemView.findViewById(R.id.txtMemTitle2);
            stato = (TextView) itemView.findViewById(R.id.txtMemStatus2);
            descrizione = (TextView) itemView.findViewById(R.id.txtDesc2);
            luogo = (TextView) itemView.findViewById(R.id.txtLocation2);
            btnDelete = (ImageButton) itemView.findViewById(R.id.imgbtnDelete2);

            MyDatabaseHelper myDB = new MyDatabaseHelper(titolo.getContext());

            itemView.findViewById(R.id.imgbtnDelete2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Delete Memorandum  ","Clicked delete "+memorandum.getId());
                    myDB.deleteOneRow(memorandum);
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                             listener.onItemDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

}