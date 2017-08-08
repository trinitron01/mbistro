package com.bfs.mbistro;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import static com.bfs.mbistro.Strings.EMPTY_STRING;

public class JavaUtils {



    public static final int HASH_CODE_31 = 31;

    public static final int HASH_CODE_32 = 32;

    public static final int INT_BYTE_MAX_VALUE = 255;

    public static final float FLOAT_HALF = 0.5f;

    private JavaUtils() {
    }

    public static boolean areNotNullAndNotEqual(Object first, Object second) {
        return JavaUtils.areNotNull(first, second) && !first.equals(second);
    }

    public static boolean areNotNull(Object... itemsToCheck) {
        for (Object object : itemsToCheck) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }

    public static String getEmptyWhenNull(String text) {
      return isNullOrEmptyString(text) ? EMPTY_STRING : text;
    }

    public static boolean isNullOrEmptyString(CharSequence text) {
        return text == null || EMPTY_STRING.equals(text);
    }

    public static boolean isNotNulNorEmptyString(CharSequence text) {
        return !isNullOrEmptyString(text);
    }

    @SuppressWarnings("DynamicRegexReplaceableByCompiledPattern")
    public static String stripDiacritics(String s) {
        return s.replaceAll("ą", "a").
                replaceAll("Ą", "A").
                replaceAll("ć", "c").
                replaceAll("Ć", "C").
                replaceAll("ę", "e").
                replaceAll("Ę", "E").
                replaceAll("ł", "l").
                replaceAll("Ł", "L").
                replaceAll("ń", "n").
                replaceAll("Ń", "N").
                replaceAll("ó", "o").
                replaceAll("Ó", "O").
                replaceAll("ś", "s").
                replaceAll("Ś", "S").
                replaceAll("ź", "z").
                replaceAll("Ź", "Z").
                replaceAll("ż", "z").
                replaceAll("Ż", "Z");
    }

    public static <T> boolean isInstanceOf(Class<T> aClass, Object casted) {
        return casted != null && aClass.isInstance(casted);
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignored) {
            // ignore
        }
    }

    public static boolean isNotNulNorEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
