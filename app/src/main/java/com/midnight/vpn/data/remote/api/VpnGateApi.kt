package com.midnight.vpn.data.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET

interface VpnGateApi {
    @GET("api/iphone/")
    suspend fun getServerList(): ResponseBody
}
