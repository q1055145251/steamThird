package com.example.steamThird.utils.bean;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.steamThird.utils.MyPage;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.ibatis.type.SimpleTypeRegistry.isSimpleType;

/**
 * Bean 工具类
 *
 * @author ruoyi
 */
@SuppressWarnings("unused")
public class BeanUtils extends org.springframework.beans.BeanUtils {
    /**
     * Bean方法名中属性名开始的下标
     */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /**
     * 匹配getter方法的正则表达式
     */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /**
     * 匹配setter方法的正则表达式
     */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean属性复制工具方法。
     *
     * @param dest 目标对象
     * @param src  源对象
     */
    public static void copyBeanProp(Object dest, Object src) {
        try {
            copyProperties(src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制属性复杂
     *
     * @param target 目标
     * @param source 源
     */
    public static void copyPropertiesComplex(Object target, Object source) {
        // 获取源对象中的属性列表
        Field[] sourceFields = source.getClass().getDeclaredFields();

        // 逐个属性拷贝
        for (Field sourceField : sourceFields) {
            try {
                // 获取目标对象中同名、同类型的属性
                Field targetField = target.getClass().getDeclaredField(sourceField.getName());
                if (targetField.getType().equals(sourceField.getType())) {
                    // 如果两个属性的类型是相同的，进行属性值拷贝
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);
                    Object sourceValue = sourceField.get(source);
                    // 如果源对象的属性为空，不进行拷贝
                    if (sourceValue != null) {
                        // 如果属性是基本类型或字符串等简单类型，直接拷贝值
                        if (isSimpleType(sourceField.getType())) {
                            targetField.set(target, sourceValue);
                        } else {
                            // 如果属性是复杂类型对象，进行递归拷贝
                            Object targetValue = targetField.getType().getConstructor().newInstance();
                            copyPropertiesComplex(targetValue, sourceValue);
                            targetField.set(target, targetValue);
                        }
                    }
                }
            } catch (Exception e) {
                // 忽略属性拷贝异常
            }
        }
    }

    /**
     * 通过安全复制复杂属性(使用set方法赋值属性)
     *
     * @param target 目标
     * @param source 源
     */
    public static void copyPropertiesComplexBySecure(Object target, Object source) {
        // 获取源对象中的属性列表
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        // 逐个属性拷贝
        for (Field sourceField : sourceFields) {
            try {
                // 获取目标对象中同名、同类型的属性
                Field targetField = target.getClass().getDeclaredField(sourceField.getName());
                if (targetField.getType().equals(sourceField.getType())) {
                    // 如果两个属性的类型是相同的，进行属性值拷贝
                    Method sourceGetMethod = getFieldMethod(sourceClass, sourceField, 1);//获取源字段get方法
                    // 调用get方法获取源值
                    Object sourceValue = sourceGetMethod.invoke(source);
                    // 如果源对象的属性为空，不进行拷贝
                    if (sourceValue != null) {
                        // 如果属性是基本类型或字符串等简单类型，直接拷贝值
                        if (isSimpleType(sourceField.getType())) {
                            Method targetGetMethod = getFieldMethod(targetClass, targetField, 2);//获取源字段set方法
                            targetGetMethod.invoke(target, sourceValue);// 调用set方法赋值
                        } else {
                            // 如果属性是复杂类型对象，进行递归拷贝
                            Object targetValue = targetField.getType().getConstructor().newInstance();
                            copyPropertiesComplexBySecure(targetValue, sourceValue);
                            Method targetGetMethod = getFieldMethod(targetClass, targetField, 2);//获取源字段set方法
                            targetGetMethod.invoke(target, sourceValue);// 调用set方法赋值
                        }
                    }
                }
            } catch (Exception e) {
                // 忽略属性拷贝异常
            }
        }
    }


    /**
     * get字段方法
     * 获取字段get方法
     *
     * @param clazz class
     * @param field 字段
     * @param type  类型 1为get方法 2为set方法
     * @return {@link Method}
     * @throws NoSuchMethodException 没有这样方法例外
     */
    private static Method getFieldMethod(Class<?> clazz, Field field, int type) throws NoSuchMethodException {
        String fieldName = field.getName();
        String methodName;
        switch (type) {
            case 1 -> {
                methodName = "get" + capitalize(fieldName);
                return clazz.getMethod(methodName);
            }
            case 2 -> {
                methodName = "set" + capitalize(fieldName);
                return clazz.getMethod(methodName, field.getType());
            }
            default -> {
                throw new NoSuchMethodException();
            }
        }

    }


    /**
     * 资本化
     *
     * @param str str
     * @return {@link String}
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 复制对象
     *
     * @param target        目标对象
     * @param source        源对象
     * @param ignoredFields 忽略复制的字段名
     */
    public static void shallowCopyProperties(Object target, Object source, String... ignoredFields) {
        try {
            Field[] sourceFields = source.getClass().getDeclaredFields();
            Map<String, Class<?>> tarFieldMap = Arrays.stream(target.getClass().getDeclaredFields()).collect(Collectors.toMap(Field::getName, Field::getType));
            Set<String> ignoredFieldSet = new HashSet<>(Arrays.asList(ignoredFields));
            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true);
                Object sourceValue = sourceField.get(source);
                //如果不是忽略的字段名
                if (!ignoredFieldSet.contains(sourceField.getName())) {
                    //判断是否存在相同字段且类型是否相同
                    Class<?> temp = tarFieldMap.get(sourceField.getName());
                    Class<?> type = sourceField.getType();
                    if (temp != null && (temp.isAssignableFrom(type) || type.isAssignableFrom(temp))) {
                        Field targetField = target.getClass().getDeclaredField(sourceField.getName());
                        targetField.setAccessible(true);
                        targetField.set(target, sourceValue);
                    }
                }
            }
        } catch (Exception e) {
            // 处理拷贝异常
        }
    }


    //map转java对象
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        Object object = beanClass.newInstance();
        BeanMap beanMap = BeanMap.create(object);
        beanMap.putAll(map);
        return object;
    }

    /**
     * page转换
     *
     * @param page 源page
     * @param <T>  转换后的类型
     * @return 转换完成的iPage
     */
    public static <T> MyPage<T> copyBeanPropPage(Page<?> page, Class<T> type) {
        return MyPage.to(page.convert(item -> {
            try {
                T objectVo = type.newInstance();//新建转换后的对象
                BeanUtils.copyBeanProp(objectVo, item);//复制属性
                return objectVo;//返回
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    /**
     * 转换对象类型
     *
     * @param src  源对象
     * @param type 要转换的对象类型
     * @param <T>  转换后的类型
     * @return 返回新对象
     */
    public static <T> T copy(Object src, Class<T> type) {
        T dest = null;
        try {
            dest = type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ignored) {
        }
        BeanUtils.copyBeanProp(dest, src);
        return dest;
    }

    /**
     * 转换对象类型
     *
     * @param src           源对象
     * @param type          要转换的对象类型
     * @param ignoredFields 需要忽略的字段列表
     * @param <T>           转换后的类型
     * @return 返回新对象
     */
    public static <T> T copy(Object src, Class<T> type, String... ignoredFields) {
        T dest;
        try {
            dest = type.getConstructor().newInstance();
            shallowCopyProperties(dest, src, ignoredFields);
            return dest;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换数组类型
     *
     * @param srcList 源数组
     * @param type    要转换成的类型
     * @param <T>     转换后的类型
     * @return 对应的数组
     */
    public static <T> List<T> copy(Collection<?> srcList, Class<T> type) {
        return srcList.stream().map(item -> {
            try {
                return copy(item, type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 转换数组类型
     *
     * @param srcList       源数组
     * @param type          要转换成的类型
     * @param ignoredFields 需要忽略的字段列表
     * @param <T>           转换后的类型
     * @return 对应的数组
     */
    public static <T> List<T> copy(Collection<?> srcList, Class<T> type, String... ignoredFields) {
        return srcList.stream().map(item -> {
            try {
                return copy(item, type, ignoredFields);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }


    /**
     * 获取对象的setter方法。
     *
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static List<Method> getSetterMethods(Object obj) {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods) {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1)) {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }


    /**
     * 获取对象的getter方法。
     *
     * @param obj 对象
     * @return 对象的getter方法列表
     */

    public static List<Method> getGetterMethods(Object obj) {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods) {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0)) {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

    /**
     * 检查Bean方法名中的属性名是否相等。<br>
     * 如getName()和setName()属性名一样，getName()和setAge()属性名不一样。
     *
     * @param m1 方法名1
     * @param m2 方法名2
     * @return 属性名一样返回true，否则返回false
     */

    public static boolean isMethodPropEquals(String m1, String m2) {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }

    /**
     * 获取对象字段结果
     *
     * @param obj  对象
     * @param name 名字
     * @return {@link Object}
     */
    public static Object getObjectFieldResult(Object obj, String name) {
        try {
            Class<?> aClass = obj.getClass();
            Field field = aClass.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }


    /**
     * 找出对象重复字段元素
     *
     * @param list
     * @param keyExtractor
     * @return {@link List}<{@link T}>
     */
    public static <T> List<T> findDuplicates(List<T> list, Function<T, String> keyExtractor) {
        Set<String> set = new HashSet<>();
        Set<String> duplicates = new HashSet<>();

        for (T element : list) {
            String key = keyExtractor.apply(element);
            if (set.contains(key)) {
                duplicates.add(key);
            } else {
                set.add(key);
            }
        }

        return list.stream()
                .filter(element -> duplicates.contains(keyExtractor.apply(element)))
                .collect(Collectors.toList());
    }


}
