package org.shamee.common.util.copy;

import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * bean转换，基于cglib
 * 注意：使用该方式复制，不能使用@Accessors(chain = true)注解。
 * 该注解将setter返回值改为this。将使复制失效
 *
 * @author shamee
 */
public class BeanCopierUtils {
    private BeanCopierUtils() {
        // Do nothing
    }

    public static void copy(Object from, Object to) {
        BeanCopier copier = BeanCopier.create(from.getClass(), to.getClass(), false);
        copier.copy(from, to, null);
    }

    public static <T,S> T copy(S from, Supplier<T> to, BeanCopierCallBack<S, T> callBack) {
        BeanCopier copier = BeanCopier.create(from.getClass(), to.get().getClass(), false);
        T t = to.get();
        copier.copy(from, t, null);
        if (callBack != null) {
            // 回调
            callBack.exec(from, t);
        }
        return t;
    }

    public static <T> T copy(Object from, Supplier<T> to) {
        return copy(from, to, null);
    }

    /**
     * 集合数据的拷贝
     *
     * @param sources: 数据源类
     * @param target:  目标类::new(eg: UserVO::new)
     * @return
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }


    /**
     * 带回调函数的集合数据的拷贝（可自定义字段拷贝规则）
     *
     * @param sources:  数据源类
     * @param target:   目标类::new(eg: UserVO::new)
     * @param callBack: 回调函数
     * @return
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanCopierCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copy(source, t);
            list.add(t);
            if (callBack != null) {
                // 回调
                callBack.exec(source, t);
            }
        }
        return list;
    }

}
