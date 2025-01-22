package com.hsdroid.animejikran.utils

sealed class APIState<out T> {
    object EMPTY : APIState<Nothing>()
    object LOADING : APIState<Nothing>()
    class SUCCESS<out T>(val data: T) : APIState<T>()
    class FAILURE(val t: Throwable) : APIState<Nothing>()
}