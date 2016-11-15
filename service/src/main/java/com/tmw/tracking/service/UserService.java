package com.tmw.tracking.service;

import com.tmw.tracking.domain.LoginRequest;

/**
 * Created by ankultepin on 6/3/2015.
 */
public interface UserService {

    String SYSTEM_ADMIN_MESSAGE = "User is system admin";
    String INCORRECT_CREDENTIALS_MESSAGE = "Incorrect credentials";
    String USER_NOT_FOUND_MESSAGE = "User is not found";

    /**
     * Method checks if login credentials are correct and user role is one of store admin/regional admin/system admin
     * @return instance of CredentialsStatus
     */
    CredentialsStatus validateUserCredentials(LoginRequest loginRequest);

    /**
     * Class contains info about credentials processing
     */
    class CredentialsStatus {
        boolean status;
        String message;

        public CredentialsStatus(boolean status, String message) {
            this.status = status;
            this.message = message;
        }

        public boolean isStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
