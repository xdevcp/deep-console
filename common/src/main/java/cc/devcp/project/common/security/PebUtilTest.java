package cc.devcp.project.common.security;

/**
 * 功能说明 Peb加密/解密测试
 *
 * @author pan wu
 */
public class PebUtilTest {

    public static void main(String[] agrs) {
        // 明文
        String text = "123456";
        // 密文
        String encryptText = PebUtil.encrypt(text);
        System.out.println("encryptText=" + encryptText);
        // 解密后的明文
        String decryptText = PebUtil.decrypt(encryptText);
        System.out.println("decryptText=" + decryptText);
    }

}
