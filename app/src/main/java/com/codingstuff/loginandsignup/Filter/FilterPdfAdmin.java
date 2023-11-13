package com.codingstuff.loginandsignup.Filter;

import android.widget.Filter;

import com.codingstuff.loginandsignup.Domain.ModelPDF;
import com.codingstuff.loginandsignup.recyclerview.AdapterPdfAdmin;

import java.util.ArrayList;

public class FilterPdfAdmin extends Filter {
    ArrayList<ModelPDF> filterList;
    AdapterPdfAdmin adapterPdfAdmin;

    public FilterPdfAdmin(ArrayList<ModelPDF> filterList, AdapterPdfAdmin adapterPdfAdmin) {
        this.filterList = filterList;
        this.adapterPdfAdmin = adapterPdfAdmin;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //  value should not be null/empty
        if (constraint != null && constraint.length() > 0) {

            //
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelPDF> filteredModels = new ArrayList<>();

            for (int i=0; i<filterList.size(); i++){
                //  validate
                if (filterList.get(i).getTitle().toUpperCase().contains(constraint)){
                    //
                    filteredModels.add(filterList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterPdfAdmin.pdfArrayList = (ArrayList<ModelPDF>)results.values;

        adapterPdfAdmin.notifyDataSetChanged();
    }
}
