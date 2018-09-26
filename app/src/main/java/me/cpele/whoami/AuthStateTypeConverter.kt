package me.cpele.whoami

import android.arch.persistence.room.TypeConverter
import net.openid.appauth.AuthState

class AuthStateTypeConverter {
    @TypeConverter
    fun fromJson(json: String): AuthState {
        return AuthState.jsonDeserialize(json)
    }

    @TypeConverter
    fun toJson(auth: AuthState): String {
        return auth.jsonSerializeString()
    }
}
