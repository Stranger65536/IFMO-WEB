package com.bitbucket.teamleave.leafcalendar.api.facades;

import com.bitbucket.teamleave.leafcalendar.api.transfer.GenericResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.transfer.users.request.*;
import com.bitbucket.teamleave.leafcalendar.api.transfer.users.response.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface UserFacade {
    GenericResponseEntity<RegistrationResponseData> registerUser(
            final RegistrationRequestBody request);

    GenericResponseEntity<ValidationResponseData> validateUser(
            final String userId,
            final ValidationRequestBody request);

    GenericResponseEntity<LoginResponseData> loginUser(
            final LoginRequestBody request);

    GenericResponseEntity<UpdatePasswordResponseData> updatePassword(
            final String userId,
            final String authToken,
            final UpdatePasswordRequestBody request);

    GenericResponseEntity<UpdateEmailResponseData> updateEmail(
            final String userId,
            final String authToken,
            final UpdateEmailRequestBody request);

    GenericResponseEntity<SendValidationMailResponseData> sendValidationMail(
            final String userId,
            final String authToken);

    GenericResponseEntity<UpdateAuthTokenResponseData> updateAuthToken(
            final String userId,
            final String authToken);

    GenericResponseEntity<GetUserInfoResponseData> getUserInfo(
            final String userId,
            final String authToken);

    GenericResponseEntity<UpdateUserInfoResponseData> updateUserInfo(
            final String userId,
            final String authToken,
            final UpdateUserInfoRequestBody request);

    GenericResponseEntity<DeleteUserResponseData> deleteUser(
            final String userId,
            final String authToken,
            final DeleteUserRequestBody request);
}
