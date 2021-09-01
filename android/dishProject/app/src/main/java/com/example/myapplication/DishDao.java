package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DishDao {
    @Query("SELECT * FROM dish")
    List<Dish> getAll();

    @Insert
    void insertDishes(Dish... dishes);

    @Delete
    void deleteDish(Dish dish);
}
