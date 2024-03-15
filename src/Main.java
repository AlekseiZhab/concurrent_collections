import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static int queueSize = 100;
    public static int totalTexts = 10_000;
    public static int textLength = 100_000;

    public static ArrayBlockingQueue<String> aCounterQueue = new ArrayBlockingQueue<>(queueSize);
    public static ArrayBlockingQueue<String> bCounterQueue = new ArrayBlockingQueue<>(queueSize);
    public static ArrayBlockingQueue<String> cCounterQueue = new ArrayBlockingQueue<>(queueSize);

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < totalTexts; i++) {
                String text = generateText("abc", textLength);
                try {
                    aCounterQueue.put(text);
                    bCounterQueue.put(text);
                    cCounterQueue.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();

        Thread threadA = new Thread(() -> {
            char letter = 'a';
            long maxLength;
            try {
                maxLength = findLetter(aCounterQueue, letter);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Максимальное количество символов " + letter + " в тексте " + maxLength);
        });

        Thread threadB = new Thread(() -> {
            char letter = 'b';
            long maxLength;
            try {
                maxLength = findLetter(bCounterQueue, letter);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Максимальное количество символов " + letter + " в тексте " + maxLength);
        });

        Thread threadC = new Thread(() -> {
            char letter = 'c';
            long maxLength;
            try {
                maxLength = findLetter(cCounterQueue, letter);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Максимальное количество символов " + letter + " в тексте " + maxLength);
        });

        threadA.start();
        threadB.start();
        threadC.start();
    }

    public static long findLetter(ArrayBlockingQueue<String> queue, char letter) throws InterruptedException {
        long maxLength = 0;
        String text;
        long count;
        for (int i = 0; i < totalTexts; i++) {
            text = queue.take();
            count = text.chars().filter(ch -> ch == letter).count();
            if (count > maxLength) {
                maxLength = count;
            }
        }
        return maxLength;

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