package cc.devcp.project.test.entity.generate;

import cc.devcp.project.common.utils.PathUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author deep
 * @desc https://mp.baomidou.com/config/generator-config.html
 */
@Slf4j
public class MybatisPlusGenerate {

    private static final String packageName = "cc.devcp.project.test.module";
    private static final String projectPath = PathUtil.instance() + "/test/src/test";

    private static final String DEFAULT_MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_HIGH_LEVEL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String JDBC_DRIVER_NAME;

    static {
        // mysql JDBC
        try {
            Class.forName(MYSQL_HIGH_LEVEL_DRIVER);
            JDBC_DRIVER_NAME = MYSQL_HIGH_LEVEL_DRIVER;
            log.info("Use Mysql 8 as the driver");
        } catch (ClassNotFoundException e) {
            log.info("Use Mysql as the driver");
            JDBC_DRIVER_NAME = DEFAULT_MYSQL_DRIVER;
        }
    }

    // dev    cpd_1 or cpd_d
//    private static final String DB_URL = "jdbc:mysql://10.82.73.49:3306/cpd_1?characterEncoding=utf8&useSSL=true&nullCatalogMeansCurrent=true&serverTimezone=Asia/Shanghai";
//    private static final String DB_URL = "jdbc:mysql://10.82.73.47:3306/cpd_d?characterEncoding=utf8&useSSL=true&nullCatalogMeansCurrent=true&serverTimezone=Asia/Shanghai";
//    private static final String DB_USERNAME = "cpd_app";
//    private static final String DB_PASSWORD = "Ujjswdeek%^m23";

    // local
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/diamond_devtest?characterEncoding=utf8&useSSL=true&nullCatalogMeansCurrent=true&serverTimezone=Asia/Shanghai";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        // todo 修改
//        String tableName = "failed_reason";
//        String fieldPrefix = "fal";
//        String tableName = "loss_direction";
//        String fieldPrefix = "los";
//        String tableName = "opportunities_follow";
//        String fieldPrefix = "opf";
//        String tableName = "opportunities";
//        String fieldPrefix = "opt";
        String tableName = "load_batch";
        String fieldPrefix = "lb";
//        String tableName = "load_temp_customer";
//        String fieldPrefix = "ltc";
//        String tableName = "cov_tag";
//        String fieldPrefix = "cov";
        // 模块名
        String moduleName = null;
//        moduleName = "def";
//        moduleName = "biz";
        PackageConfig pc = new PackageConfig()
            .setParent(packageName)
            .setModuleName(moduleName);
        // todo 按需开启
        //pc.setController("");
        //pc.setService("");
        //pc.setServiceImpl("");
        generateByTables(fieldPrefix, pc, tableName);
    }

    private static void generateByTables(String fieldPrefix, PackageConfig pc, String... tableNames) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //String projectPath = System.getProperty("user.dir");、

        // 输出文件路径
        // todo 修改

        gc.setOutputDir(projectPath + "/java");
        // 是否覆盖已有文件
        gc.setFileOverride(false);
        // 是否打开输出目录
        gc.setOpen(false);
        // 是否在xml中添加二级缓存配置
        gc.setEnableCache(false);
        // 开发人员
        //todo 修改
        gc.setAuthor("deep");
        //开启 swagger2 模式
        gc.setSwagger2(true);
        //是否关闭BaseColumn
        //gc.setBaseColumnList(false);
        gc.setBaseColumnList(true);
        // 是否生成BaseResultMap
        gc.setBaseResultMap(false);
        //gc.setBaseResultMap(true);
        // 实体,mapper,xml命名方式
        gc.setEntityName("%sEntity");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper.xml");
        // 增加全局配置
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        // todo 修改
        dsc.setDriverName(JDBC_DRIVER_NAME);
        dsc.setUrl(DB_URL);
        dsc.setUsername(DB_USERNAME);
        dsc.setPassword(DB_PASSWORD);
        // todo 修改（如果是PGSql）
        //dsc.setSchemaName("public");
        // 增加数据库配置
        mpg.setDataSource(dsc);
        // 测试连接是否正常
        mpg.getDataSource().getConn();

        // 包配置
        // 增加包配置
        mpg.setPackageInfo(pc);

        //todo 按需开启
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                StringBuilder fullPath = new StringBuilder();
                fullPath.append(projectPath).append("/resources/mapper/");
                if (pc.getModuleName() != null) {
                    fullPath.append(pc.getModuleName());
                }
                fullPath.append("/").append(tableInfo.getEntityName().replace("Entity", "")).append("Mapper").append(StringPool.DOT_XML);
                //System.out.println("generatePath=" + fullPath.toString());
                return fullPath.toString();
            }
        });
        /*cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });*/
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板 基本不需要
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        //templateConfig.setEntity("templates/entity2.java");
        //templateConfig.setController("");
        //templateConfig.setService("");
        //templateConfig.setMapper("");
        //templateConfig.setEntity("");

        //关闭在/src/java/main下生成xml,controller,service
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 数据库表配置
        StrategyConfig strategy = new StrategyConfig();
        // 是否大写命名
        //strategy.isCapitalMode(false);
        // 是否跳过视图
        //strategy.setSkipView(false);
        // 默认表名命名规则下划线转驼峰
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 默认字段名命名规则下划线转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 自定义继承的Entity类全称，带包名
        //strategy.setSuperEntityClass("");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        // 实体是否生成字段常量（默认 false） wrapper可能会用到
        // todo 按需开启
        strategy.setEntityColumnConstant(true);
        // 实体是否为构建者模型（默认 false） 需考虑继承问题
        // todo 按需开启
        strategy.setEntityBuilderModel(true);
        // 实体是否为lombok模型（默认 false）
        // todo 按需开启
        strategy.setEntityLombokModel(true);
        // Boolean类型字段是否移除is前缀（默认 false）
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        // 是否生成实体时，生成字段注解
        strategy.setEntityTableFieldAnnotationEnable(true);
        // 是否生成实体时，生成字段注解
        // todo 按需开启
        //strategy.setVersionFieldName("")
        // 逻辑删除属性名称
        // todo 按需开启
        //strategy.setLogicDeleteFieldName("")
        // 设置需要生成的表名，输入或者固定值
        strategy.setInclude(tableNames);
        // 表前缀
        //strategy.setTablePrefix(scanner("表前缀"));
        // 字段前缀，配置将在字段中替换
        strategy.setFieldPrefix(fieldPrefix);
        // 增加数据库配置
        mpg.setStrategy(strategy);

        // 增加模板引擎
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        mpg.execute();
        log.info("END");
    }

}
