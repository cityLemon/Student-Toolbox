package com.example.guyuefangyuan.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class ScheduleRepository(private val context: Context) {
    
    companion object {
        private val SCHEDULE_KEY = stringPreferencesKey("course_schedule")
    }
    
    // 获取课程表
    val schedule: Flow<CourseSchedule> = context.dataStore.data.map { preferences ->
        preferences[SCHEDULE_KEY]?.let { scheduleJson ->
            try {
                Json.decodeFromString<CourseSchedule>(scheduleJson)
            } catch (e: Exception) {
                CourseSchedule()
            }
        } ?: CourseSchedule()
    }
    
    // 添加课程
    suspend fun addCourse(course: Course) {
        val currentSchedule = schedule.first()
        val updatedCourses = currentSchedule.courses.toMutableList()
        updatedCourses.add(course)
        
        saveSchedule(CourseSchedule(updatedCourses))
    }
    
    // 更新课程
    suspend fun updateCourse(updatedCourse: Course) {
        val currentSchedule = schedule.first()
        val updatedCourses = currentSchedule.courses.toMutableList()
        
        val index = updatedCourses.indexOfFirst { it.id == updatedCourse.id }
        if (index >= 0) {
            updatedCourses[index] = updatedCourse
            saveSchedule(CourseSchedule(updatedCourses))
        }
    }
    
    // 删除课程
    suspend fun deleteCourse(courseId: String) {
        val currentSchedule = schedule.first()
        val updatedCourses = currentSchedule.courses.toMutableList()
        
        updatedCourses.removeIf { it.id == courseId }
        saveSchedule(CourseSchedule(updatedCourses))
    }
    
    // 创建新课程
    fun createCourse(
        name: String,
        teacher: String,
        location: String,
        dayOfWeek: Int,
        startTime: String,
        endTime: String,
        color: Long
    ): Course {
        return Course(
            id = UUID.randomUUID().toString(),
            name = name,
            teacher = teacher,
            location = location,
            dayOfWeek = dayOfWeek,
            startTime = startTime,
            endTime = endTime,
            color = color
        )
    }
    
    // 保存课程表
    private suspend fun saveSchedule(schedule: CourseSchedule) {
        context.dataStore.edit { preferences ->
            preferences[SCHEDULE_KEY] = Json.encodeToString(schedule)
        }
    }
} 