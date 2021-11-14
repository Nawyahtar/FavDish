package com.example.favdish.viewmodel

import androidx.lifecycle.*
import com.example.favdish.model.database.FavDishRepository
import com.example.favdish.model.entities.FavDish
import kotlinx.coroutines.launch

class RandomDishViewModel(private val repository: FavDishRepository) : ViewModel() {

    val randomDish = MutableLiveData<FavDish>()

    init {

        randomDishLoad()
    }

    fun randomDishLoad() {
        viewModelScope.launch {
            randomDish.value = repository.randomDish()
        }
    }
    fun update(dish: FavDish) = viewModelScope.launch {
        repository.updateTheDish(dish)
        randomDish.value = dish
    }

    val allDishesList: LiveData<List<FavDish>> = repository.allDishesList
}
 class RandomDishViewModelFactory(private val repository: FavDishRepository) : ViewModelProvider.Factory{
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
         if (modelClass.isAssignableFrom(RandomDishViewModel::class.java)){
             return RandomDishViewModel(repository) as T
         }
         throw IllegalAccessException("Unknown ViewModel Class")
     }

 }