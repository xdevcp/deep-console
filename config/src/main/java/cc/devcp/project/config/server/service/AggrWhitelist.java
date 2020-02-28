package cc.devcp.project.config.server.service;

import cc.devcp.project.common.utils.IoUtils;
import cc.devcp.project.config.server.utils.RegexParser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static cc.devcp.project.config.server.utils.LogUtil.defaultLog;
import static cc.devcp.project.config.server.utils.LogUtil.fatalLog;

/**
 * 聚合数据白名单。
 *
 * @author Nacos
 */
@Service
public class AggrWhitelist {

    public static final String AGGRIDS_METADATA = "cc.devcp.project.metadata.aggrIDs";

    /**
     * 判断指定的dataId是否在聚合dataId白名单。
     */
     public static boolean isAggrDataId(String dataId) {
        if (null == dataId) {
            throw new IllegalArgumentException();
        }

        for (Pattern pattern : AGGR_DATAID_WHITELIST.get()) {
            if (pattern.matcher(dataId).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 传入内容，重新加载聚合白名单
     */
     public static void load(String content) {
        if (StringUtils.isBlank(content)) {
            fatalLog.error("aggr dataId whitelist is blank.");
            return;
        }
        defaultLog.warn("[aggr-dataIds] {}", content);

        try {
            List<String> lines = IoUtils.readLines(new StringReader(content));
            compile(lines);
        } catch (Exception ioe) {
            defaultLog.error("failed to load aggr whitelist, " + ioe.toString(), ioe);
        }
    }

    static void compile(List<String> whitelist) {
        List<Pattern> list = new ArrayList<Pattern>(whitelist.size());

        for (String line : whitelist) {
            if (!StringUtils.isBlank(line)) {
                String regex = RegexParser.regexFormat(line.trim());
                list.add(Pattern.compile(regex));
            }
        }
        AGGR_DATAID_WHITELIST.set(list);
    }

    public static List<Pattern> getWhiteList() {
        return AGGR_DATAID_WHITELIST.get();
    }

    // =======================

    static final AtomicReference<List<Pattern>> AGGR_DATAID_WHITELIST = new AtomicReference<List<Pattern>>(
        new ArrayList<Pattern>());
}
