package com.reringuy.support.helper

sealed class OperationHandler<out T> {
    //    Loading the entity, example: calling a function and loading the result
    object Loading : OperationHandler<Nothing>()

    //    Waiting to be used, example: initialized the state and waiting for the first event
    object Waiting : OperationHandler<Nothing>()

    //    Result loaded with the expected type
    data class Success<T>(val data: T) : OperationHandler<T>()

    //    Result no loaded or got an error
    data class Error(val message: String) : OperationHandler<Nothing>()
}