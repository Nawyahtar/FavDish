package com.example.favdish.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.favdish.model.entities.FavDish

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query(value = "SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): LiveData<List<FavDish>>

    @Delete
    suspend fun onDelete(favDish: FavDish)

    @Update
    suspend fun onUpdate(favDish: FavDish)

    @Query(value = "SELECT * FROM FAV_DISHES_TABLE WHERE favourite_dish = :favouriteDish")
    fun getFavDishesList(favouriteDish : Boolean = true):LiveData<List<FavDish>>

    @Query(value = "SELECT * FROM FAV_DISHES_TABLE ORDER BY RANDOM() LIMIT 1")
    fun getRandomDishLive(): LiveData<FavDish>

    @Query(value = "SELECT * FROM FAV_DISHES_TABLE ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomDish(): FavDish

    @Query(value = "SELECT * FROM fav_dishes_table")
    fun selectALL(): List<FavDish>



}