package zhaohg.test.helper;

import java.util.Calendar;

import zhaohg.api.Encryption;

public class RandomName {
    public static String generateRandomName(String prefix) {
        Calendar calendar = Calendar.getInstance();
        String text = prefix + calendar.getTimeInMillis();
        return prefix + Encryption.md5(text);
    }
}
