package com.ramz.infinitescroll.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramz.infinitescroll.R;

import java.util.List;

/**
 * Recycler view adapter class
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private List<String> listOfNames;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name_tv;

        ViewHolder(View v) {
            super(v);
            name_tv = v.findViewById(R.id.fbfriends_text);
        }
    }

    public FriendsListAdapter(List<String> listOfNames) {
        this.listOfNames = listOfNames;
    }

    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fbfriends_list_content_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = listOfNames.get(position);
        holder.name_tv.setText(name);
    }

    @Override
    public int getItemCount() {
        return listOfNames.size();
    }
}