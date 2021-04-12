package com.p2p.wallet.auth.ui.security.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.p2p.wallet.databinding.ItemSecurityKeyBinding

class KeysAdapter : RecyclerView.Adapter<KeysAdapter.KeyViewHolder>() {

    private val data = mutableListOf<String>()

    fun setItems(new: List<String>) {
        data.clear()
        data.addAll(new)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        KeyViewHolder(
            ItemSecurityKeyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: KeyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class KeyViewHolder(
        binding: ItemSecurityKeyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val keyTextView = binding.root as TextView

        @SuppressLint("SetTextI18n")
        fun bind(value: String) {
            keyTextView.text = "${adapterPosition + 1}. $value"
        }
    }
}