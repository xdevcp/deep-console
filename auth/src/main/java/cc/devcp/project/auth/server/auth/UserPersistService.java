package cc.devcp.project.auth.server.auth;

import cc.devcp.project.auth.model.User;
import cc.devcp.project.common.model.page.Page;
import cc.devcp.project.provider.service.PersistService;
import cc.devcp.project.provider.utils.PaginationHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static cc.devcp.project.provider.utils.LogUtil.fatalLog;

/**
 * User CRUD service
 *
 * @author nkorange
 * @since 1.2.0
 */
@Service
public class UserPersistService extends PersistService {

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

    static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }

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

}
