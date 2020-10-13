package com.hamza.veterinarysystemdemo.UserSection.UserProblemsActivity;

import android.widget.Filter;

import com.hamza.veterinarysystemdemo.adapters.userMedicineListAdapter;
import com.hamza.veterinarysystemdemo.models.userMedicineListModel;
import com.hamza.veterinarysystemdemo.models.userProblemsModel;

import java.util.ArrayList;
import java.util.List;

public class UserProblemsFilter extends Filter {
    userProblemsAdapterEnglish adapter;
    public List<userProblemsModel> filterlist;

    public UserProblemsFilter(userProblemsAdapterEnglish adapter, List<userProblemsModel> filterlist) {
        this.adapter = adapter;
        this.filterlist = filterlist;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
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
