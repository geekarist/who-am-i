package me.cpele.whoami

data class Resource<T>(val value: PersonBo? = null, val error: RespError? = null)
