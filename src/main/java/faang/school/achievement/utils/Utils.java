package faang.school.achievement.utils;

import org.slf4j.helpers.MessageFormatter;

public final class Utils {
    public static String stringFormatting(String formattedString, Object... args) {
        return MessageFormatter.arrayFormat(formattedString, args).getMessage();
    }

    public static <T> String getSimpleClassName(T javaClass) {
        return javaClass.getClass().getSimpleName();
    }

    public static String extractRootCauseMessage(Throwable ex) {
        if (ex == null) {
            return null;
        }

        Throwable root = ex;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }

        String msg = root.getMessage();
        if (msg == null || msg.isBlank()) {
            msg = root.toString();
        }

        int maximumMessageLength = 500;
        boolean truncated = msg.length() > maximumMessageLength;
        if (truncated) {
            msg = msg.substring(0, maximumMessageLength);
        }

        return root.getClass().getSimpleName() + ": " + msg + (truncated
            ? "..."
            : "");
    }
}