/**
 * INF 421 DM2 self-test framework. This file is provided for your benefit only.
 * Auxiliary tests for Part 2 - advanced version.
 * It is supposed to help you test your code at home. If you don't find it helpful,
 * don't use it.
 * 
 * Please report bugs/issues to <adrian.kosowski@inria.fr>. Version: January 4, 2019 (revised: 19H00)
 */

import java.util.*;

class Test3 {
    
    static Random rGen = new Random(0);
    
    public static String getRandomString(int length) {
        return getRandomString (length, "abcdefghijklmnopqrstuvwxyz");
    }
    
    public static String randomModifyString(String input, int mods, final String alphabet) {
        StringBuilder s = new StringBuilder(input);
        while (mods-- > 0) {
            char c = alphabet.charAt(rGen.nextInt(alphabet.length()));
            int pos = rGen.nextInt(s.length());
            switch (rGen.nextInt(3))
            {
                case 0:
                    s.setCharAt(pos, c);
                    break;                           
                case 1:
                    s.deleteCharAt(pos);
                    break;
                case 2:
                    s.insert(pos, c);
            }
        }
        return s.toString();
    }


    public static String getRandomString(int length, final String alphabet) {
       StringBuilder result = new StringBuilder();
       while(length > 0) {
           result.append(alphabet.charAt(rGen.nextInt(alphabet.length())));
           length--;
       }
       return result.toString();
    }

    public static long preValidateSequence(List<String> es, String s1, String s2, int c_i, int c_d, int c_r)
    {
        StringBuilder s = new StringBuilder(s1);
        long cost = 0;
        for (String op : es) {

            try
            {
                String [] args = op.split("[\\(\\)\\,]", 20);
                char command = 0;
                if(args[0].startsWith("replace"))
                    command = 'r';
                else if(args[0].startsWith("delete"))
                    command = 'd';
                else if(args[0].startsWith("insert"))
                    command = 'i';
                else throw new Exception ("Invalid command");
                if (!((args.length == 3 && command == 'd') ||
                        (args.length == 4 && (command == 'r' || command == 'i'))))
                    throw new Exception ("Invalid syntax or unsupported character {, ( )} in text string");
                int pos = 0;
                pos = Integer.parseInt(args[1]);
                if (pos < 0 || pos > s.length() || (pos >= s.length() && (command=='r' || command =='d')))
                    throw new Exception("Position outside of string range");
                char c = 0;
                if (command == 'r' || command == 'i'){
                    if (args[2].length() != 1)
                        throw new Exception ("Invalid replacement character");
                    c = args[2].charAt(0);
                }
                switch (command) {
                    case 'r':
                        s.setCharAt(pos, c);
                        cost += c_r;
                        break;
                    case 'd':
                        s.deleteCharAt(pos);
                        cost += c_d;
                        break;
                    case 'i':
                        s.insert(pos, c);
                        cost += c_i;
                        break;
                }
            }
            catch (Exception e){
                System.err.println("Parse exception: "+e.getMessage());
                System.err.println("Offending operation: " + op);                
                System.err.println("Current string: " + s);    
                return -1;
            }
        }
        if (! s2.equals(s.toString())){
            System.err.println("The final string reached is not the intended one.");
            System.err.println("Objective: "+s2);
            System.err.println("Reached:   "+s);
            return -1;
        }
        System.out.println("Edit Sequence OK. Note that the cost of the Edit Sequence is not being validated (!)");
        return cost;
    }


    public static void test3(String s1,String s2) {
        EditDistance ed = new EditDistance(3,2,1);
        List<String> m = ed.getMinimalEditSequence(s1,s2);
        System.out.println(preValidateSequence (m, s1, s2, 3, 2, 1));
        
        ed = new EditDistance(3,2,6);
        ed.getMinimalEditSequence(s1,s2);
        System.out.println(preValidateSequence (m, s1, s2, 3, 2, 6));
  
        ed = new EditDistance(100,1,1);
        ed.getMinimalEditSequence(s1,s2);
        System.out.println(preValidateSequence (m, s1, s2, 100, 1, 1));

        ed = new EditDistance(1,100,1);
        ed.getMinimalEditSequence(s1,s2);
        System.out.println(preValidateSequence (m, s1, s2, 1, 100, 1));
        
        ed = new EditDistance(1,1,100);
        ed.getMinimalEditSequence(s1,s2);
        System.out.println(preValidateSequence (m, s1, s2, 1, 1, 100));
    }

    public static void main(String[] args) {
        test3("abcd","adcb");
        String s1 = getRandomString(1000000, "ab");
        test3(s1,randomModifyString(s1, 10, "ab"));
        s1 = getRandomString(10000);
        test3(s1,randomModifyString(s1, 1000, "a"));
        s1 = getRandomString(100000, "a");
        test3(s1,randomModifyString(s1, 100, "ab"));
        s1 = getRandomString(100000, "a");
        test3(s1,randomModifyString(s1, 100, "a"));
    }
}
