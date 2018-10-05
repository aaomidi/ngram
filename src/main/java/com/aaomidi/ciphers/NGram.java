package com.aaomidi.ciphers;

import com.aaomidi.ciphers.engine.Frequency;
import com.aaomidi.ciphers.engine.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NGram {
    private static Map<Integer, String> map = new HashMap<>();

    static {
        map.put(1, "english_monograms.txt");
        map.put(2, "english_bigrams.txt");
        map.put(3, "english_trigrams.txt");
        map.put(4, "english_quadgrams.txt");
    }

    private final Frequency frequency;
    private final int length;

    public NGram(int length) {
        if (length < 1 || length > 4) {
            throw new RuntimeException("NGrams are only available for strings length 1 through 4");
        }

        this.frequency = Parser.read(this.getClass().getClassLoader().getResourceAsStream(map.get(length)));
        this.length = length;
    }

    public NGram(Frequency frequency, int length) {
        this.frequency = frequency;
        this.length = length;
    }

    public static void main(String... args) {
        System.out.println("This library is not meant to be ran as a standalone jar.");
        System.exit(0);
    }

    public static List<NGram> getAllDefaultNGrams() {
        return map.keySet().stream().map(NGram::new).collect(Collectors.toList());
    }

    public double getFitness(String input) {
        double result = 0.0;
        for (String s : split(input)) {
            if (s.length() != length) continue;
            double fitness = getSplitFitness(s);
            result += fitness;
        }

        return result;
    }

    public double getSplitFitness(String s) {
        if (s.length() != length) throw new RuntimeException("Length mismatch");

        return Math.log10(getProbability(s));
    }

    public double getProbability(String s) {
        double value = frequency.getFitness(s);
        if (value <= 0) {
            value = 0.01;
        }

        return value / (double) frequency.getSize();
    }

    private String[] split(String s) {
        int arrayLen = (int) Math.ceil((s.length() / (double) length));
        String[] result = new String[arrayLen];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + length);
            j += length;
        }

        result[lastIndex] = s.substring(j);

        return result;
    }
}
