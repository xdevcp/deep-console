package cc.devcp.project.test.other;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author deep.wu
 * @version 1.0 on 2019/12/17 18:21.
 */
@Slf4j
public class Slf4jTest {

    @Test
    public void logTest01() {
        log.info("test-info");
        log.warn("test-warn");
        log.debug("test-debug");
        log.error("test-error");
    }

}
