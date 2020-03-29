package cc.devcp.project.auth.server.auth;

import cc.devcp.project.provider.model.Page;
import cc.devcp.project.provider.service.PersistService;
import cc.devcp.project.provider.utils.PaginationHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static cc.devcp.project.provider.utils.LogUtil.fatalLog;


/**
 * Role CRUD service
 *
 * @author nkorange
 * @since 1.2.0
 */
@Service
public class RolePersistService extends PersistService {


    public Page<RoleInfo> getRoles(int pageNo, int pageSize) {

        PaginationHelper<RoleInfo> helper = new PaginationHelper<>();

        String sqlCountRows = "select count(*) from (select distinct role from roles) roles where ";
        String sqlFetchRows
            = "select role,username from roles where ";

        String where = " 1=1 ";

        try {
            Page<RoleInfo> pageInfo = helper.fetchPage(jt, sqlCountRows
                    + where, sqlFetchRows + where, new ArrayList<String>().toArray(), pageNo,
                pageSize, ROLE_INFO_ROW_MAPPER);
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

    public Page<RoleInfo> getRolesByUserName(String username, int pageNo, int pageSize) {

        PaginationHelper<RoleInfo> helper = new PaginationHelper<>();

        String sqlCountRows = "select count(*) from roles where ";
        String sqlFetchRows
            = "select role,username from roles where ";

        String where = " username='" + username + "' ";

        if (StringUtils.isBlank(username)) {
            where = " 1=1 ";
        }

        try {
            return helper.fetchPage(jt, sqlCountRows
                    + where, sqlFetchRows + where, new ArrayList<String>().toArray(), pageNo,
                pageSize, ROLE_INFO_ROW_MAPPER);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    public void addRole(String role, String userName) {

        String sql = "INSERT into roles (role, username) VALUES (?, ?)";

        try {
            jt.update(sql, role, userName);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    public void deleteRole(String role) {
        String sql = "DELETE from roles WHERE role=?";
        try {
            jt.update(sql, role);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    public void deleteRole(String role, String username) {
        String sql = "DELETE from roles WHERE role=? and username=?";
        try {
            jt.update(sql, role, username);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    private static final class RoleInfoRowMapper implements
        RowMapper<RoleInfo> {
        @Override
        public RoleInfo mapRow(ResultSet rs, int rowNum)
            throws SQLException {
            RoleInfo roleInfo = new RoleInfo();
            roleInfo.setRole(rs.getString("role"));
            roleInfo.setUsername(rs.getString("username"));
            return roleInfo;
        }
    }

    private static final RoleInfoRowMapper ROLE_INFO_ROW_MAPPER = new RoleInfoRowMapper();
}
