package com.example.lesson8.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson8.data.PetEntity
import com.example.lesson8.databinding.ActivityMainBinding
import com.example.lesson8.databinding.DialogAddPersonBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy {
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(MainViewModel::class.java)
    }

    private val adapter = PersonsAdapter { viewModel.deletePerson(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.layoutManager = LinearLayoutManager(applicationContext)
        binding.rv.adapter = adapter
        binding.sw.setOnRefreshListener { viewModel.onRefresh() }
        binding.fab.setOnClickListener { viewModel.onFabClick() }

        viewModel.persons.observe(this, adapter::submitList)
        viewModel.isRefreshing.observe(this, binding.sw::setRefreshing)
        viewModel.event.observe(this, ::handleEvent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
    }

    private fun handleEvent(
        event: MainViewModel.Event
    ) = when (event) {
        is MainViewModel.Event.ShowDialog -> showAddDialog(event.pets)
    }

    private fun showAddDialog(pets: List<PetEntity>) {
        var dialog: AlertDialog? = null
        val b = DialogAddPersonBinding.inflate(layoutInflater).apply {
            personName.doAfterTextChanged {
                dialog!!
                    .getButton(AlertDialog.BUTTON_POSITIVE)
                    .isEnabled = !it.isNullOrBlank() && pet.selectedItem != null
            }
            pet.adapter = getDialogSpinnerAdapter(pets)
            pet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    dialog!!
                        .getButton(AlertDialog.BUTTON_POSITIVE)
                        .isEnabled = personName.text.isNotBlank()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    dialog!!
                        .getButton(AlertDialog.BUTTON_POSITIVE)
                        .isEnabled = false
                }
            }
        }

        dialog = AlertDialog.Builder(binding.root.context)
            .setView(b.root)
            .setMessage("????????????????????")
            .setPositiveButton("????????????????") { _, _ ->
                val pet = b.pet.selectedItem as? PetEntity ?: return@setPositiveButton
                viewModel.savePerson(b.personName.text.toString(), pet)
            }
            .setNegativeButton("????????????") { _, _ ->  }
            .show()
    }

    private fun DialogAddPersonBinding.getDialogSpinnerAdapter(
        pets: List<PetEntity>
    ) = object : ArrayAdapter<PetEntity>(root.context, android.R.layout.simple_spinner_item, pets) {

        init {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val tv = super.getView(position, convertView, parent) as TextView
            tv.text = pets[position].name
            return tv
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val tv = super.getDropDownView(position, convertView, parent) as TextView
            tv.text = pets[position].name
            return tv
        }
    }
}