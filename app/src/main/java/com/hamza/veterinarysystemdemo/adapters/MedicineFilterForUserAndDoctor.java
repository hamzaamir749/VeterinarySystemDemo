package com.hamza.veterinarysystemdemo.adapters;

import com.hamza.veterinarysystemdemo.models.userMedicineListModel;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;

public class MedicineFilterForUserAndDoctor extends Filter {
    userMedicineListAdapter adapter;
    public List<userMedicineListModel> filterlist;

    public MedicineFilterForUserAndDoctor(userMedicineListAdapter adapter, List<userMedicineListModel> filterlist) {
        this.adapter = adapter;
        this.filterlist = filterlist;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString();
            List<userMedicineListModel> filteredList = new ArrayList<>();
            for (userMedicineListModel u : filterlist) {
                if (u.getmName().contains(constraint)) {
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
        adapter.medicineList = (List<userMedicineListModel>) results.values;
        adapter.notifyDataSetChanged();
    }
}
