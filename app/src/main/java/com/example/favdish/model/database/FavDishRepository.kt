package com.example.favdish.model.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.favdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }

    val allDishesList: LiveData<List<FavDish>> = favDishDao.getAllDishesList()

    val favDishesList: LiveData<List<FavDish>> = favDishDao.getFavDishesList()


    suspend fun randomDish(): FavDish = favDishDao.getRandomDish()

    @WorkerThread
    fun randomDishLive(): LiveData<FavDish> = favDishDao.getRandomDishLive()

    @WorkerThread
    suspend fun deleteTheDish(favDish: FavDish){
        favDishDao.onDelete(favDish)
    }

    @WorkerThread
    suspend fun updateTheDish(favDish: FavDish){
        favDishDao.onUpdate(favDish)
    }
}