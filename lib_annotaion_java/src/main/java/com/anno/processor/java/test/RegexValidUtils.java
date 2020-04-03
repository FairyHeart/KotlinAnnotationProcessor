package com.anno.processor.java.test;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: GuaZi.
 * @date : 2020-04-03.
 */
public class RegexValidUtils {
    public static boolean check(Object obj) {
        //获取所有属性
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断成员是否被 @RegexValid 注解所修饰
            if (field.isAnnotationPresent(RegexValid.class)) {
                RegexValid valid = field.getAnnotation(RegexValid.class);
                // 如果 value 为空字符串，说明没有注入自定义正则表达式，改用 policy 属性
                String value = valid.value();
                if ("".equals(value)) {
                    RegexValid.Policy policy = valid.policy();
                    value = policy.getPolicy();
                }

                // 通过设置 setAccessible(true) 来访问私有成员
                field.setAccessible(true);

                Object fieldObj = null;
                try {
                    fieldObj = field.get(obj);
                    if (fieldObj != null) {
                        if (fieldObj instanceof String) {
                            String text = (String) fieldObj;
                            Pattern pattern = Pattern.compile(value);
                            Matcher matcher = pattern.matcher(text);
                            if (matcher.matches()) {
                                return true;
                            } else {
                                throw new Exception(String.format("%s 不是合法的 %s ！", text, field.getName()));
                            }
                        } else {
                            throw new Exception(String.format("%s 类中的 %s 字段不是字符串类型，不能使用此注解校验！",
                                    obj.getClass().getName(), field.getName()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return false;
    }
}
