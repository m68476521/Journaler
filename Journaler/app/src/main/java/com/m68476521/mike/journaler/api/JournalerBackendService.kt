package com.m68476521.mike.journaler.api

import com.m68476521.mike.journaler.model.Note
import com.m68476521.mike.journaler.model.Todo
import retrofit2.Call
import retrofit2.http.*

interface JournalerBackendService {
    companion object {
        fun obtain(): JournalerBackendService {
            return BackendServiceRetrofit
                    .obtain()
                    .create(JournalerBackendService::class.java)
        }
    }
    
    @POST("authenticate")
    fun login(
            @HeaderMap headers: Map<String, String>,
            @Body payload: UserLoginRequest
    ): Call<JournalerApiToken>

    @GET("notes")
    fun getNotes(
            @HeaderMap headers: Map<String, String>
    ): Call<List<Note>>

    @GET("todos")
    fun getTodos(
            @HeaderMap headers: Map<String, String>
    ): Call<List<Todo>>

    @PUT("entity/note")
    fun publishNotes(
            @HeaderMap headers: Map<String, String>,
            @Body payload: List<Note>
    ): Call<Unit>

    @PUT("entity/todo")
    fun publishTodos(
            @HeaderMap headers: Map<String, String>,
            @Body payload: List<Todo>
    ): Call<Unit>

    @DELETE("entity/note")
    fun removeNotes(
            @HeaderMap headers: Map<String, String>,
            @Body payload: List<Note>
    ): Call<Unit>

    @DELETE("entity/todo")
    fun removeTodos(
            @HeaderMap headers: Map<String, String>,
            @Body payload: List<Todo>
    ): Call<Unit>
}