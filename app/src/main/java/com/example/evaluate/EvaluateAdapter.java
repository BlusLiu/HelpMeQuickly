package com.example.evaluate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.helpmequickly_my.R;

import java.util.List;

public class EvaluateAdapter extends RecyclerView.Adapter<EvaluateAdapter.ViewHolder> {

    private List<Evaluate> mEvaluates;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView user_name;
        TextView evaluate_theme;
        TextView year_month_day;
        TextView evaluate_state;
        RatingBar evaluate_rb;
        TextView evaluate_content;

        public ViewHolder(View view) {
            super(view);
            user_name = view.findViewById(R.id.user_name);
            evaluate_theme = view.findViewById(R.id.evaluate_theme);
            year_month_day = view.findViewById(R.id.year_month_day);
            evaluate_state = view.findViewById(R.id.evaluate_state);
            evaluate_rb = view.findViewById(R.id.evaluate_rb);
            evaluate_content = view.findViewById(R.id.evaluate_content);
        }
    }

    public EvaluateAdapter(List<Evaluate> mEvaluates) {
        this.mEvaluates = mEvaluates;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_evaluate_little,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Evaluate evaluate = mEvaluates.get(position);
        holder.user_name.setText(evaluate.getName());
        holder.evaluate_theme.setText(evaluate.getTheme());
        holder.year_month_day.setText(evaluate.getTime());
        holder.evaluate_state.setText(evaluate.getState());
        holder.evaluate_rb.setRating(evaluate.getStar());
        holder.evaluate_content.setText(evaluate.getContent());
    }

    @Override
    public int getItemCount(){
        return mEvaluates.size();
    }
}
