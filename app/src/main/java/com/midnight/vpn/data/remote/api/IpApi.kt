package com.midnight.vpn.data.remote.api

import retrofit2.http.GET

interface IpApi {
    @GET("?format=text")
    suspend fun getPublicIp(): String
}
