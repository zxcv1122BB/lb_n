/**
 * 
 */
package com.lb.sys.tools;

/**
 * @describe 字符串校验类
 */
public final class StringUtil {
	private StringUtil() {}
	
	/**判断是否是空串*/
	public static boolean isBlank(final CharSequence str)
	{
		int strLen;
        if (str == null || (strLen = str.length()) == 0 || str.equals("null")) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
	}
	/**判断是否Object是空串*/
	public static boolean isBlank(final Object obj)
	{
        if (obj == null) {
            return true;
        }
        return isBlank(obj.toString());
	}
	/**判断是否Object是非空串*/
	public static boolean isNotBlank(final Object obj)
	{
        if (obj == null) {
            return false;
        }
        CharSequence str = obj.toString();
        int strLen;
        if ((strLen = str.length()) == 0 || str.equals("null")) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
	}
	/**验证两个对象是否相等*/
	/*public static boolean constant(Object o1,Object o2) {
		if(o1 == o2) {
			return true;
		}
		if(o1 instanceof String || o2 instanceof String) {
			if(o1 != null && o2 != null) {
				
			}
		}
		return false;
	}*/
}
