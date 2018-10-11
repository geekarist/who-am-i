package me.cpele.whoami

import android.arch.persistence.room.TypeConverter
import net.openid.appauth.AuthState

class AuthStateTypeConverter {
    @TypeConverter
    fun fromJson(json: String): AuthState {
        val deserialized = AuthState.jsonDeserialize(json)
        val custom = CustomAuthState(
                deserialized.lastAuthorizationResponse,
                deserialized.authorizationException
        )
        deserialized.apply {
            if (lastTokenResponse != null || authorizationException != null) {
                custom.update(
                        lastTokenResponse,
                        authorizationException
                )
            }
        }
        return custom
    }

    @TypeConverter
    fun toJson(auth: AuthState): String {
        return auth.jsonSerializeString()
    }
}
