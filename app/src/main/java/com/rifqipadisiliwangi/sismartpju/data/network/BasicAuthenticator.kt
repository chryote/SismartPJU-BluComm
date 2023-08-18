package com.rifqipadisiliwangi.sismartpju.data.network

import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.http.Headers

class BasicAuthenticator(private val username: String, private val password: String) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val credential = Credentials.basic(username, password)
        return response.request.newBuilder()
            .header("Authorization", credential)
            .build()
    }
}