package me.cpele.whoami

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao

@Dao
interface AuthDao {
    fun get(): LiveData<Auth>
    fun set(auth: Auth)
}
