package me.cpele.whoami

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import net.openid.appauth.AuthState

@Entity
data class Auth(@PrimaryKey(autoGenerate = true) val id: Int = 0, val state: AuthState)
