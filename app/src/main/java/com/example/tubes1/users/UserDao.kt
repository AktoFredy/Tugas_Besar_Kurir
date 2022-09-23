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

    @Query("SELECT * FROM User WHERE idU=:idInput")
    suspend fun getUser2(idInput: Int) : List<User>

    @Query("SELECT * FROM User WHERE username=:inp_uname")
    suspend fun getUser(inp_uname: String) : List<User>
}