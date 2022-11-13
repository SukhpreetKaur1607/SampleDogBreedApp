package com.visualdx.dogbreedapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualdx.dogbreedapp.databinding.RecyclerviewImagesRowBinding

class DogImagesAdapter(val dogImagesList: List<String>?) :
    RecyclerView.Adapter<DogImagesAdapter.DogImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogImageViewHolder {
        val binding =
            RecyclerviewImagesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogImageViewHolder(binding)
    }


    override fun onBindViewHolder(holder: DogImageViewHolder, position: Int) {
        holder.recyclerviewBinding.imageViewModel = dogImagesList?.get(position) ?: ""
    }

    override fun getItemCount(): Int = dogImagesList?.size ?: 0

    inner class DogImageViewHolder(val recyclerviewBinding: RecyclerviewImagesRowBinding) :
        RecyclerView.ViewHolder(recyclerviewBinding.root) {}

}