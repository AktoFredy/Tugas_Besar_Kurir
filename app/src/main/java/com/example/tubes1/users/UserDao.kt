package com.example.tubes1

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM User")
    suspend fun getUsers() : List<User>

    @Query("SELECT * FROM User WHERE idU =:user_id")
    suspend fun getUser(user_id: Int) : List<User>
}