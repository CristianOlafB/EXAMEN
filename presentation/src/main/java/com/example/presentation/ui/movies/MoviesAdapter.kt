package com.example.presentation.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.domain.model.movie.Movies
import com.example.domain.model.movie.Movies.Companion.HIGH_IMG
import com.example.presentation.R
import com.example.presentation.databinding.ItemMovieBinding

class MoviesAdapter :
    PagingDataAdapter<Movies, MoviesAdapter.CharacterListViewHolder>(DiffUtilMovieItem()) {

    inner class CharacterListViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCharacter(character: Movies) {
            val lowResImage = HIGH_IMG + character.backdrop_path

            binding.apply {
                tvCharacterName.text = character.title
            }

            binding.imgvCharacterImage.apply {
                Glide.with(context)
                    .load(lowResImage)
                    .placeholder(R.drawable.ic_movie)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        getItem(position)?.let { holder.bindCharacter(it) }
    }

}