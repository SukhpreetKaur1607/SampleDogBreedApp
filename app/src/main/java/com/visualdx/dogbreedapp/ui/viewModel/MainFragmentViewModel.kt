package com.visualdx.dogbreedapp.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visualdx.dogbreedapp.ui.DogImagesAdapter
import com.visualdx.dogbreedapp.ui.customAdapter
import com.visualdx.dogbreedapp.network.repositories.MainRepository
import com.visualdx.dogbreedapp.network.ApiState
import com.visualdx.dogbreedapp.network.model.DogBreed
import com.visualdx.dogbreedapp.network.model.DogSingleImage
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

    private val _dogImagesList = MutableStateFlow<List<String>?>(listOf<String>())
    val dogImagesList: StateFlow<List<String>?> = _dogImagesList

    private val imageString: MutableStateFlow<String?> = MutableStateFlow(null)
    val _imageString: StateFlow<String?> = imageString

    var searchText = MutableStateFlow<String>("")

    var _recyclerViewAdapter = DogImagesAdapter(listOf())

    var _dropAdapter = customAdapter(listOf());


    init {
        viewModelScope.launch {
            requestState.value = ApiState.Loading


            mainRepository.getAllBreeds()
                .catch { e ->
                    requestState.value = ApiState.Failure(e)
                    // showError.value = true
                    return@catch
                }
                /*to search  complete subBreedList*/
                /*.combine(searchText) { list, filter ->
                    list?.filter { master ->
                        master.name.startsWith(filter, false) || master.subBreed.filter { child ->
                            child.name.startsWith(filter, false)
                        }.size > 0
                    }
                }*/

                /*Only considering first element of subArray*/
                .combine(searchText) { list, filter ->
                    list?.filter { master ->
                        master.name.startsWith(
                            filter,
                            false
                        ) || (if (master.subBreed.size > 0) (master.subBreed.get(0).name.startsWith(
                            filter,
                            false
                        )) else false)
                    }
                }

                .flowOn(Dispatchers.IO)
                .collect {
                    requestState.value = ApiState.Success(it)
                    _breedList.value = it
                    println("Responsee" + it)
                    println("DogReedValues" + _breedList.value)
                    //  println(_breedName.value)
                    if (it != null)
                        setAdapterData(it)
                }
        }
    }

    private fun setAdapterData(it: List<DogBreed>) {
        _dropAdapter = customAdapter(it)
        _dropAdapter.notifyDataSetChanged()
    }

    fun setSelection(dogBreed: DogBreed) {
        breedName.value = dogBreed.name
        if (dogBreed.subBreed.size > 0) subBreedName.value = dogBreed.subBreed[0].name else ""
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

    fun setImage(it: DogSingleImage?) {
        imageString.value = it?.message
    }

    private fun setRecyclerViewAdapter(dogImages: List<String>?) {
        _recyclerViewAdapter = DogImagesAdapter(dogImages)
        _recyclerViewAdapter.notifyDataSetChanged()
    }
}