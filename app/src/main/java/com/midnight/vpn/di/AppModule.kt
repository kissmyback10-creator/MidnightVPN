package com.midnight.vpn.di

import com.midnight.vpn.data.remote.api.IpApi
import com.midnight.vpn.data.remote.api.VpnGateApi
import com.midnight.vpn.data.repository.SettingsRepositoryImpl
import com.midnight.vpn.data.repository.VpnServerRepositoryImpl
import com.midnight.vpn.domain.repository.SettingsRepository
import com.midnight.vpn.domain.repository.VpnServerRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                },
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideVpnGateApi(client: OkHttpClient): VpnGateApi {
        return Retrofit.Builder()
            .baseUrl("https://www.vpngate.net/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(VpnGateApi::class.java)
    }

    @Provides
    @Singleton
    fun provideIpApi(client: OkHttpClient): IpApi {
        return Retrofit.Builder()
            .baseUrl("https://api.ipify.org/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(IpApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVpnServerRepository(
        impl: VpnServerRepositoryImpl,
    ): VpnServerRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl,
    ): SettingsRepository
}
