package cc.devcp.project.test.other;

import cc.devcp.project.common.utils.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author deep.wu
 * @version 1.0 on 2019/12/17 18:21.
 */
@Slf4j
public class StrTest {

    @Test
    public void test01() {
        String mobile = "13512430011";
        mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
        log.info(mobile);
    }

    @Test
    public void test02() {
        String path = PathUtil.instance();
        log.info(path);
    }

}
