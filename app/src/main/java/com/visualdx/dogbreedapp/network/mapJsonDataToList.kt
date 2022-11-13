package com.visualdx.dogbreedapp

import com.visualdx.dogbreedapp.network.model.*

fun AllBreedsResponse.mapDogBreeds(): List<DogBreed> {
    return this.message.entries.map {
        val subBreed = it.value.map { sub ->
            DogSubBreed(sub)
        }
        DogBreed(it.key, subBreed)
    }

}