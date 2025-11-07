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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// 创建DataStore实例
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    
    companion object {
        // 偏好设置键
        private val CURRENT_USER_KEY = stringPreferencesKey("current_user")
        private val USERS_KEY = stringPreferencesKey("users")
        private val IS_LOGGED_IN_KEY = stringPreferencesKey("is_logged_in")
    }
    
    // 获取当前登录用户
    val currentUser: Flow<User?> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_USER_KEY]?.let { userJson ->
            try {
                Json.decodeFromString<User>(userJson)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    // 获取所有注册用户
    val users: Flow<List<User>> = context.dataStore.data.map { preferences ->
        preferences[USERS_KEY]?.let { usersJson ->
            try {
                Json.decodeFromString<List<User>>(usersJson)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }
    
    // 获取登录状态
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY]?.toBoolean() ?: false
    }
    
    // 保存用户
    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            val currentUsers = preferences[USERS_KEY]?.let {
                try {
                    Json.decodeFromString<List<User>>(it).toMutableList()
                } catch (e: Exception) {
                    mutableListOf()
                }
            } ?: mutableListOf()
            
            // 移除同用户名的用户
            currentUsers.removeIf { it.username == user.username }
            // 添加新用户
            currentUsers.add(user)
            
            preferences[USERS_KEY] = Json.encodeToString(currentUsers)
        }
    }
    
    // 设置当前登录用户
    suspend fun setCurrentUser(user: User?) {
        context.dataStore.edit { preferences ->
            if (user != null) {
                preferences[CURRENT_USER_KEY] = Json.encodeToString(user)
                preferences[IS_LOGGED_IN_KEY] = true.toString()
            } else {
                preferences.remove(CURRENT_USER_KEY)
                preferences[IS_LOGGED_IN_KEY] = false.toString()
            }
        }
    }
    
    // 验证用户登录
    suspend fun validateUser(username: String, password: String): User? {
        val userList = users.first()
        return userList.find { it.username == username && it.password == password }
    }
    
    // 注销
    suspend fun logout() {
        setCurrentUser(null)
    }
} 