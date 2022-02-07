package com.example.domain.usecase

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object DatabaseError : Failure()
    abstract class FeatureFailure : Failure()
}