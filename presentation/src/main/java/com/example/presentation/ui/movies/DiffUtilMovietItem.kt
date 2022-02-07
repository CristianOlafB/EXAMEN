package com.example.presentation.ui.movies

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.movie.Movies

class DiffUtilMovieItem : DiffUtil.ItemCallback<Movies>() {
    override fun areItemsTheSame(
        oldItem: Movies,
        newItem: Movies
    ) = oldItem.id == newItem.id
    override fun areContentsTheSame(
        oldItem: Movies,
        newItem: Movies
    ) = oldItem == newItem
}