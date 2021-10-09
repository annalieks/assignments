package com.univ.lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProcessorApplication {

    public static void main(String[] args) {
        Reader reader = new TextFileReader();
        Processor processor = new PairsProcessor();
        Set<String> words = reader.readWords(args[0]);
        List<String> sorted = new ArrayList<>(words);
        sorted.sort((o1, o2) -> Integer.compare(o2.length(), o1.length()));
        System.out.println(processor.process(sorted));
    }

}
