package com.midnight.vpn.data.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET

interface IpApi {
    @GET("?format=text")
    suspend fun getPublicIp(): ResponseBody
}
