package me.cpele.whoami

import net.openid.appauth.*

class CustomAuthState(
        response: AuthorizationResponse?,
        exception: AuthorizationException?
) : AuthState(response, exception) {

    override fun createTokenRefreshRequest(
            additionalParameters: MutableMap<String, String>
    ): TokenRequest {

        if (refreshToken == null) {
            throw IllegalStateException("No refresh token available for refresh request")
        }

        val authResponse = lastAuthorizationResponse ?: throw IllegalStateException(
                "No authorization configuration available for refresh request")

        return authResponse.request.run {
            TokenRequest.Builder(configuration, clientId)
                    .setGrantType(GrantTypeValues.REFRESH_TOKEN)
                    .setScope("")
                    .setRefreshToken(refreshToken)
                    .setAdditionalParameters(additionalParameters)
                    .build()
        }
    }
}
