package com.antsfamily.danskflashcards.core.model

import androidx.credentials.exceptions.NoCredentialException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

enum class ErrorType {
    NetworkConnection,
    Server,
    Unknown,
    ;
}

fun Exception.mapToErrorType(): ErrorType {
    return when (this) {
        is UnknownHostException,
        is SocketTimeoutException,
        is NoCredentialException,
        is FirebaseNetworkException,
        is ConnectException -> ErrorType.NetworkConnection
        is FirebaseException -> ErrorType.Server
        else -> ErrorType.Unknown
    }
}