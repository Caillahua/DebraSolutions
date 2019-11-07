package com.codev.capturalo.data;

/**
 * URL BASE
 */
public class ApiConstants {


    //Auth
    public static final String USER_LOGIN = "login/";
    public static final String USER_REGISTER = "doctors/register/";
    public static final String USER_RETRIEVE = "doctor/";

    //Profile
    public static final String SEARCH_SPECIALiTY = "specialities/";
    public static final String ADD_SPECIALITY = "add-specialities/";
    public static final String UPDATE_PHOTO = "users/photos/";
    public static final String REGISTER_HEALTH_CLINIC = "doctoroffices/";

    public static final String HEALTH_CLINIC = "doctoroffices/{id}/";

    public static final String GET_SCHEDULE_HEALTH_CLINIC ="doctoroffices/{id}/schedules/" ;

    public static final String REGISTER_SCHEDULE = "doctoroffices/register-schedules/";

    public static final String SECRETARIES = "secretaries/";
    public static final String UPDATE_SECRETARIES = "secretaries/{pk}/";
}
