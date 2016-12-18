package com.bitbucket.teamleave.leafcalendar.api.service.model;

import com.bitbucket.teamleave.leafcalendar.api.model.UserModel;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"FieldHasSetterButNoGetter", "unused"})
public class UserServiceGetUserInfoModel {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean emailValidated;

    public UserServiceGetUserInfoModel(
            final String firstName,
            final String lastName,
            final String email,
            final Boolean emailValidated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.emailValidated = emailValidated;
    }

    public UserServiceGetUserInfoModel(final UserModel model) {
        this(
                model.getFirstName(),
                model.getLastName(),
                model.getEmail(),
                model.isEmailValidated());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(final Boolean emailValidated) {
        this.emailValidated = emailValidated;
    }
}