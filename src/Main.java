/* Promise of Originality
I promise that this source code file has, in its entirety, been
written by myself and by no other person or persons. If at any time an
exact copy of this source code is found to be used by another person in
this term, I understand that both myself and the student that submitted
the copy will receive a zero on this assignment.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String data;
        int[] start = new int[]{0, 0, 0};
        int[] run = new int[]{24,3,3};
        fcfs(start,run);
//        ArrayList<Integer> start = new ArrayList<>();
//
//        while((data = in.readLine()) != null){
//            data.split(" ");
//            start.add(Integer.valueOf(data));
//        }
//        System.out.println(start);

    }

    private static void fcfs(int[] s, int[] r) {
        int clock = 0;
        int ta = 0;
        int wait = 0;
        int resp = 0;
        int ta_total = 0;
        int wait_total = 0;
        int resp_total = 0;
        int numProcess = s.length;

        for(int i = 0; i < s.length; i++){
            ta = (clock + r[i]) - s[i];
            wait = ta - r[i];
            resp = clock - s[i];
            clock += r[i];
            ta_total += ta;
            wait_total += wait;
            resp_total += resp;

        }
        System.out.println("Average TA: " + ta_total/numProcess + " Average Wait: " + wait_total/numProcess + " Average Response: " + resp_total/numProcess);
    }


}
