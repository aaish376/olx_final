package com.example.olx

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface apiService {

    @POST("User/Login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("User/Register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Boolean>

    @Multipart
    @POST("User/PostAd")
    suspend fun postAd(
        @Part("ad") ad: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Boolean>

    @GET("User/GetAllAds")
    suspend fun getAllAds(@Query("id") id: Int?=null): Response<List<Ad>>

    @POST("User/DeleteAd")
    suspend fun deleteAd(@Query("id") adId: Int): Response<Boolean>

    @Multipart
    @POST("User/UpdateAd")
    suspend fun updateAd(
        @Query("id") id: Int,
        @Part("ad") ad: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Boolean>



//    @GET("Admin/GetTest")
//    suspend fun GetTest(): test



//    @GET("Adventure/GetAll")
//    suspend fun GetAllAdventure(): List<adventures>
//
//    @GET("Resturant/GetAll")
//    suspend fun GetAllResturant(): List<resturant>
//
//    @GET("Hotel/GetAll")
//    suspend fun GetAllHotels():List<hotel>//1
//
//    @GET("HotelDetail/GetAll")
//    suspend fun GetAllHotelDetails():List<hotelDetails>//2
//
//    @GET("ResturantDetail/GetAll")
//    suspend fun GetAllResturantDetails():List<resturantDetails>//3
//
//    @DELETE("Adventure/DeleteById")
//    suspend fun deleteAdventure(@Query("id") id:Int): Response<Void>//4
//
//
//
//
//
//    @GET("Resturant/GetResturantByCountryId")
//    suspend fun getResturantDetails(@Query("countryId") countryId: Int): List<resturant>
//
//    @GET("ResturantDetail/GetByResturantId")
//    suspend fun getMoreResturantDetails(@Query("id") id: Int): resturantDetails
//
//    @GET("Resturant/GeById")
//    suspend fun getByIdResturant(@Query("Id") Id: Int): resturant
//
//
//
//    @GET("Adventure/GetAdventureById")
//    suspend fun GetAdventureById(@Query("Id") Id: Int): adventures
//
//    @GET("Hotel/GeById")
//    suspend fun getByIdHotel(@Query("Id") Id: Int): hotel
//
//    @GET("Hotel/GetHotelByCountryId")
//    suspend fun getCountryDetails(@Query("countryId") countryId: Int): List<hotel>
//
//    @GET("HotelDetail/GetByhotelId")
//    suspend fun getMoreHotelDetails(@Query("id") adventureId: Int): hotelDetails
//
//
//    @GET("Hotel/GetHotelById")
//    suspend fun getHotelById(@Query("Id") Id: Int): hotel
//
//
//    @POST("Adventure/Add") // This should match the route in your .NET controller
//    suspend fun addAdventure(@Body adventure: adventures):Response<Int>


}