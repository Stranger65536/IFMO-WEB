package com.bitbucket.teamleave.leafcalendar.api.transfer.users.response;

import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceRegistrationModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.USER_ID;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("unused")
public class RegistrationResponseData {
    @JsonProperty(USER_ID)
    private String userId;

    public RegistrationResponseData(final UserServiceRegistrationModel source) {
        this.userId = source.getUserId();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }
}