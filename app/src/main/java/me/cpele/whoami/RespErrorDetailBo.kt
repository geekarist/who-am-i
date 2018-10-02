package me.cpele.whoami

data class RespErrorDetailBo(
        val domain: String?,
        val reason: String?,
        val message: String?,
        val extendedHelp: String?
)