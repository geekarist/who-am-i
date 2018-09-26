package me.cpele.whoami

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Auth::class], version = 1)
abstract class CustomDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
}
