
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Crypto {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя .,:-!?";

    public static void main(String[] args) throws Exception {
        run();
    }

    private static void statAnalyze() {
        System.out.println("Введите имя файла для анализа:");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        String encryptedText = getFile(filePath);
        System.out.println("Введите имя файла для статистики:");
        String statFilePath = scanner.nextLine();
        String textForStatistics = getFile(statFilePath);
        HashMap<Character, Integer> decryptedTextStatistics = getCharacterStatistics(encryptedText);
        HashMap<Character, Integer> generalStatistics = getCharacterStatistics(textForStatistics);
        List<Character> ListofKeysFromDecryptedStatistics = new ArrayList<>(decryptedTextStatistics.keySet());
        List<Character> ListofKeysFromGeneralStatistics = new ArrayList<>(generalStatistics.keySet());
        System.out.println(ListofKeysFromDecryptedStatistics);
        System.out.println(ListofKeysFromGeneralStatistics);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ListofKeysFromDecryptedStatistics.size(); i++) {
            for (int j = 0; j < ListofKeysFromGeneralStatistics.size(); j++) {
                char c= ListofKeysFromDecryptedStatistics.set(i, ListofKeysFromGeneralStatistics.get(j));
                stringBuilder.append(c);
            }
        }
        System.out.println(stringBuilder);
    }


    private static HashMap<Character, Integer> getCharacterStatistics(String text) {
        HashMap<Character, Integer> resultAbsolute = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Integer integer = resultAbsolute.get(c);
            if (integer == null) {
                resultAbsolute.put(c, 1);
            } else {
                integer++;
                resultAbsolute.put(c, integer);
            }
        }
        resultAbsolute.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return resultAbsolute;
    }


    static void run() {

        System.out.println("Сделайте выбор согласно списка, введя цифру");
        System.out.println("1 - Шифрование");
        System.out.println("2 - Дешифрование");
        System.out.println("3 - Брут-форс");
        System.out.println("4 - Стат-анализ");
        System.out.println("5 - Выход");

        Scanner scanner = new Scanner(System.in);
        int answer = scanner.nextInt();

        if (answer == 1) {
            encrypt();
        }
        else if (answer == 2){
            decrypt();
        }
        else if (answer == 3){
            brutForce();
        }
        else if (answer == 4){
            statAnalyze();
        }
        else if (answer == 5){
            System.out.println("finish");
        }
    }

    public static void encrypt() {
        System.out.println("Введите полный адрес файла");
        Scanner scanner = new Scanner(System.in);
        String filesPath = scanner.nextLine();
        String file = getFile(filesPath);
        System.out.println("Введите ключ от 1 до 40");
        int key = scanner.nextInt();
        assert file != null;
        writeContentToFile(encryptText(file, key), filesPath, "-encrypted");
        System.out.println("Файл зашифрован");
    }
    private static void decrypt() {
        System.out.println("Введите полный адрес файла");
        Scanner scanner = new Scanner(System.in);
        String filesPath = scanner.nextLine();
        String file = getFile(filesPath);
        System.out.println("Введите ключ от 1 до 40");
        int key = scanner.nextInt();
        assert file != null;
        writeContentToFile(decryptText(file, key), filesPath, "-decrypted");
        System.out.println("Файл расшифрован");
    }
    private static void brutForce() {
        System.out.println("Введите полный путь файла для Брут-форса");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        String file = getFile(filePath);
        for (int i = 0; i < ALPHABET.length(); i++) {
            assert file != null;
            String decryptedText = decryptText(file,i);
            boolean isValid = isValidText(decryptedText);
            if (isValid) {
                System.out.println("Key is " + i);
                writeContentToFile(decryptedText, filePath, "-brutted");
                break;
            }
        }
    }

    private static boolean isValidText(String text) {
        String[] strings = text.split(" ");
        for (String string : strings) {
            if (string.length() > 24) {
                return false;
            }
        }
        System.out.println("Текст понятен?");
        System.out.println(text);
        System.out.println("1. Yes");
        System.out.println("2. No");
        Scanner scanner = new Scanner(System.in);
        int i = scanner.nextInt();
        if (i == 1) {
            return true;
        } else if (i == 2) {
            return false;
        } else {
            System.out.println("Неверный ввод");
        }
        return true;
    }
    static String getFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void writeContentToFile(String content, String prevFilePath, String suffix) {
        int dotIndex = prevFilePath.lastIndexOf(".");
        String fileBeforeDot = prevFilePath.substring(0, dotIndex);
        String fileAfterDot = prevFilePath.substring(dotIndex);
        String newFileName = fileBeforeDot + suffix + fileAfterDot;
        try {
            Files.writeString(Path.of(newFileName), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String encryptText(String text, int key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int charIndex = ALPHABET.indexOf(c);
            int newCharIndex = charIndex + key;
            if (newCharIndex >= ALPHABET.length()) {
                newCharIndex = newCharIndex - ALPHABET.length();
            }
            //newCharIndex = newCharIndex%ALPHABET.charAt(newCharIndex);
            char encryptedChar = ALPHABET.charAt(newCharIndex);
            stringBuilder.append(encryptedChar);
        }
        return stringBuilder.toString();
    }
    private static String decryptText(String encryptedText, int key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < encryptedText.length(); i++) {
            char c = encryptedText.charAt(i);
            int charIndex = ALPHABET.indexOf(c);
            int newCharIndex = charIndex - key;
            if (newCharIndex < 0) {
                newCharIndex = newCharIndex + ALPHABET.length();
            }
            char decryptedChar = ALPHABET.charAt(newCharIndex);
            stringBuilder.append(decryptedChar);
        }
        return stringBuilder.toString();
    }
}
