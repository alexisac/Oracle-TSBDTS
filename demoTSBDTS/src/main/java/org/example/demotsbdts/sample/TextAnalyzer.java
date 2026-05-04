package org.example.demotsbdts.sample;

import java.util.HashMap;
import java.util.Map;

public class TextAnalyzer {
    public int countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }

        String[] words = text.trim().split("\\s+");
        return words.length;
    }

    public int countCharactersWithoutSpaces(String text) {
        if (text == null) {
            return 0;
        }

        return text.replace(" ", "").length();
    }

    public boolean containsKeyword(String text, String keyword) {
        if (text == null || keyword == null) {
            return false;
        }

        return text.toLowerCase().contains(keyword.toLowerCase());
    }

    public Map<String, Integer> countWordFrequency(String text) {
        Map<String, Integer> frequency = new HashMap<>();

        if (text == null || text.isBlank()) {
            return frequency;
        }

        String[] words = text.toLowerCase().split("\\s+");

        for (String word : words) {
            String cleanedWord = word.replaceAll("[^a-zA-Z0-9]", "");
            if (!cleanedWord.isBlank()) {
                frequency.put(cleanedWord, frequency.getOrDefault(cleanedWord, 0) + 1);
            }
        }

        return frequency;
    }

    public String reverseText(String text) {
        if (text == null) {
            return "";
        }

        return new StringBuilder(text).reverse().toString();
    }
}
