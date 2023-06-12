package com.example.revengeforyou;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RevengeAdapter extends RecyclerView.Adapter<RevengeAdapter.RevengeViewHolder> {


    private ArrayList<Revenge> revenges;

    private OnRevengeClickListener listener;
    public interface OnRevengeClickListener{
        void onRevengeClick(int position);
    }

    public void setOnRevengeClickListener(OnRevengeClickListener clickListener){
        listener = clickListener;
    }


    public RevengeAdapter(ArrayList<Revenge> revenges) {
        this.revenges = revenges;
    }

    @Override
    public RevengeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View revengeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_revenge, parent, false);
        return new RevengeViewHolder(revengeView, listener);
    }

    @Override
    public void onBindViewHolder(RevengeViewHolder holder, int position) {
        Revenge currentRevenge = revenges.get(position);
        holder.TVNameOfRevenge.setText(currentRevenge.getEtNameOfRevenge());
        holder.TVWhoWillTakeRevenge.setText(currentRevenge.getEtWhoWillTakeRevenge());
        holder.TVWhatTheRevenge.setText(currentRevenge.getEtWhatTheRevenge());
        holder.TVReasonForRevenge.setText(currentRevenge.getEtReasonForRevenge());
//         holder.checkBox1.setChecked(currentRevenge.RevengeIsDone());
    }

    @Override
    public int getItemCount() {
        return revenges.size();
    }

    public static class RevengeViewHolder extends RecyclerView.ViewHolder {

        public TextView TVNameOfRevenge;
        public TextView TVWhoWillTakeRevenge;
        public TextView TVWhatTheRevenge;
        public TextView TVReasonForRevenge;
        private ImageView ImageDeleteRevenge;
        //public CheckBox checkBox1;


        public RevengeViewHolder(View itemView, OnRevengeClickListener listener) {
            super(itemView);

            TVNameOfRevenge = itemView.findViewById(R.id.TVNameOfRevenge);
            TVWhoWillTakeRevenge = itemView.findViewById(R.id.TVWhoWillTakeRevenge);
            TVWhatTheRevenge = itemView.findViewById(R.id.TVWhatTheRevenge);
            TVReasonForRevenge = itemView.findViewById(R.id.TVReasonForRevenge);
            ImageDeleteRevenge = itemView.findViewById(R.id.ImageDeleteRevenge);
           // checkBox1 = itemView.findViewById(R.id.checkBox1);

            ImageDeleteRevenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRevengeClick(getAdapterPosition());
                }
            });


        }
    }

}
