package me.cpele.whoami

data class RespErrorDetailDto(
        val domain: String?,
        val reason: String?,
        val message: String?,
        val extendedHelp: String?
)