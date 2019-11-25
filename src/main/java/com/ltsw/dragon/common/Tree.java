package com.ltsw.dragon.common;

import java.util.Collection;

/**
 * @author heshaobing
 */

public interface Tree<T> {

    /**
     * id
     *
     * @return
     */
    Long getId();

    /**
     * 父id
     *
     * @return
     */
    Long getParentId();

    /**
     * 子元素
     *
     * @return
     */
    Collection<T> getChildren();

    /**
     * 子元素
     *
     * @param list 子元素
     */
    void setChildren(Collection<T> list);

}
