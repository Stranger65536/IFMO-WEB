package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.model.AuthTokenModel;

import javax.sql.DataSource;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface AuthTokenDao {
    void setDataSource(final DataSource dataSource);

    String createToken(final AuthTokenModel tokenModel);

    AuthTokenModel getTokenByTokenId(final AuthTokenModel tokenModel);

    AuthTokenModel getTokenByUserId(final AuthTokenModel tokenModel);

    void updateTokenByTokenId(final AuthTokenModel tokenModel);

    void updateTokenByUserId(final AuthTokenModel tokenModel);
}