package com.example.favdish.viewmodel

import androidx.lifecycle.*
import com.example.favdish.model.database.FavDishRepository
import com.example.favdish.model.entities.FavDish
import kotlinx.coroutines.launch

class FavDishViewModel(private val repository: FavDishRepository) : ViewModel() {

    fun insert(dish : FavDish) = viewModelScope.launch {
        repository.insertFavDishData(dish)
    }

    fun delete(dish: FavDish) = viewModelScope.launch {
        repository.deleteTheDish(dish)
    }

    fun update(dish: FavDish) = viewModelScope.launch {
        repository.updateTheDish(dish)
    }

    val allDishesList: LiveData<List<FavDish>> = repository.allDishesList

    val favDishesList: LiveData<List<FavDish>> = repository.favDishesList


}

class FavDishViewModelFactory(private val repository: FavDishRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)){
            return FavDishViewModel(repository) as T
        }
        throw IllegalAccessException("Unknown ViewModel Class")
    }

}