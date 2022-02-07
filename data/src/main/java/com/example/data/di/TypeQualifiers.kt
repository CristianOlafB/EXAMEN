package com.example.data.di

import javax.inject.Qualifier
/*
Moshi NET
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoshiNetwork

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoshiDefault

/**
 * Coroutines Dispatchers
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher
