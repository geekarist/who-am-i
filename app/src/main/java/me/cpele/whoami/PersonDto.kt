package me.cpele.whoami

data class PersonDto(val name: NameDto?, val image: ImageDto?, val emails: List<EmailDto>?)
