/**
 * INF 421 DM2 self-test framework. This file is provided for your benefit only.
 * Auxiliary tests for Part 1.
 * It is supposed to help you test your code at home. If you don't find it helpful,
 * don't use it.
 * 
 * Please report bugs/issues to <adrian.kosowski@inria.fr>. Version: January 4, 2019 (revised: 19H00)
 */
 
 
import java.util.*;

class Test1 {
    
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

    public static boolean preValidateSequence(List<String> es, String s1, String s2)
    {
        StringBuilder s = new StringBuilder(s1);
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
                        break;
                    case 'd':
                        s.deleteCharAt(pos);
                        break;
                    case 'i':
                        s.insert(pos, c);
                        break;
                }
            }
            catch (Exception e){
                System.err.println("Parse exception: "+e.getMessage());
                System.err.println("Offending operation: " + op);                
                System.err.println("Current string: " + s);    
                return false;
            }
        }
        if (! s2.equals(s.toString())){
            System.err.println("The final string reached is not the intended one.");
            System.err.println("Objective: "+s2);
            System.err.println("Reached:   "+s);
            return false;
        }
        System.out.println("Edit Sequence OK. Note that the cost of the Edit Sequence is not being validated (!)");
        return true;
    }

    public static void test1(String s1,String s2) {
        EditDistance ed = new EditDistance(3,2,1);
        int[][] d = ed.getEditDistanceDP(s1,s2);
        System.out.println("Cost = "+d[s1.length()][s2.length()]);

        ed = new EditDistance(3,2,6);
        d = ed.getEditDistanceDP(s1,s2);
        System.out.println("Cost = "+d[s1.length()][s2.length()]);

        ed = new EditDistance(100,1,1);
        d = ed.getEditDistanceDP(s1,s2);
        System.out.println("Cost = "+d[s1.length()][s2.length()]);

        ed = new EditDistance(1,100,1);
        d = ed.getEditDistanceDP(s1,s2);
        System.out.println("Cost = "+d[s1.length()][s2.length()]);

        ed = new EditDistance(1,1,100);
        d = ed.getEditDistanceDP(s1,s2);
        System.out.println("Cost = "+d[s1.length()][s2.length()]);
}

	
    public static void main(String[] args) {
        test1("abcd","adcb");
        String s1 = getRandomString(1000, "ab");
        test1(getRandomString(1000),getRandomString(1000));
        test1(getRandomString(10000),getRandomString(100));
        test1(getRandomString(100),getRandomString(10000));
        test1(s1,randomModifyString(s1, 10, "ab"));
    }
}
