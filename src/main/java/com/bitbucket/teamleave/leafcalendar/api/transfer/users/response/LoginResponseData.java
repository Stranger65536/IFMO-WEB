package com.bitbucket.teamleave.leafcalendar.api.transfer.users.response;

import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceLoginModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.AUTH_TOKEN;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.USER_ID;

/**
 * @author vladislav.trofimov@emc.com
 */
public class LoginResponseData {
    @JsonProperty(USER_ID)
    private String userId;
    @JsonProperty(AUTH_TOKEN)
    private String authToken;

    public LoginResponseData(final UserServiceLoginModel source) {
        this.userId = source.getUserId();
        this.authToken = source.getAuthToken();
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