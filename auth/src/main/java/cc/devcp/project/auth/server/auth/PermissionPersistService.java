package cc.devcp.project.auth.server.auth;


import cc.devcp.project.common.model.page.Page;
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
 * Permission CRUD service
 *
 * @author nkorange
 * @since 1.2.0
 */
@Service
public class PermissionPersistService extends PersistService {

    public Page<PermissionInfo> getPermissions(String role, int pageNo, int pageSize) {
        PaginationHelper<PermissionInfo> helper = new PaginationHelper<>();

        String sqlCountRows = "select count(*) from permissions where ";
        String sqlFetchRows
            = "select role,resource,action from permissions where ";

        String where = " role='" + role + "' ";

        if (StringUtils.isBlank(role)) {
            where = " 1=1 ";
        }

        try {
            Page<PermissionInfo> pageInfo = helper.fetchPage(jt, sqlCountRows
                    + where, sqlFetchRows + where, new ArrayList<String>().toArray(), pageNo,
                pageSize, PERMISSION_ROW_MAPPER);

            if (pageInfo==null) {
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

    public void addPermission(String role, String resource, String action) {

        String sql = "INSERT into permissions (role, resource, action) VALUES (?, ?, ?)";

        try {
            jt.update(sql, role, resource, action);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    public void deletePermission(String role, String resource, String action) {

        String sql = "DELETE from permissions WHERE role=? and resource=? and action=?";
        try {
            jt.update(sql, role, resource, action);
        } catch (CannotGetJdbcConnectionException e) {
            fatalLog.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    private static final class PermissionRowMapper implements
        RowMapper<PermissionInfo> {
        @Override
        public PermissionInfo mapRow(ResultSet rs, int rowNum)
            throws SQLException {
            PermissionInfo info = new PermissionInfo();
            info.setResource(rs.getString("resource"));
            info.setAction(rs.getString("action"));
            info.setRole(rs.getString("role"));
            return info;
        }
    }

    private static final PermissionRowMapper PERMISSION_ROW_MAPPER = new PermissionRowMapper();
}
