package com.elorrieta.alumnoclient

import androidx.fragment.app.Fragment

enum class AppFragments(val fragmentClass: Class<out Fragment>) {
    LOGIN(LoginFragment::class.java),
    STUDENT_PROFILE(StudentProfileFragment::class.java),
    TEACHER_PROFILE(TeacherProfileFragment::class.java),
    REGISTER(RegisterFragment::class.java),
    LANDING(LandingFragment::class.java),
    COURSE(CourseFragment::class.java);

    fun newInstance(): Fragment {
        return fragmentClass.newInstance()
    }
}
