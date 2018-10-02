package me.cpele.whoami

data class RespErrorEmbeddedBo(
        val errors: List<RespErrorDetailBo>?,
        val code: Int?,
        val message: String?
)