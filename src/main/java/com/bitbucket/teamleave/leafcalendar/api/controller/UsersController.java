package com.bitbucket.teamleave.leafcalendar.api.controller;

import com.bitbucket.teamleave.leafcalendar.api.facades.UserFacade;
import com.bitbucket.teamleave.leafcalendar.api.transfer.GenericResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.transfer.users.request.*;
import com.bitbucket.teamleave.leafcalendar.api.transfer.users.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.bitbucket.teamleave.leafcalendar.api.controller.ContextPaths.*;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.AUTH_TOKEN;

/**
 * @author vladislav.trofimov@emc.com
 */
@RestController
@SuppressWarnings("StringConcatenationMissingWhitespace")
@RequestMapping(
        value = USERS,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {
    @Autowired
    private UserFacade userFacade;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<RegistrationResponseData> registerUser(
            @RequestBody final RegistrationRequestBody request) {
        return userFacade.registerUser(request);
    }

    @RequestMapping(
            value = USER_ID_PATH + VALIDATE,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<ValidationResponseData> validateUser(
            @PathVariable(USER_ID) final String userId,
            @RequestBody final ValidationRequestBody request) {
        return userFacade.validateUser(userId, request);
    }

    @RequestMapping(
            value = LOGIN,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<LoginResponseData> loginUser(
            @RequestBody final LoginRequestBody request) {
        return userFacade.loginUser(request);
    }

    @RequestMapping(
            value = USER_ID_PATH + UPDATE + PASSWORD,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<UpdatePasswordResponseData> updatePassword(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @RequestBody final UpdatePasswordRequestBody request) {
        return userFacade.updatePassword(userId, authToken, request);
    }

    @RequestMapping(
            value = USER_ID_PATH + UPDATE + EMAIL,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<UpdateEmailResponseData> updateEmail(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @RequestBody final UpdateEmailRequestBody request) {
        return userFacade.updateEmail(userId, authToken, request);
    }

    @RequestMapping(
            value = USER_ID_PATH + NOTIFY + VALIDATE,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<SendValidationMailResponseData> sendValidationMail(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId) {
        return userFacade.sendValidationMail(userId, authToken);
    }

    @RequestMapping(
            value = USER_ID_PATH + LOGOUT,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<UpdateAuthTokenResponseData> updateToken(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId) {
        return userFacade.updateAuthToken(userId, authToken);
    }

    @RequestMapping(
            value = USER_ID_PATH,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<GetUserInfoResponseData> getUserInfo(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId) {
        return userFacade.getUserInfo(userId, authToken);
    }

    @RequestMapping(
            value = USER_ID_PATH + UPDATE,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<UpdateUserInfoResponseData> updateUserInfo(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @RequestBody final UpdateUserInfoRequestBody request) {
        return userFacade.updateUserInfo(userId, authToken, request);
    }

    @RequestMapping(
            value = USER_ID_PATH,
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<DeleteUserResponseData> deleteUser(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @RequestBody final DeleteUserRequestBody request) {
        return userFacade.deleteUser(userId, authToken, request);
    }
}