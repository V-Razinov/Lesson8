package com.example.lesson8.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson8.App
import com.example.lesson8.data.PersonEntity
import com.example.lesson8.data.PetEntity
import com.example.lesson8.other.SingleLiveEvent
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {

    val isRefreshing = MutableLiveData(false)
    val persons = MutableLiveData<List<PersonEntity>>()
    val event = SingleLiveEvent<Event>()

    private val dbRepository
        get() = App.instance.dbRepository
    private val scope = CoroutineScope(Job() + Dispatchers.Main.immediate)

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    fun onRefresh() {
        scope.launch {
            persons.value = withContext(Dispatchers.IO) { dbRepository.readPersons() }
            isRefreshing.value = false
        }
    }

    fun onFabClick() {
        scope.launch {
            val pets = withContext(Dispatchers.IO) { dbRepository.readPets() }
            event.value = Event.ShowDialog(pets = pets)
        }
    }

    fun savePerson(personName: String, pet: PetEntity) {
        scope.launch {
            persons.value = withContext(Dispatchers.IO) {
                dbRepository.insertPersons(PersonEntity(name = personName, pet = pet))
                dbRepository.readPersons()
            }
        }
    }

    fun deletePerson(person: PersonEntity) {
        scope.launch {
            persons.value = withContext(Dispatchers.IO) {
                dbRepository.deletePerson(person)
                dbRepository.readPersons()
            }
        }
    }

    sealed class Event {
        class ShowDialog(val pets: List<PetEntity>) : Event()
    }
}