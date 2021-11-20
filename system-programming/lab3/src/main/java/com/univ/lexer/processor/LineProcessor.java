package com.univ.lexer.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LineProcessor {

    private LineProcessor() {}

    private static final Map<String, String> TRIGRAPHS = Map.of(
            "??=", "#",
            "??-", "~",
            "??<", "{",
            "??>", "}",
            "??(", "[",
            "??!", "|",
            "??'", "^",
            "??)", "]",
            "??/", "\\"
    );

    private static Stream<String> replaceTrigraphs(Stream<String> lines) {
        return lines.map(s -> {
                    final String[] x = {s};
                    TRIGRAPHS.forEach((trg, val) -> x[0] = x[0].replace(trg, val));
                    return x[0];
                }
        );
    }

    private static Stream<String> splitToLines(Stream<String> lines) {
        List<String> spliced = new ArrayList<>();
        Iterator<String> it = lines.iterator();
        StringBuilder cur = new StringBuilder();
        while (it.hasNext()) {
            String s = it.next();
            if (s.endsWith("\\")) {
                s = s.substring(0, s.length() - 1);
                cur.append(s);
                continue;
            }
            cur.append(s);
            spliced.add(cur.toString());
            cur = new StringBuilder();
        }
        if (cur.length() > 0) {
            System.err.println("Last line ends with a backslash");
            System.exit(3);
        }
        return spliced.stream();
    }

    public static Stream<String> preprocess(Stream<String> lines) {
        return splitToLines(replaceTrigraphs(lines));
    }
}
