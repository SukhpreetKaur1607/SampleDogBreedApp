package com.visualdx.dogbreedapp.network.repositories

import com.visualdx.dogbreedapp.mapDogBreeds
import com.visualdx.dogbreedapp.network.ApiInterface
import com.visualdx.dogbreedapp.network.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiInterface: ApiInterface) {

    fun getAllBreeds(): Flow<List<DogBreed>?> = flow {
        val res = apiInterface.getAllBreeds()
        emit(
            res.body()?.mapDogBreeds()
        )
    }.catch { e ->
        println(e)
    }.flowOn(Dispatchers.IO)


    fun getRandomImages(breedName: String): Flow<List<String>?> = flow {
        val res = apiInterface.getRandomImages(breedName)
        emit(
            res.body()?.message
        )
    }.flowOn(Dispatchers.IO)

    fun getRandomImages(breedName: String, subBreed: String): Flow<List<String>> = flow {
        val res = apiInterface.getRandomImagesBySubBreed(breedName, subBreed)
        emit(
            res.body()!!.message
        )
    }.flowOn(Dispatchers.IO)
}