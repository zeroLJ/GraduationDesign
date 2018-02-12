package zero.com.utillib.utils.object;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ws on 2017/7/21.
 */

public class StringUtils {

    /**
     * 判断String是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断String不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && !str.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为数字类型含负号“-”的数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 判断是否为数字类型含负号“-”包含小数点
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        try {
            Double.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String trimEnd(String str,String end){
        if(str.length()>0){
            if(str.lastIndexOf(end)==(str.length()-end.length())){
                str = str.substring(0,str.length()-end.length());
            }
        }
        return str;
    }
    public static String trimEnd(String str){
        if(str.length()>0){
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    public static String trimStart(String str){
        if(str.length()>0){
            str = str.substring(1);
        }
        return str;
    }

    public static String trimStart(String str,String start) {
        if (str.length() > 0) {
            if(str.indexOf(start)==0){
                str = str.substring(start.length(),str.length());
            }
        }
        return str;
    }
        /**
         * md5加密
         * @param string
         * @return
         */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 判定邮箱格式
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 判断是否是手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 判断是否是身份证号码
     *
     * @param strEmail
     * @return
     */
    public static boolean isIdentify(String strEmail) {
        String strPattern = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 判断是否为网络链接
     *
     * @param url
     * @return
     */
    public static boolean isWebLink(String url) {
        Pattern pattern = Pattern
                .compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return false;
        } else {
            return true;
        }
    }
}
