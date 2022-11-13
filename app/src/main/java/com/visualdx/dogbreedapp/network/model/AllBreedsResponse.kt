package com.visualdx.dogbreedapp.network.model

data class AllBreedsResponse(
    val status: String,
    val message: Map<String, List<String>>
) {
}

data class DogBreed(
    val name: String,
    val subBreed: List<DogSubBreed>
)

data class DogSubBreed(
    val name: String
)

data class RandomDogImages(val status: String, val message: List<String>)

data class DogImages(val imageName: String)

data class DogSingleImage(val status: String, val message: String)



