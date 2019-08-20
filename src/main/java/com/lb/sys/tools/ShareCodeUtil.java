/**
 * 
 */
package com.lb.sys.tools;

/**
 * @author wz
 * @describe 
 * @date 2017��10��27��
 */
import java.util.Random;

/**
 * ���������������㷨ԭ��<br/>
 * 1) ��ȡid: 1127738 <br/>
 * 2) ʹ���Զ������תΪ��gpm6 <br/>
 * 3) תΪ�ַ��������ں����'o'�ַ���gpm6o <br/>
 * 4���ں�������������ɸ���������ַ���gpm6o7 <br/>
 * תΪ�Զ�����ƺ�Ͳ������o����ַ���Ȼ���ں���Ӹ�'o'����������ȷ��Ψһ�ԡ�����ں������һЩ����ַ����в�ȫ��<br/>
 * @author jiayu.qiu
 */
public class ShareCodeUtil {

	 /***/
    //private static final char[] r=new char[]{'q', 'w', 'e', '8', 'a', 's', '2', 'd', 'z', 'x', '9', 'c', '7', 'p', '5', 'i', 'k', '3', 'm', 'j', 'u', 'f', 'r', '4', 'v', 'y', 'l', 't', 'n', '6', 'b', 'g', 'h'};
    private static final char[] r=new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
    /** */
    private static final char b='0';

    /***/
    private static final int binLen=r.length;

    /** */
    private static final int s=5;

    /**
     * ����ID������λ�����
     * @param id ID
     * @return �����
     */
    public static String toSerialCode(long id) {
        char[] buf=new char[32];
        int charPos=32;

        while((id / binLen) > 0) {
            int ind=(int)(id % binLen);
            buf[--charPos]=r[ind];
            id /= binLen;
        }
        buf[--charPos]=r[(int)(id % binLen)];
        String str=new String(buf, charPos, (32 - charPos));
        // �������ȵ��Զ������ȫ
        if(str.length() < s) {
            StringBuilder sb=new StringBuilder();
            sb.append(b);
            Random rnd=new Random();
            for(int i=1; i < s - str.length(); i++) {
            sb.append(r[rnd.nextInt(binLen)]);
            }
            str+=sb.toString();
        }
        return str;
    }

    public static long codeToId(String code) {
        char chs[]=code.toCharArray();
        long res=0L;
        for(int i=0; i < chs.length; i++) {
            int ind=0;
            for(int j=0; j < binLen; j++) {
                if(chs[i] == r[j]) {
                    ind=j;
                    break;
                }
            }
            if(chs[i] == b) {
                break;
            }
            if(i > 0) {
                res=res * binLen + ind;
            } else {
                res=ind;
            }
        }
        return res;
    }
}