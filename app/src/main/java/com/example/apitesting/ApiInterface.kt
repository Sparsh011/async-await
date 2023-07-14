package com.example.apitesting

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/todos")
    suspend fun getTodos() : Response<FakeModel>

    @GET("/comments")
    suspend fun getComments() : Response<FakeComments>

    @GET("/albums")
    suspend fun getAlbums() : Response<FakeAlbums>
}