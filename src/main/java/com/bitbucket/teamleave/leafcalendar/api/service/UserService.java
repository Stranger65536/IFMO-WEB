package com.bitbucket.teamleave.leafcalendar.api.service;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.InternalServerException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserAlreadyRegisteredException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserAlreadyValidatedException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserInvalidCredentialsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import com.bitbucket.teamleave.leafcalendar.api.model.UserModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceGetUserInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceLoginModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceRegistrationModel;
import com.bitbucket.teamleave.leafcalendar.api.transfer.users.request.UpdateUserInfoRequestBody;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface UserService {
    UserServiceRegistrationModel registerUser(
            final String firstName,
            final String lastName,
            final String email,
            final String password) throws
            UserModelException, UserAlreadyRegisteredException,
            InternalServerException;

    void validateUser(
            final String userId,
            final String validationToken) throws
            UserModelException, UserAlreadyValidatedException,
            UserNotExistsException;

    UserServiceLoginModel loginUser(
            final String email,
            final String password) throws
            UserModelException, UserNotExistsException,
            UserInvalidCredentialsException;

    void updatePassword(
            final String userId,
            final String authToken,
            final String oldPassword,
            final String newPassword) throws
            UserModelException, AuthTokenModelException,
            UserNotExistsException, InternalServerException,
            UserInvalidCredentialsException;

    void updateEmail(
            final String userId,
            final String authToken,
            final String password,
            final String newEmail) throws
            UserModelException, InternalServerException,
            UserInvalidCredentialsException, AuthTokenModelException,
            UserNotExistsException;

    void sendValidationMail(
            final String userId,
            final String authToken) throws
            AuthTokenModelException, UserAlreadyValidatedException,
            UserNotExistsException, UserModelException;

    void updateAuthToken(
            final String userId,
            final String authToken) throws
            AuthTokenModelException, UserNotExistsException, UserModelException;

    UserServiceGetUserInfoModel getUserInfo(
            final String userId,
            final String authToken) throws
            AuthTokenModelException, UserNotExistsException, UserModelException;

    void updateUserInfo(
            final String userId,
            final String authToken,
            final UpdateUserInfoRequestBody request) throws
            AuthTokenModelException, UserNotExistsException,
            UserModelException;

    void deleteUser(
            final String userId,
            final String authToken,
            final String password) throws
            UserInvalidCredentialsException,
            AuthTokenModelException, UserNotExistsException,
            UserModelException;

    void fetchModelByTokenIdAndCheckId(
            final String userId,
            final String authToken,
            final UserModel model) throws
            AuthTokenModelException, UserNotExistsException;
}