package sysprogrammingmainalgorithm;

import JavaTeacherLib.MyLang;

import java.util.List;

public class ImplementationTest {

    private static final List<String> FILES = List.of(
            "../Examples/Pascal/PASCAL_GR.TXT",
            "../Examples/C/C0_GR.txt",
            "../Examples/PL0/PL0_GR.TXT"
    );

    public static void testEpsilonNonTerminals() {
        for (String file : FILES) {
            MyLang lang = new MyLang(file, 2);
            int[] actualNonTerminals = Implementation.createEpsilonNonterminals(lang);
            int[] expectedNonTerminals = lang.createEpsilonNonterminals();
            assert expectedNonTerminals.length == expectedNonTerminals.length;
            for (int j = 0; j < actualNonTerminals.length; j++) {
                assert actualNonTerminals[j] == expectedNonTerminals[j];
            }

            System.out.println("-------------------- Expected:");
            lang.setEpsilonNonterminals(expectedNonTerminals);
            lang.printEpsilonNonterminals();
            // Set custom non terminals
            lang.setEpsilonNonterminals(expectedNonTerminals);
            System.out.println("-------------------- Actual:");
            lang.printEpsilonNonterminals();
            System.out.println("\n\n");
        }
    }

    public static void main(String[] args) {
        testEpsilonNonTerminals();
    }

}
