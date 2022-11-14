package com.visualdx.dogbreedapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.visualdx.dogbreedapp.databinding.SearchviewDropdownBinding
import com.visualdx.dogbreedapp.network.model.DogSubBreed

class customAdapter(private var items: List<DogSubBreed>) : BaseAdapter(), Filterable {

    private var listFilter: ListFilter? = null

    override fun getCount(): Int = items.size

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getItem(position: Int) = items.get(position)
    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        lateinit var myHolder: myAutoCompleteHolder

        if (convertView == null) {
            var layoutInflater = LayoutInflater.from(parent.context)
            val binding = SearchviewDropdownBinding.inflate(layoutInflater)
            myHolder = myAutoCompleteHolder(binding)
            myHolder.view.tag = myHolder
            binding.dropData = items[position]
            binding.executePendingBindings()
        } else
            myHolder = convertView.tag as myAutoCompleteHolder

        println("YY")
        return myHolder.view
    }

    inner class myAutoCompleteHolder(binding: SearchviewDropdownBinding) {
        val view = binding.root
    }

    override fun getFilter(): Filter? {
        if (listFilter == null) {
            listFilter = ListFilter()
        }
        return listFilter
    }

    inner class ListFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults? {
            var constraint = ""
            val results = Filter.FilterResults()

            results.count = items!!.size
            results.values = items

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
            if (!items.isNullOrEmpty()) {
                items = results?.values as ArrayList<DogSubBreed>
                notifyDataSetChanged()
            }
        }
    }
}