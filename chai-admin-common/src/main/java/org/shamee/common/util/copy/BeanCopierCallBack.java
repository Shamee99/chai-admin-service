package org.shamee.common.util.copy;

/**
 * @author shamee
 * @date 2021-07-07
 */
@FunctionalInterface
public interface BeanCopierCallBack<S, T> {

    /**
     * 回调
     * @param source
     * @param target
     */
    void exec(S source, T target);
}
