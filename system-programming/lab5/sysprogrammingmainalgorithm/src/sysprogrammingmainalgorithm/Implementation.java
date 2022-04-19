package sysprogrammingmainalgorithm;

import JavaTeacherLib.MyLang;
import JavaTeacherLib.Node;

import java.util.Iterator;

public class Implementation {

    public static int[] createEpsilonNonterminals(MyLang lang) {
        int[] terminal = new int[lang.getNonTerminals().length];
        int count = 0;
        Iterator iter = lang.getLanguarge().iterator();

        Node tmp;
        while(iter.hasNext()) {
            tmp = (Node)iter.next();
            tmp.setTeg(0);
        }

        boolean upr;
        int ii;
        upper:
        do {
            upr = false;
            iter = lang.getLanguarge().iterator();
            while(true) {
                int[] role;
                do {
                    if (!iter.hasNext()) {
                        continue upper;
                    }
                    role = ((Node)iter.next()).getRoole();

                    for(ii = 1; ii < role.length && role[ii] <= 0; ++ii) {
                        int ii1;
                        for(ii1 = 0; ii1 < count && terminal[ii1] != role[ii]; ++ii1) { }
                        if (ii1 == count) break;
                    }
                } while(ii != role.length);

                int ii2;
                for(ii2 = 0; ii2 < count && terminal[ii2] != role[0]; ++ii2) { }
                if (ii2 == count) {
                    terminal[count++] = role[0];
                    upr = true;
                }
            }
        } while(upr);

        int[] rezult = new int[count];

        for(ii = 0; ii < count; ++ii) {
            rezult[ii] = terminal[ii];
        }

        return rezult;
    }


}
