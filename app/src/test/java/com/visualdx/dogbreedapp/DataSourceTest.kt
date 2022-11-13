package com.visualdx.dogbreedapp

import com.visualdx.dogbreedapp.network.ApiInterface
import com.visualdx.dogbreedapp.network.model.DogBreed
import com.visualdx.dogbreedapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class DataSourceTest {

    lateinit var service: ApiInterface

    @Before
    internal fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ApiInterface::class.java)
    }

    @Test
    internal fun callServicesWithFlow() {
        runBlocking {
            flow<List<DogBreed>> {
                emit(service.getAllBreeds().body()!!.toDogBreed())
            }.flowOn(Dispatchers.IO)
                .collect {
                    it.forEach(::println)
                }
        }


    }
}