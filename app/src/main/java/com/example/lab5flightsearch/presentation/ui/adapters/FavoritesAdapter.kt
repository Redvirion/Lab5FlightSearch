package com.example.lab5flightsearch.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab5flightsearch.databinding.ItemFavoriteBinding
import com.example.lab5flightsearch.domain.models.FavoriteWithDetails

class FavoritesAdapter(
    private val onDeleteClick: (FavoriteWithDetails) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    private var favorites: List<FavoriteWithDetails> = emptyList()

    fun submitList(list: List<FavoriteWithDetails>) {
        favorites = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount() = favorites.size

    inner class FavoriteViewHolder(
        private val binding: ItemFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favorite: FavoriteWithDetails) {
            binding.tvDeparture.text = "${favorite.departureCode} (${favorite.departureName})"
            binding.tvDestination.text = "→ ${favorite.destinationCode} (${favorite.destinationName})"

            binding.btnDelete.setOnClickListener {
                onDeleteClick(favorite)
            }
        }
    }
}