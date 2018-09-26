package me.cpele.whoami

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

@Database(entities = [Auth::class], version = 1)
@TypeConverters(AuthStateTypeConverter::class)
abstract class CustomDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
}
