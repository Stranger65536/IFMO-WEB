package com.bitbucket.teamleave.leafcalendar.api.service.model;

/**
 * @author vladislav.trofimov@emc.com
 */
public class UserServiceLoginModel {
    private String userId;
    private String authToken;

    public UserServiceLoginModel(final String userId, final String authToken) {
        this.userId = userId;
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }
}