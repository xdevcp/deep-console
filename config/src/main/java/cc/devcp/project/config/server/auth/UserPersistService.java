package cc.devcp.project.config.server.auth;

import cc.devcp.project.config.server.model.Page;
import cc.devcp.project.config.server.model.User;
import cc.devcp.project.config.server.service.PersistService;
import cc.devcp.project.config.server.utils.PaginationHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static cc.devcp.project.config.server.utils.LogUtil.fatalLog;

/**
 * User CRUD service
 *
 * @author nkorange
 * @since 1.2.0
 */
@Service
public class UserPersistService extends PersistService {

    public void createUser(String username, String password) {
        String sql = "INSERT into users (username, password, enabled) VALUES (?, ?, ?)";

        try {
            jt.update(sql, username, password, true);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    public void deleteUser(String username) {
        String sql = "DELETE from users WHERE username=?";
        try {
            jt.update(sql, username);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    public void updateUserPassword(String username, String password) {
        try {
            jt.update(
                "UPDATE users SET password = ? WHERE username=?",
                password, username);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT username,password FROM users WHERE username=? ";
        try {
            return this.jt.queryForObject(sql, new Object[]{username}, USER_ROW_MAPPER);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            fatalLog.error("[db-other-error]" + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public Page<User> getUsers(int pageNo, int pageSize) {

        PaginationHelper<User> helper = new PaginationHelper<>();

        String sqlCountRows = "select count(*) from users where ";
        String sqlFetchRows
            = "select username,password from users where ";

        String where = " 1=1 ";

        try {
            Page<User> pageInfo = helper.fetchPage(jt, sqlCountRows
                    + where, sqlFetchRows + where, new ArrayList<String>().toArray(), pageNo,
                pageSize, USER_ROW_MAPPER);
            if (pageInfo == null) {
                pageInfo = new Page<>();
                pageInfo.setTotalCount(0);
                pageInfo.setPageItems(new ArrayList<>());
            }
            return pageInfo;
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    private static final class UserRowMapper implements
        RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum)
            throws SQLException {
            User info = new User();
            info.setUsername(rs.getString("username"));
            info.setPassword(rs.getString("password"));
            return info;
        }
    }

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

}
