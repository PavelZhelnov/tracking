package com.tmw.tracking.service;

import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.entity.User;

import java.util.List;

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

    List<User> getAllUsers();

    User getUserByEmail(String email);

    User getAnyUserByEmail(String email);

    User getById(Long id);

    void update(User user);

    User clearRoles(String email);

    User create(User user);



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
