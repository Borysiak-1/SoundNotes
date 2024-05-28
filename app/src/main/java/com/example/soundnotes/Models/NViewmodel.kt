package com.example.soundnotes.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.soundnotes.Database.DB
import com.example.soundnotes.Database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NViewmodel(application: Application) : AndroidViewModel(application) {

    private  val rep : Repository

    val allnotes: LiveData<List<Note>>

    init{
        val dao = DB.getDatabase(application).getNoteDao()
        rep = Repository(dao)
        allnotes = rep.allNotes
    }

    fun deleteN(note: Note) = viewModelScope.launch(Dispatchers.IO) {

        rep.delete(note)
    }

    fun insertN(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        rep.insert(note)
    }

    fun updateN(note: Note) = viewModelScope.launch(Dispatchers.IO){
        rep.update(note)
    }
}