package readability;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    public static String readFile(String name) {
        String str = "";
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            while (br.ready()) {
                str += br.readLine();
            }
        } catch (IOException ex) {
            System.out.println("reading error");
        }
        return str;
    }

    public static int syllabCounter(String[] text, boolean poly) {
        int count = 0;
        int sylCount = 0;
        int polyCount = 0;
        Pattern pol = Pattern.compile("[^aeiouy]*[aeiouy]+");
        for (String word : text) {
            String newWord;
            if (word.endsWith("e")) {
                newWord = word.substring(0, word.length()-1).toLowerCase().replaceAll("[0-9]", "");
            } else {
                newWord = word.toLowerCase().replaceAll("[0-9]", "");
            }
            Matcher m = pol.matcher(newWord);
            while (m.find()) {
                count++;
            }
            sylCount += (count == 0 ? 1 : count);
            if (count > 2) {
                polyCount++;
            }
            count = 0;
        }
        if (poly) {
            return polyCount;
        }
        return sylCount;
    }


    public static int getAgeByScore(double score) {
        int age = 0;
        switch ((int)Math.floor(score)) {
            case 1: age = 6;
                break;
            case 2: age = 7;
                break;
            case 3: age = 9;
                break;
            case 4: age = 10;
                break;
            case 5: age = 11;
                break;
            case 6: age = 12;
                break;
            case 7: age = 13;
                break;
            case 8: age = 14;
                break;
            case 9: age = 15;
                break;
            case 10: age = 16;
                break;
            case 11: age = 17;
                break;
            case 12: age = 18;
                break;
            case 13:
            case 14: age = 24;
                break;
        }
        return age;
    }

    public static int ARI (int chars, int words, int sentens) {
        double score = 4.71 * ((double)chars / words) + 0.5 * ((double)words / sentens) - 21.43;
        int age = getAgeByScore(score);
        System.out.printf("\n\nAutomated Readability Index: %f (about %d year olds).",score , age);
        return age;
    }
    public static int FK (int words, int sentens, int syllab) {
        double score = 0.39 * ((double)words / sentens) + 11.8 * ((double)syllab / words) - 15.59;
        int age = getAgeByScore(score);
        System.out.printf("\nFlesch–Kincaid readability tests: %f (about %d year olds).",score , age);
        return age;
    }
    public static int SMOG (int polys, int sentens) {
        double score = 1.043 * Math.sqrt(polys * (30.0 / sentens)) + 3.1291;
        int age = getAgeByScore(score);
        System.out.printf("\nSimple Measure of Gobbledygook: %f (about %d year olds).",score , age);
        return age;
    }
    public static int CL (double l, double s) {
        double score = 0.0588 * l - 0.296 * s - 15.8;
        int age = getAgeByScore(score);
        System.out.printf("\nColeman–Liau index: %f (about %d year olds).", score , age);
        return age;
    }

    public static void main(String[] args) throws IOException {
        String file = readFile(args[0]);
        Scanner sc = new Scanner(System.in);
        System.out.println("The text is:\n" + file);
        String[] sntns = file.split("[!?.]");
        int sentences = sntns.length;
        int words = file.split("\\s").length;
        int chars = file.replaceAll("(\\s|\\t|\\n)", "").length();
        int syllab = syllabCounter(file.replaceAll("[!?.]", "").split("\\s"), false);
        int polys = syllabCounter(file.replaceAll("[!?.]", "").split("\\s"), true);
        double l = (double) chars / words * 100;
        double s = (double) sentences / words * 100;
        System.out.printf("\nWords: %d\nSentences: %d\nCharacters: %d\nSyllables: %d\nPolysyllables: %d\n", words, sentences, chars, syllab, polys);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String scoreSystem = sc.nextLine();
        switch (scoreSystem) {
            case "ARI" :
                ARI(chars, words, sentences);
                break;
            case "FK" :
                FK(words, sentences, syllab);
                break;
            case "SMOG" :
                SMOG(polys, sentences);
                break;
            case "CL" :
                CL(l, s);
                break;
            case "all" :
                double avgAge = (ARI(chars, words, sentences) + FK(words, sentences, syllab) + SMOG(polys, sentences) + CL(l, s)) / 4.0;
                System.out.printf("\n\nThis text should be understood in average by %f year olds.", avgAge);
        }
    }
}