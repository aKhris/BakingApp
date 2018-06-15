package com.example.anatoly.bakingapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class SelectableRecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>  {

    private static final String BUNDLE_SELECTED_POS = "selected_pos";

    private int selectedPos = RecyclerView.NO_POSITION;

    public abstract boolean isSelectable();

    public abstract void itemClicked(int position);

    public void selectItem(int position){
        if(position==RecyclerView.NO_POSITION){return;}
        int prevSelected = selectedPos;
        selectedPos = position;
        notifyItemChanged(selectedPos);
        notifyItemChanged(prevSelected);
    }


    @NonNull
    public abstract T onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        if(holder instanceof SelectableRecyclerViewAdapter.SelectableViewHolder && isSelectable()){
            int selectedColor = ContextCompat.getColor(
                    holder.itemView.getContext(),
                    R.color.colorAccent
            );
            holder.itemView.setBackgroundColor(
                    position==selectedPos?
                            selectedColor : Color.TRANSPARENT
            );
        }
    }

    public void onSaveInstanceState(Bundle bundle){
        bundle.putInt(BUNDLE_SELECTED_POS, selectedPos);
    }

    public void onRetainInstanceState(Bundle bundle){
        if(bundle!=null && bundle.containsKey(BUNDLE_SELECTED_POS)){
            this.selectedPos = bundle.getInt(BUNDLE_SELECTED_POS);
            notifyItemChanged(this.selectedPos);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SelectableViewHolder extends RecyclerView.ViewHolder{

        public SelectableViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked(getAdapterPosition());
                    selectItem(getAdapterPosition());
                }
            });

        }
    }

}
