package com.bitbucket.teamleave.leafcalendar.api.service;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface AuthTokenService {
    String createTokenByUserId(final String userId) throws
            UserNotExistsException, UserModelException;

    String getTokenIdByUserId(final String userId) throws
            UserNotExistsException, UserModelException;

    String getUserIdByTokenId(final String tokenId) throws
            AuthTokenModelException, UserNotExistsException;

    void updateTokenByTokenId(final String tokenId) throws
            AuthTokenModelException, UserNotExistsException;

    void updateTokenByUserId(final String userId) throws
            UserNotExistsException, UserModelException;
}