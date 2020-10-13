package com.hamza.veterinarysystemdemo.UserSection.UserProblemsActivity;

import android.widget.Filter;

import com.hamza.veterinarysystemdemo.models.userProblemsModel;

import java.util.ArrayList;
import java.util.List;

public class UserProblemsFilterUrdu extends Filter {
    public List<userProblemsModel> filterlist;
    UserProblemsAdapterUrdu adapter;

    public UserProblemsFilterUrdu(List<userProblemsModel> filterlist, UserProblemsAdapterUrdu adapter) {
        this.filterlist = filterlist;
        this.adapter = adapter;
    }
    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results = new Filter.FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString();
            List<userProblemsModel> filteredList = new ArrayList<>();
            for (userProblemsModel u : filterlist) {
                if (u.getDescription().contains(constraint)) {
                    filteredList.add(u);
                }
            }
            results.count = filteredList.size();
            results.values = filteredList;
            return results;

        }
        results.count = filterlist.size();
        results.values = filterlist;
        return results;

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.userproblemsList = (List<userProblemsModel>) results.values;
        adapter.notifyDataSetChanged();
    }
}
