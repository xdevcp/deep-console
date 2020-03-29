package cc.devcp.project.provider.enums;

/**
 * @author klw
 * @ClassName: FileTypeEnum
 * @Description: config file type enum
 * @date 2019/7/1 10:21
 */
public enum FileTypeEnum {

    /**
     * @author klw
     * @Description: yaml file
     */
    YML("yaml"),

    /**
     * @author klw
     * @Description: yaml file
     */
    YAML("yaml"),

    /**
     * @author klw
     * @Description: text file
     */
    TXT("text"),

    /**
     * @author klw
     * @Description: text file
     */
    TEXT("text"),

    /**
     * @author klw
     * @Description: json file
     */
    JSON("json"),

    /**
     * @author klw
     * @Description: xml file
     */
    XML("xml"),

    /**
     * @author klw
     * @Description: html file
     */
    HTM("html"),

    /**
     * @author klw
     * @Description: html file
     */
    HTML("html"),

    /**
     * @author klw
     * @Description: properties file
     */
    PROPERTIES("properties");

    /**
     * @author klw
     * @Description: file type corresponding to file extension
     */
    private String fileType;

    FileTypeEnum(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return this.fileType;
    }


}
