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
        sjf(start, run);
//        ArrayList<Integer> start = new ArrayList<>();
//
//        while((data = in.readLine()) != null){
//            data.split(" ");
//            start.add(Integer.valueOf(data));
//        }
//        System.out.println(start);

    }

    /*
    * Shortest Job first
    * @params array of ints for start times and array of ints for run times
    * */
    private static void sjf(int[] s, int[] r){
        int clock = 0;
        int ta = 0;
        int wait = 0;
        int resp = 0;
        int ta_total = 0;
        int wait_total = 0;
        int resp_total = 0;
        int numProcess = s.length;
        int[] sortedStart = new int[s.length];
        int[] sortedRun = new int[r.length];
        for(int i = 0; i < s.length; i++){
            sortedStart[i] = s[i];
        }
        for(int i = 0; i < r.length; i++){
            sortedRun[i] = r[i];
        }
        order(sortedStart,sortedRun);
        for(int i = 0; i < sortedStart.length; i++){
            ta = (clock + sortedRun[i]) - sortedStart[i];
            wait = ta - sortedRun[i];
            resp = clock - sortedStart[i];
            clock += sortedRun[i];
            ta_total += ta;
            wait_total += wait;
            resp_total += resp;

        }
        System.out.println("Shortest Job First");
        printAverages(ta_total, wait_total, resp_total, numProcess);

    }
    private static void order(int[] s, int[] r){
        int temp;
        int temp2;
        for (int i = 1; i < r.length; i++) {
            for (int j = i; j > 0; j--) {
                if (r[j] < r [j - 1]) {
                    temp = r[j];
                    temp2 = s[j];
                    r[j] = r[j - 1];
                    s[j] = s[j - 1];
                    r[j - 1] = temp;
                    s[j - 1] = temp2;
                }
            }
        }
    }

    /*
    * First come first serve algorithm
    * @params array of ints for start times and array of ints for run times
    * */
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
        System.out.println("First Come First Serve:");
        printAverages(ta_total, wait_total, resp_total, numProcess);
    }

    /*
    * Method to print out average results of Turn around, wait, and response time
    * @params total turn around time, wait, and response times, and total number of processes
    * */
    private static void printAverages(int t, int w, int r, int n){
        System.out.println("Average TA: " + t/n + " Average Wait: " + w/n + " Average Response: " + r/n);
    }
}
