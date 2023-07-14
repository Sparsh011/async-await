package com.example.apitesting

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    val api = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(ApiInterface::class.java)

    suspend fun getTodos() : Response<FakeModel> {
        return api.getTodos()
    }
    suspend fun getAlbums() : Response<FakeAlbums> {
        return api.getAlbums()
    }
    suspend fun getComments() : Response<FakeComments> {
        return api.getComments()
    }
}