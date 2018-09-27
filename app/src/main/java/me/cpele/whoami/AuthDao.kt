package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface AuthDao {

    @Query("SELECT * FROM Auth LIMIT 1")
    fun get(): LiveData<Auth?>

    @Insert
    fun insert(auth: Auth)

    @Query("DELETE FROM Auth")
    fun clear()
}
