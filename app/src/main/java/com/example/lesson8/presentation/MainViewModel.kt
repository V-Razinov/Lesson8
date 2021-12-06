package com.example.lesson8.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson8.App
import com.example.lesson8.data.MyDb
import com.example.lesson8.data.entity.PersonEntity
import com.example.lesson8.data.entity.PetEntity
import com.example.lesson8.other.SingleLiveEvent
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {

    val isRefreshing = MutableLiveData(false)
    val persons = MutableLiveData<List<PersonEntity>>()
    val event = SingleLiveEvent<Event>()

    private val scope = CoroutineScope(Job() + Dispatchers.Main.immediate)
    private val db by lazy { MyDb.getInstance(App.instance, scope) }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    fun refresh() {
        scope.launch {
            delay(100)
            persons.value = withContext(Dispatchers.IO) { db.personDao().readAll() }
            isRefreshing.value = false
        }
    }

    fun showAddDialog() {
        scope.launch {
            val pets = withContext(Dispatchers.IO) { db.petsDao().readAll() }
            event.value = Event.ShowDialog(pets = pets)
        }
    }

    fun savePerson(person: PersonEntity) {
        scope.launch {
            persons.value = withContext(Dispatchers.IO) {
                db.personDao().insertAll(person)
                db.personDao().readAll()
            }
        }
    }

    fun deletePerson(person: PersonEntity) {
        scope.launch {
            persons.value = withContext(Dispatchers.IO) {
                db.personDao().delete(person)
                db.personDao().readAll()
            }
        }
    }

    sealed class Event {
        class ShowDialog(val pets: List<PetEntity>) : Event()
    }
}