/*
 * Copyright (C) 2020-2025 Raf
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package gr.ihu.iee.aboard.android.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.ihu.iee.aboard.android.data.notifications.NotificationsDataSourceImpl
import gr.ihu.iee.aboard.android.data.notifications.NotificationsRepository
import gr.ihu.iee.aboard.android.domain.notifications.NotificationsDataSource
import gr.ihu.iee.aboard.android.framework.notifications.NotificationsApi
import gr.ihu.iee.aboard.android.framework.notifications.NotificationsRepositoryImpl
import gr.ihu.iee.aboard.android.util.API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {

    @Singleton
    @Provides
    fun provideNotificationsApi(@ApiClient okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): NotificationsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()

        return retrofit.create(NotificationsApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface NotificationsBindsModule {

    @Binds
    fun bindNotificationsDataSource(dataSource: NotificationsDataSourceImpl): NotificationsDataSource

    @Binds
    fun bindNotificationsRepository(repository: NotificationsRepositoryImpl): NotificationsRepository
}