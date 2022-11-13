package com.visualdx.dogbreedapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.visualdx.dogbreedapp.ui.DogImagesAdapter
import com.visualdx.dogbreedapp.ui.customAdapter
import com.visualdx.dogbreedapp.network.model.DogBreed
import com.visualdx.dogbreedapp.network.model.DogSubBreed

@BindingAdapter("urlImage")
fun bindImgeURL(view: ImageView, imageUrl: String?) {
    val requestOptions: RequestOptions = RequestOptions()
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()
    Glide.with(view.context)
        .load(imageUrl)
        .apply(requestOptions)
        .into(view)
}


@BindingAdapter("entries", requireAll = false)
fun MaterialAutoCompleteTextView.bindAdapter(
    entries: List<DogBreed>
) {
    setAdapter(customAdapter(entries))
}

@BindingAdapter("textView")
fun TextView.bindData(data: List<DogSubBreed>) {
    println("entryDogBreed" + data.size)
    this.text = data[0].name
}

@BindingAdapter("itemViewModels")
fun bindItemViewModels(recyclerView: RecyclerView, itemViewModels: List<String>) {
    val adapter = DogImagesAdapter(itemViewModels)
    recyclerView.adapter = adapter
}



