package com.bfs.mbistro;

import java.util.Collection;

public class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean areEmpty(Collection<?>... itemsToCheck) {
        for (Collection<?> object : itemsToCheck) {
            if (isNotNullNorEmpty(object)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotNullNorEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean noneNullNorEmpty(Collection<?>... itemsToCheck) {
        for (Collection<?> object : itemsToCheck) {
            if (isNullOrEmpty(object)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

}
