package com.bitbucket.teamleave.leafcalendar.api.service.model;

import com.bitbucket.teamleave.leafcalendar.api.model.UserModel;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("unused")
public class UserServiceRegistrationModel {
    private String userId;
    private String validationToken;

    public UserServiceRegistrationModel(final UserModel model) {
        this.userId = model.getUserId();
        this.validationToken = model.getEmailValidationToken();
    }

    public String getValidationToken() {
        return validationToken;
    }

    public void setValidationToken(final String validationToken) {
        this.validationToken = validationToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }
}