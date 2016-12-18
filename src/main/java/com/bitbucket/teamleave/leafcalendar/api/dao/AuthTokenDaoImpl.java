package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.model.AuthTokenModel;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;

import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.AuthTokens.AUTH_TOKENS_TABLE;
import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.AuthTokens.TOKEN_ID;
import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.DEFAULT;
import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Users.USER_ID;

/**
 * @author vladislav.trofimov@emc.com
 */
@Repository("AuthTokenDao")
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicCharacter"})
public class AuthTokenDaoImpl extends JdbcDaoSupport implements AuthTokenDao {
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public String createToken(final AuthTokenModel tokenModel) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(
                connection -> {
                    final String query = "INSERT INTO " + AUTH_TOKENS_TABLE + " (" +
                            USER_ID +
                            ") VALUES (?)";
                    PreparedStatement ps = connection.prepareStatement(
                            query, new String[]{TOKEN_ID});
                    DaoUtils.setString(1, ps, tokenModel.getUserId());
                    return ps;
                }, keyHolder);
        return Utils.rawToHex((byte[]) keyHolder.getKeys().get(TOKEN_ID));
    }

    @Override
    public AuthTokenModel getTokenByTokenId(final AuthTokenModel tokenModel) {
        final String query = "SELECT * FROM " + AUTH_TOKENS_TABLE + ' ' +
                "WHERE " + TOKEN_ID + " = ?";
        return getJdbcTemplate().queryForObject(query, new Object[]{
                tokenModel.getTokenId()
        }, new BeanPropertyRowMapper<>(AuthTokenModel.class));
    }

    @Override
    public AuthTokenModel getTokenByUserId(final AuthTokenModel tokenModel) {
        final String query = "SELECT * FROM " + AUTH_TOKENS_TABLE + ' ' +
                "WHERE " + USER_ID + " = ?";
        return getJdbcTemplate().queryForObject(query, new Object[]{
                tokenModel.getUserId()
        }, new BeanPropertyRowMapper<>(AuthTokenModel.class));
    }

    @Override
    public void updateTokenByTokenId(final AuthTokenModel tokenModel) {
        final String query = "UPDATE " + AUTH_TOKENS_TABLE + ' ' +
                "SET " +
                TOKEN_ID + " = " + DEFAULT + ' ' +
                "WHERE " + TOKEN_ID + " = ?";
        final int affectedRows = getJdbcTemplate().execute(query,
                (PreparedStatement ps) -> {
                    DaoUtils.setString(1, ps, tokenModel.getTokenId());
                    return ps.executeUpdate();
                });
        if (affectedRows == 0) {
            throw new EmptyResultDataAccessException(0);
        }
    }

    @Override
    public void updateTokenByUserId(final AuthTokenModel tokenModel) {
        final String query = "UPDATE " + AUTH_TOKENS_TABLE + ' ' +
                "SET " +
                TOKEN_ID + " = " + DEFAULT + ' ' +
                "WHERE " + USER_ID + " = ?";
        final int affectedRows = getJdbcTemplate().execute(query,
                (PreparedStatement ps) -> {
                    DaoUtils.setString(1, ps, tokenModel.getUserId());
                    return ps.executeUpdate();
                });
        if (affectedRows == 0) {
            throw new EmptyResultDataAccessException(0);
        }
    }
}