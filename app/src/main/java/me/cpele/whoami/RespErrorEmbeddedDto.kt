package me.cpele.whoami

data class RespErrorEmbeddedDto(
        val errors: List<RespErrorDetailDto>?,
        val code: Int?,
        val message: String?
)