package com.visualdx.dogbreedapp.network

import com.visualdx.dogbreedapp.network.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("breeds/list/all/")
    suspend fun getAllBreeds(): Response<AllBreedsResponse>

    @GET("breed/{breed_name}/images/random/10")
    suspend fun getRandomImages(@Path("breed_name") breedName: String): Response<RandomDogImages>

    @GET("breed/{breed_name}/{sub_breed}/images/random/10")
    suspend fun getRandomImagesBySubBreed(
        @Path("breed_name") breedName: String,
        @Path("sub_breed") subBreedName: String
    ): Response<RandomDogImages>
}