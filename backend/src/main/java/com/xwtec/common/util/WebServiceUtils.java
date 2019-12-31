package com.xwtec.common.util;


//import java.lang.reflect.Field;
//import java.util.*;
//import org.apache.axis.client.Call;
//import org.apache.axis.client.Service;
//import org.apache.axis.client.Call;
//import org.apache.axis.client.Service;
import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.axis2.client.ServiceClient;

/**
 * @Author sunwei
 * @ClassName WebServiceUtils
 * @Date 2019/9/11 16:14
 * @Description webService处理工具类
 */
public class WebServiceUtils {

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key 密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key) throws Exception {
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(text + key);
        System.out.println("MD5加密后的字符串为:encodeStr="+encodeStr);
        return encodeStr;
    }
    public static void main(String[] args){

        try {
            String PWD = md5("SDK-OFT-010-XXXXX","sw422177").toUpperCase();//md5(sn+password) 32位大写密文
            String SN = "SDK-BBX-010-00001";
            String Mobile = "18936896476";
//            String Content = "测试";
            String str = "任意字符串";
            String Content = new String(str.getBytes("utf-8"),"utf-8");
            String Ext=null;
            String para = "SN="+SN+"&PWD="+PWD+"&Mobile="+Mobile+"&Content="+str+"&Ext=&Stime=&Rrid=&msgfmt=";
            HttpClientUtilNew client = HttpClientUtilNew.getInstance();
            String url = "http://sdk2.entinfo.cn:8061/mdsmssend.ashx";
//            http://sdk2.entinfo.cn:8061/mdsmssend.ashx?sn=SN&pwd=MD5(sn+password)&mobile=mobile&content=content&ext=&stime=&rrid=&msgfmt=
            String rtn = client.sendHttpPost(url,para);
            System.out.println(rtn);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
////            String endpoint = "http://172.19.0.153:8080/scs-web/webservice/SignService";
//            String urlname = "http://sdk.entinfo.cn:8060/webservice.asmx";
//            // 直接引用远程的wsdl文件
//// 以下都是套路
//            Service service = new Service();
//            Call call = (Call)service.createCall();
//            call.setOperationName("signContract");// WSDL里面描述的接口名称
//            call.setTargetEndpointAddress(urlname);
//            String SN= "SDK-BBX-010-00001";
//            String PWD="3B5D3C427365F40C1D27682D78BB31E0";
//            String Mobile = "18936896476";
//            String Content = "测试";
//            String Ext =  "";
//            String Stime =  "";
//            String Rrid =  "";
//
//            Object[] fn01 = { SN, PWD, Mobile, Content, Ext, Stime, Rrid };
//            String val = (String) call.invoke(fn01);
//            System.out.println(val);
//        }catch (Exception e) {
//            System.err.println(e.toString());
//        }
    }
}
