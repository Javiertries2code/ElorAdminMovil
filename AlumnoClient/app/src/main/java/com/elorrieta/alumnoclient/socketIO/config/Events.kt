package com.elorrieta.socketsio.sockets.config

/**
 * The events our client is willing to listen or able to send. It is
 * the same class as in the Java Server
 */
enum class Events(val value: String) {

    ON_LOGIN ("onLogin"),
    ON_LOGOUT ("onLogout"),

    ON_STOP_SERVER("onStopServer"),

    ON_LOGIN_ANSWER ("onLoginAnswer"),

    ON_NOT_REGISTERED ("notRegistered"),

    ON_LOGIN_USER_NOT_FOUND_ANSWER ("onLoginUsernotFound"),

    ON_LOGIN_SUCCESS_ANSWER ("onLoginCorrectAnswer"),

    ON_RESET_PASSWORD("OnResetPassword"),
    ON_RESET_PASSWORD_ANSWER("OnResetPasswordAnswer"),


    ON_SUCCESSFUL_REGISTRATION ("onSuccessfulRegistration"),
    ON_SUCCESSFUL_REGISTRATION_ANSWER ("onSuccessfulRegistrationAnswer"),

    ON_DB_ERROR ("onDbError"),







    ON_GET_ALL_STUDENTS ("onGetAllStudents"),
    ON_GET_ALL_STUDENTS_ANSWER ("onGetAllStudentsAnswer"),

    ON_GET_ALL_TEACHERS ("onGetAllTeachers"),
    ON_GET_ALL_TEACHERS_ANSWER ("onGetAllTeachersAnswer"),

    ON_GET_ALL_MEETINGS ("onGetAllMeetings"),
    ON_GET_ALL_MEETINGS_ANSWER ("onGetAllMeetingsAnswer"),

    ON_GET_ALL_SCHEDULES ("onGetAllSchedules"),
    ON_GET_ALL_SCHEDULES_ANSWER ("onGetAllSchedulesAnswer"),

    ON_GET_ALL_COURSES ("onGetAllCourses"),
    ON_GET_ALL_COURSES_ANSWER ("onGetAllCoursesAnswer"),

    ON_GET_ALL_SUBJECTS ("onGetAllSubjects"),
    ON_GET_ALL_SUBJECTS_ANSWER ("onGetAllSubjectsAnswer"),

    ON_GET_ALL_MEETING_REQUESTS ("onGetAllMeetingRequests"),
    ON_GET_ALL_MEETING_REQUESTS_ANSWER ("onGetAllMeetingRequestsAnswer"),

    ON_GET_ALL_REGISTRATIONS ("onGetAllRegistrations"),
    ON_GET_ALL_REGISTRATIONS_ANSWER ("onGetAllRegistrationsAnswer"),

    ON_GET_ONE_MEETING ("onGetOneMeeting"),
    ON_GET_ONE_MEETING_ANSWER ("onGetOneMeetingAnswer"),

    ON_GET_ONE_STUDENT_SCHEDULE ("onGetOneStudentSchedule"),
    ON_GET_ONE_STUDENT_SCHEDULE_ANSWER ("onGetOneStudentScheduleAnswer"),

    ON_GET_ONE_TEACHER_SCHEDULE ("onGetOneTeacherSchedule"),
    ON_GET_ONE_TEACHER_SCHEDULE_ANSWER ("onGetOneTeacherScheduleAnswer"),

    ON_GET_ONE_MEETING_REQUEST ("onGetOneMeetingRequest"),
    ON_GET_ONE_MEETING_REQUEST_ANSWER ("onGetOneMeetingRequestAnswer"),

    ON_GET_ONE_TEACHER ("onGetOneTeacher"),
    ON_GET_ONE_TEACHER_ANSWER ("onGetOneTeacherAnswer"),

    ON_GET_ONE_STUDENT ("onGetOneStudent"),
    ON_GET_ONE_STUDENT_ANSWER ("onGetOneStudentAnswer"),

    ON_GET_ONE_REGISTRATION ("onGetOneRegistration"),
    ON_GET_ONE_REGISTRATION_ANSWER ("onGetOneRegistrationAnswer"),

    ON_GET_ALL_OF_ONE_REGISTRATIONS_OF_STUDENT ("onGetAllOfOneRegistrationsOfStudent"),
    ON_GET_ALL_OF_ONE_REGISTRATIONS_OF_STUDENT_ANSWER ("onGetAllOfOneRegistrationsOfStudentAnswer");




}