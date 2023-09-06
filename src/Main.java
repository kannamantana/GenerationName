import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger beautifulWordsWithThree = new AtomicInteger(0);
    static AtomicInteger beautifulWordsWithFour = new AtomicInteger(0);
    static AtomicInteger beautifulWordsWithFive = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        Thread threadPalindrome = new Thread(
                () -> {
                    for (String text : texts) {
                        if (ifStringPalindrome(text)) {
                            updateCounter(text);
                        }
                    }
                }
        );
        threads.add(threadPalindrome);
        threadPalindrome.start();

        Thread threadOfTheSameLetter = new Thread(
                () -> {
                    for (String text : texts) {
                        if (ifConsistsOfTheSameLetter(text)) {
                            updateCounter(text);
                        }
                    }
                }
        );
        threads.add(threadOfTheSameLetter);
        threadOfTheSameLetter.start();

        Thread threadLettersAscending = new Thread(
                () -> {
                    for (String text : texts) {
                        if (ifLettersAscending(text)) {
                            updateCounter(text);
                        }
                    }
                }
        );
        threads.add(threadLettersAscending);
        threadLettersAscending.start();

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("Красивых слов с длиной 3: " + beautifulWordsWithThree + " шт");
        System.out.println("Красивых слов с длиной 4: " + beautifulWordsWithFour + " шт");
        System.out.println("Красивых слов с длиной 5: " + beautifulWordsWithFive + " шт");
    }

    public static void updateCounter(String str) {
        if (str.length() == 3) {
            beautifulWordsWithThree.addAndGet(1);
        } else if (str.length() == 4) {
            beautifulWordsWithFour.addAndGet(1);
        } else if (str.length() == 5) {
            beautifulWordsWithFive.addAndGet(1);
        }
    }

    public static boolean ifStringPalindrome(String str) {
        StringBuilder rev = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            rev.append(str.charAt(i));
        }
        return str.contentEquals(rev);
    }

    public static boolean ifConsistsOfTheSameLetter(String str) {
        char ch = str.charAt(0);
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ch) {
                return false;
            }
        }
        return true;
    }

    public static boolean ifLettersAscending(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) > str.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}