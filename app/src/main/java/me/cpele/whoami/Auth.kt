package me.cpele.whoami

import android.arch.persistence.room.Entity
import net.openid.appauth.AuthState

@Entity
data class Auth(val state: AuthState)
