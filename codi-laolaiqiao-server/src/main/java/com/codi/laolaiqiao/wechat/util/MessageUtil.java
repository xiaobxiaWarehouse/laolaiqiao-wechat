package com.codi.laolaiqiao.wechat.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.codi.base.util.PropertiesUtil;

public class MessageUtil {

    private static Map<String, String> errorMessageMap = PropertiesUtil.doGetProperties("error-message.properties");

    public static String getErrorMessage(Integer errorCode, Map<String, String> valueMap) {

        String errorCodeString = String.valueOf(errorCode);
        try {

            if (errorMessageMap.containsKey(errorCodeString)) {
                String errorMessage = errorMessageMap.get(errorCodeString);
                if (valueMap == null || valueMap.size() == 0) {
                    return errorMessage;
                } else {
                    for (Map.Entry<String, String> entry : valueMap.entrySet()) {
                        errorMessage = errorMessage.replace("{" + entry.getKey() + "}", entry.getValue());
                    }

                    return errorMessage;
                }
            } else {
                return errorCodeString;
            }

        } catch (Exception exception) {
            return errorCodeString;
        }
    }
    
    /***
     * 当只有一个内置参数需要替换的时候可以调用此方法
     * @param 错误代码
     * @param 待替换的标签
     * @param 标签的值
     * @return 返回错误消息
     */
    public static String getErrorMessage(Integer errorCode, String placeHolder, String placeValue) {
    	Map<String, String> map = new HashMap<>();
		map.put(placeHolder, placeValue);
		
		return getErrorMessage(errorCode, map);				
    }

    /**
     * 获取错误信息
     * 
     * @param errorCode
     *            错误编码
     * @param param
     *            不定参数值
     * @return
     */
    public static String getErrorMsg(Integer errorCode, Object... param) {
        return getErrorMsg(String.valueOf(errorCode), param);
    }

    /**
     * 获取错误信息
     * 
     * @param errorCode
     *            错误编码
     * @param param
     *            不定参数值
     * @return
     */
    public static String getErrorMsg(String errorCode, Object... param) {
        try {
            if (errorMessageMap.containsKey(errorCode)) {
                String errorMsg = errorMessageMap.get(errorCode);

                if (param == null || param.length == 0) {
                    return errorMsg;
                } else {
                    MessageFormat mf = new MessageFormat(errorMsg);
                    return mf.format(param);
                }
            } else {
                return errorCode;
            }

        } catch (Exception exception) {
            return errorCode;
        }
    }

}
