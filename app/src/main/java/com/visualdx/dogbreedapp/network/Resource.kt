package com.visualdx.dogbreedapp.network

sealed class Resource<out T>{
    data class Success<out T>(val value:T):Resource<T>()
    data class Failure(val isNetworkError: Boolean, val errorCode: String): Resource<Nothing>()
}