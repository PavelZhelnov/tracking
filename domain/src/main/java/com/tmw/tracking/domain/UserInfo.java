package com.tmw.tracking.domain;

import com.tmw.tracking.entity.User;

import java.util.Date;

/**
 * @author dmikhalishin@provectus-it.com
 */

public class UserInfo {
    private final User user;
    private final Date notBusy;

    public UserInfo(final User user,
                    final Date notBusy){
        this.user = user;
        this.notBusy = notBusy;
    }

    public User getUser() {
        return user;
    }


    public Date getNotBusy() {
        return notBusy;
    }
}
