package com.example.guyuefangyuan.data

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String,
    val name: String,
    val teacher: String,
    val location: String,
    val dayOfWeek: Int, // 1-7，代表周一到周日
    val startTime: String, // 格式：HH:mm
    val endTime: String, // 格式：HH:mm
    val color: Long // 课程颜色
)

@Serializable
data class CourseSchedule(
    val courses: List<Course> = emptyList()
) 