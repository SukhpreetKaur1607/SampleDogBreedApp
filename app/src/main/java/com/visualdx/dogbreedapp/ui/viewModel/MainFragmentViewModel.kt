package com.visualdx.dogbreedapp.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visualdx.dogbreedapp.convertToSubBreedList
import com.visualdx.dogbreedapp.network.ApiState
import com.visualdx.dogbreedapp.network.model.DogBreed
import com.visualdx.dogbreedapp.network.model.DogSingleImage
import com.visualdx.dogbreedapp.network.model.DogSubBreed
import com.visualdx.dogbreedapp.network.repositories.MainRepository
import com.visualdx.dogbreedapp.ui.DogImagesAdapter
import com.visualdx.dogbreedapp.ui.customAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainFragmentViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val requestState: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val _requestState: StateFlow<ApiState> = requestState

    private val loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val _loading: MutableLiveData<Boolean> = loading

    private val breedName: MutableStateFlow<String?> = MutableStateFlow(null)
    val _breedName: StateFlow<String?> = breedName

    private val subBreedName: MutableStateFlow<String?> = MutableStateFlow(null)
    val _subBreedName: StateFlow<String?> = subBreedName

    private val _breedList = MutableStateFlow<List<DogBreed>?>(listOf<DogBreed>())
    val breedList: StateFlow<List<DogBreed>?> = _breedList

    private val _subBreedList = MutableStateFlow<List<DogSubBreed>?>(listOf<DogSubBreed>())
    val subBreedList: StateFlow<List<DogSubBreed>?> = _subBreedList

    private val _dogImagesList = MutableStateFlow<List<String>?>(listOf<String>())
    val dogImagesList: StateFlow<List<String>?> = _dogImagesList

    var searchText = MutableStateFlow<String>("")

    var _recyclerViewAdapter = DogImagesAdapter(listOf())

    var _dropAdapter = customAdapter(listOf());


    init {
        viewModelScope.launch {
            requestState.value = ApiState.Loading


            mainRepository.getAllBreeds()
                .catch { e ->
                    requestState.value = ApiState.Failure(e)
                    return@catch
                }
                .combine(searchText) { list, filter ->
                    convertToSubBreedList(list).filter { master ->

                        master.name.substringBefore(",")
                            .startsWith(filter, false) || master.name.substringAfter(",")
                            .startsWith(filter, false)
                    }
                }


                .flowOn(Dispatchers.IO)
                .collect {
                    requestState.value = ApiState.Success(it)
                    _subBreedList.value = it
                    println("DogBreedValues" + _breedList.value)
                    setAdapterData(it)
                }
        }
    }

    private fun setAdapterData(it: List<DogSubBreed>) {

        _dropAdapter = customAdapter(it)
        _dropAdapter.notifyDataSetChanged()
    }

    fun setSelection(dogBreeds: DogSubBreed) {
        breedName.value = dogBreeds.name.substringBefore(",")
        subBreedName.value = dogBreeds.name.substringAfter(",")
    }

    fun getRandomImages() {
        println("DynamicBreed" + breedName.value.toString())

        viewModelScope.launch {

            loading.postValue(true)
            mainRepository.getRandomImages(breedName.value.toString())
                .catch { e ->
                    requestState.value = ApiState.Failure(e)
                }.flowOn(Dispatchers.IO)
                .collect() {
                    requestState.value = ApiState.Success(it)
                    _dogImagesList.value = it
                    setRecyclerViewAdapter(it)
                    loading.postValue(false)
                    println("DogImagesList" + it?.size)
                }
        }

    }

    fun getRandomImagesByBreed() {
        println("DynamicBreed" + breedName.value.toString() + " , " + subBreedName.value.toString())
        loading.postValue(true)
        viewModelScope.launch {

            mainRepository.getRandomImages(
                breedName.value.toString(),
                subBreedName.value.toString()
            )
                .catch { e ->
                    requestState.value = ApiState.Failure(e)

                }.flowOn(Dispatchers.IO)
                .collect() {
                    requestState.value = ApiState.Success(it)
                    _dogImagesList.value = it
                    setRecyclerViewAdapter(it)
                    loading.postValue(false)
                    println("DogImagesList" + it.size)
                }
        }

    }

    private fun setRecyclerViewAdapter(dogImages: List<String>?) {
        _recyclerViewAdapter = DogImagesAdapter(dogImages)
        _recyclerViewAdapter.notifyDataSetChanged()
    }
}