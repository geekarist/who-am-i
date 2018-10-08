package me.cpele.whoami

data class ResourceDto<T>(val value: PersonDto? = null, val error: RespErrorDto? = null)
