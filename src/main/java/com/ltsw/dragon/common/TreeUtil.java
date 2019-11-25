package com.ltsw.dragon.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author heshaobing
 */
public class TreeUtil {
    public static final Long ROOT_ID = 0L;

    public static <T extends Tree> void parse(Collection<T> list) {
        Map<Long, T> idEntityMap = new HashMap<>(20);

        list.forEach(tree -> idEntityMap.put(tree.getId(), tree));
        list.clear();
        idEntityMap.forEach((id, tree) -> {
            Long parentId = tree.getParentId();
            // parent <= rootId
            if (parentId == null || parentId <= ROOT_ID) {
                list.add(tree);
            } else {
                Tree parent = idEntityMap.get(parentId);
                Collection<T> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(tree);
            }
        });
    }
}
