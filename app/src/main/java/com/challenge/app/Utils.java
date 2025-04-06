package com.challenge.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {
    public static ArrayList<String> tokenizer(String inputString) {
        ArrayList<String> tokens = new ArrayList<String>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < inputString.length(); ++i) {
            char character = inputString.charAt(i);
            if (Character.isWhitespace(character)) {
                if (!word.isEmpty()) {
                    tokens.add(word.toString());
                    word.setLength(0);
                }
            } else {
                if (character == '"') {
                    word.setLength(0);
                    i += 1;
                    while (i < inputString.length() && inputString.charAt(i) != '"') {
                        word.append(inputString.charAt(i));
                        i += 1;
                    }
                } else {
                    word.append(character);
                }
            }
        }
        if (!word.isEmpty()) // add any remaining word
            tokens.add(word.toString());

        return tokens;
    }

    public static String processSet(ArrayList<String> words, ConcurrentHashMap<String, Token> serverDictionary) {
        if (words.size() < 3) {
            return "LESS NUMBER OF INPUT PROVIDED";
        }

        String key = words.get(1);
        String value = words.get(2);
        boolean nx = false, xx = false;
        Long expiryTime = null;
        if (serverDictionary.containsKey(key)) {
            Token token = serverDictionary.get(key);
            if (token.getExpiryTime() != null && System.currentTimeMillis() > token.getExpiryTime()) {
                serverDictionary.remove(key);
            }
        }
        for (int i = 3; i < words.size(); ++i) {
            switch (words.get(i).toUpperCase()) {
                case "EX":
                    expiryTime = System.currentTimeMillis() + (Long.parseLong(words.get(++i)) * 1000);
                    break;
                case "PX":
                    expiryTime = System.currentTimeMillis() + Long.parseLong(words.get(++i));
                    break;
                case "EXAT":
                    expiryTime = Long.parseLong(words.get(++i)) * 1000;
                    break;
                case "PXAT":
                    expiryTime = Long.parseLong(words.get(++i));
                    break;
                case "NX":
                    nx = true;
                    break;
                case "XX":
                    xx = true;
                    break;
                default:
                    throw new IllegalArgumentException("INVALID COMMAND");
            }
        }
        Token token = new Token();
        token.setContent(value);
        if (expiryTime != null)
            token.setExpiryTime(expiryTime);
        if (nx && serverDictionary.containsKey(key)) {
            return "CANNOT SET KEY AS IT ALREADY EXISTS";
        }
        if (xx && !serverDictionary.containsKey(key)) {
            return "CANNOT SET KEY AS IT DOES NOT EXIST";
        }
        serverDictionary.put(key, token);
        return "SUCCESSFULLY WROTE KEY";
    }

    public static String processGet(ArrayList<String> words, ConcurrentHashMap<String, Token> serverDictionary) {
        if (words.size() < 2) {
            return "INVALID FORMAT";
        }

        String key = words.get(1);
        Token token = serverDictionary.get(key);

        if (token == null) {
            return "KEY NOT FOUND";
        }

        Long expiryTime = token.getExpiryTime();
        if (expiryTime != null && System.currentTimeMillis() > expiryTime) {
            serverDictionary.remove(key);
            return "KEY EXPIRED";
        }

        return token.getContent();
    }

    public static String processExists(ArrayList<String> words, ConcurrentHashMap<String, Token> serverDictionary) {
        if (words.size() < 2) {
            return "INVALID FORMAT";
        }

        for (int i = 1; i < words.size(); ++i) {
            String key = words.get(i);
            Token token = serverDictionary.get(key);

            if (token == null) {
                return key + "KEY NOT FOUND";
            }

            Long expiryTime = token.getExpiryTime();
            if (expiryTime != null && System.currentTimeMillis() > expiryTime) {
                serverDictionary.remove(key);
                return key + "KEY NOT FOUND";
            }
        }

        return "ALL KEY FOUND";
    }

    public static String processDelete(ArrayList<String> words, ConcurrentHashMap<String, Token> serverDictionary) {
        if (words.size() < 2) {
            return "INVALID FORMAT";
        }

        for (int i = 1; i < words.size(); ++i) {
            String key = words.get(i);
            serverDictionary.remove(key);
        }

        return "SUCCESSFULLY DELETED ALL KEYS";
    }

    public static String processSave(ArrayList<String> words, ConcurrentHashMap<String, Token> serverDictionary) {
        if (words.size() < 2) {
            return "LESS NUMBER OF ARGUMENTS";
        }
        String filename = words.get(1);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(serverDictionary);
            return "DICTIONARY SAVED SUCCESSFULLY";
        } catch (IOException e) {
            return e.toString();
        }
    }

    public static String processLoad(ArrayList<String> words, ConcurrentHashMap<String, Token> serverDictionary) {
        if (words.size() < 2) {
            return "LESS NUMBER OF ARGUMENTS";
        }
        String filename = words.get(1);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            ConcurrentHashMap<String, Token> loadedDictionary = (ConcurrentHashMap<String, Token>) ois.readObject();
            serverDictionary.clear();
            serverDictionary.putAll(loadedDictionary);
            return "DICTIONARY LOADED SUCCESSFULLY";
        } catch (Exception e) {
            return e.toString();
        }
    }
}
