package com.univ.lab1;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TextFileReader implements Reader {

    @Override
    public Set<String> readWords(String filePath) {
        Set<String> result = new TreeSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile("\\w[\\w-]+('\\w*)?");
                Matcher m = p.matcher(line);
                while (m.find()) {
                    String word = line.substring(m.start(), m.end());
                    if (word.length() > 30) {
                        word = word.substring(0, 30);
                    }
                    result.add(word);
                }
            }
        } catch (IOException e) {
            log.error("Could not read words from file", e);
            return Collections.emptySet();
        }
        return result;
    }

}
