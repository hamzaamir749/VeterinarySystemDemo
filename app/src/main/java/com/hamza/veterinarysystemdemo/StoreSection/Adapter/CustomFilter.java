package com.hamza.veterinarysystemdemo.StoreSection.Adapter;

import android.widget.Filter;

import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreMyProductsModel;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter {
    StoreMyProductsAdapter storeMyProductsAdapter;
    public List<StoreMyProductsModel>  filterList;

    public CustomFilter(StoreMyProductsAdapter storeMyProductsAdapter, List<StoreMyProductsModel> filterList) {
        this.storeMyProductsAdapter = storeMyProductsAdapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        if (constraint !=null && constraint.length()>0)
        {
            constraint=constraint.toString();
            List<StoreMyProductsModel> filteredList=new ArrayList<>();
            for (StoreMyProductsModel s: filterList)
            {
                if (s.getmName().contains(constraint))
                {
                    filteredList.add(s);
                }
            }
            results.count=filteredList.size();
            results.values=filteredList;
            return results;
        }
        results.count=filterList.size();
        results.values=filterList;
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        storeMyProductsAdapter.myProducts= (List<StoreMyProductsModel>) results.values;
        storeMyProductsAdapter.notifyDataSetChanged();
    }
}
