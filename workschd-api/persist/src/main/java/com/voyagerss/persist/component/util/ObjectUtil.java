package com.voyagerss.persist.component.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

@Component
public class ObjectUtil {
    public static HashMap<String, Object> compareObjects(Object beforeObj, Object afterObj, String[] ignoreProperties) {
        HashMap<String, Object> differentFields = new HashMap<>();

        HashMap<String, Object> before = new HashMap<>();
        HashMap<String, Object> after = new HashMap<>();
        HashMap<String, Object> compareMap = new HashMap<>();

        // 객체의 클래스 가져오기
        Class<?> clazz = beforeObj.getClass();

        // 모든 필드에 대해 반복하여 비교
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // private 필드에 접근하기 위해 접근 가능하도록 설정

            try {
                // 두 객체의 해당 필드 값 비교
                Object beforeValue = field.get(beforeObj);
                Object afterValue = field.get(afterObj);

                // 두 값이 모두 null인 경우 동일한 것으로 간주하고 다음 필드로 이동
                if (beforeValue == null && afterValue == null) continue;

                if ((beforeValue == null && afterValue != null) || !beforeValue.equals(afterValue)) {
                    if (Arrays.stream(ignoreProperties).anyMatch(s -> s.equals(field.getName()))) continue;
                    differentFields.put(field.getName(), beforeValue + " -> " + afterValue);
                    before.put(field.getName(), beforeValue);
                    after.put(field.getName(), afterValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
//        differentFields.put("compare", compareMap);
//        differentFields.put("before", before);
//        differentFields.put("after", after);// 값이 다른 경우, 필드 이름을 리스트에 추가

        return differentFields;
    }
}
