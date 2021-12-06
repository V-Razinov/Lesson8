package com.example.lesson8.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson8.data.PersonEntity
import com.example.lesson8.databinding.ItemPersonBinding

class PersonsAdapter(
    private val onDelete: (PersonEntity) -> Unit,
) : ListAdapter<PersonEntity, PersonsAdapter.ViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            binding = ItemPersonBinding.inflate(inflater, parent, false),
            onDelete = onDelete,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<PersonEntity>() {
        override fun areItemsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(
        private val binding: ItemPersonBinding,
        private val onDelete: (PersonEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(person: PersonEntity) {
            val age = if (person.age >= 0) person.age.toString() else ""
            binding.personName.text = person.name
            binding.personAge.text = age
            binding.personAge.isVisible = age.isNotEmpty()
            binding.petName.text = person.pet.name
            binding.root.setOnLongClickListener { onDelete(person); true }
        }
    }
}