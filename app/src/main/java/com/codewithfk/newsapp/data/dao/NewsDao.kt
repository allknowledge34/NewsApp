package com.codewithfk.newsapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.codewithfk.newsapp.data.model.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getNews(): Flow<List<News>>

    @Insert
    suspend fun addNews(news: News)

    @Query("SELECT * FROM news WHERE id = :id")
    fun getNewsById(id: Int): Flow<News?>


    @Delete
    suspend fun deleteNews(news: News)
}