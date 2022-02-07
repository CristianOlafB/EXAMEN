package com.example.presentation.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.domain.model.movie.Movies
import com.example.domain.usecase.movie.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _charactersFlow = MutableSharedFlow<PagingData<Movies>>()
    val charactersFlow = _charactersFlow.asSharedFlow()

    init {
        getimpleSearchName("")
    }

    private fun getimpleSearchName(impleSearch: String) {
        getMoviesUseCase(impleSearch).onEach {
            _charactersFlow.emit(it)
        }.launchIn(viewModelScope)
    }

}