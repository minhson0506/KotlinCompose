package com.example.w2_d2_retrofit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val repository: DemoApi.WebServiceRepository = DemoApi.WebServiceRepository()

    val numbersOfSearch = MutableLiveData<Int>()

    fun getNumberOfSearch(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val presidentResult = repository.search(name)
            numbersOfSearch.postValue(presidentResult.query.searchinfo.totalhits)
        }
    }
}