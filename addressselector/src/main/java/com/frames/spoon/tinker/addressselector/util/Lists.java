package com.frames.spoon.tinker.addressselector.util;

import java.util.List;

/**
 * Created by zhanxiaolin-n22 on 2017/3/13.
 */

public class Lists {
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }
}
