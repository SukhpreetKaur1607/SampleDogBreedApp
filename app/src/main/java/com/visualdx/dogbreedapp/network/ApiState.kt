package com.visualdx.dogbreedapp.network

sealed class ApiState{

    object Loading: ApiState()
    object Empty: ApiState()
    class Failure(val msg: Throwable): ApiState()
    class Success(val message: Any?): ApiState()

}

