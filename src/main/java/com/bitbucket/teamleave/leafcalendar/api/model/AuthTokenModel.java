package com.bitbucket.teamleave.leafcalendar.api.model;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;

import java.util.regex.Pattern;

import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.AuthTokens.TOKEN_ID;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public class AuthTokenModel {
    private static final Pattern TOKEN_ID_PATTERN = Pattern.compile("^[A-F0-9]{32}$");

    private String tokenId;
    private String userId;

    public static String validateTokenId(final String tokenId) throws
            AuthTokenModelException {
        if (tokenId != null && TOKEN_ID_PATTERN.matcher(tokenId).matches()) {
            return tokenId;
        } else {
            throw new AuthTokenModelException(TOKEN_ID, tokenId);
        }
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(final String tokenId) throws
            AuthTokenModelException {
        this.tokenId = validateTokenId(tokenId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) throws
            UserModelException {
        this.userId = UserModel.validateUserId(userId);
    }
}