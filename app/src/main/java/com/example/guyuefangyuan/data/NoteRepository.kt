package com.example.guyuefangyuan.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// 使用现有的DataStore实例
class NoteRepository(private val context: Context) {
    
    companion object {
        private val NOTES_KEY = stringPreferencesKey("notes")
    }
    
    // 获取所有笔记
    val notes: Flow<List<Note>> = context.dataStore.data.map { preferences ->
        preferences[NOTES_KEY]?.let { notesJson ->
            try {
                Json.decodeFromString<List<Note>>(notesJson)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }
    
    // 保存笔记
    suspend fun saveNote(note: Note) {
        context.dataStore.edit { preferences ->
            val currentNotes = preferences[NOTES_KEY]?.let {
                try {
                    Json.decodeFromString<List<Note>>(it).toMutableList()
                } catch (e: Exception) {
                    mutableListOf()
                }
            } ?: mutableListOf()
            
            // 更新或添加笔记
            val index = currentNotes.indexOfFirst { it.id == note.id }
            if (index >= 0) {
                currentNotes[index] = note
            } else {
                currentNotes.add(note)
            }
            
            preferences[NOTES_KEY] = Json.encodeToString(currentNotes)
        }
    }
    
    // 删除笔记
    suspend fun deleteNote(noteId: String) {
        context.dataStore.edit { preferences ->
            val currentNotes = preferences[NOTES_KEY]?.let {
                try {
                    Json.decodeFromString<List<Note>>(it).toMutableList()
                } catch (e: Exception) {
                    mutableListOf()
                }
            } ?: mutableListOf()
            
            // 删除指定ID的笔记
            currentNotes.removeIf { it.id == noteId }
            
            preferences[NOTES_KEY] = Json.encodeToString(currentNotes)
        }
    }
    
    // 获取单个笔记
    suspend fun getNoteById(noteId: String): Note? {
        val allNotes = notes.first()
        return allNotes.find { it.id == noteId }
    }
} 