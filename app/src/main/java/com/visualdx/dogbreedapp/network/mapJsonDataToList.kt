package com.visualdx.dogbreedapp

import com.visualdx.dogbreedapp.network.model.*

fun AllBreedsResponse.mapDogBreeds(): List<DogBreed> {
    return this.message.entries.map {
        val breed = it.key
        val subBreed = it.value.map { sub ->
            DogSubBreed(/*breed+","+*/sub)
        }
      DogBreed(breed,subBreed)
    }

}
fun convertToSubBreedList(objects: List<DogBreed>?): List<DogSubBreed> {
    val out: MutableList<DogSubBreed> = ArrayList()
    for (o in objects!!) {
        for (j in o.subBreed) {
            val name = o.name + "," + j.name
            out.add(DogSubBreed(name))
        }
    }
    return out
}




