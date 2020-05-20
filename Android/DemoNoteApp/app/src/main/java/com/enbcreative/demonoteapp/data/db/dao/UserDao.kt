package com.enbcreative.demonoteapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enbcreative.demonoteapp.data.db.model.CURRENT_USER_ID
import com.enbcreative.demonoteapp.data.db.model.User

@Dao
interface UserDao {
    // insert or update user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: User): Long

    @Query("SELECT * FROM User WHERE userId = $CURRENT_USER_ID")
    fun getUser(): LiveData<User>
}