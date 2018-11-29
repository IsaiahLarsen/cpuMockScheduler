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
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String data;
        int[] start = new int[]{0, 1, 2, 3, 4};
        int[] run = new int[]{2, 3, 1, 5, 4};
        fcfs(start,run);
        sjf(start, run);
        strf(start, run);
//
//        while((data = in.readLine()) != null){
//            data.split(" ");
//            start.add(Integer.valueOf(data));
//        }
//        System.out.println(start);

    }

    /**
     * Round Robin
     * @param s start times array
     * @param r run times array
     */
    private static void roundRobin(int[] s, int[] r){

    }

    /**
     *Shortest run time first
     * @param s start times array
     * @param r run time array
     */
    private static void strf(int[] s, int[] r){
        int clock = 0;
        int ta_total = 0;
        int wait_total = 0;
        int resp_total = 0;
        int currentLowRunTime = r[0];
        int totalRunTime = 0;
        int numProcess = s.length;

        int[] strfStart = new int[s.length];
        int[] strfRun = new int[r.length];
        for(int i = 0; i < s.length; i++){
            strfStart[i] = s[i];
        }
        for(int i = 0; i < r.length; i++){
            strfRun[i] = r[i];
        }

        //add run times to get a total max on runtime
        for(int i = 0; i < r.length; i++){
            totalRunTime += r[i];
        }

        //index for keeping track of lowest remaining time
        int index = 0;
        int runNext = 0;
        int[] firstRun = new int[strfRun.length];
        currentLowRunTime = strfRun[index];
        firstRun[0] = strfStart[0];
        clock = strfStart[index + 1];
        strfRun[index] -= strfStart[index + 1];
        Map<Integer,Integer> queue = new HashMap<>();
        while(clock < totalRunTime){
            //run till next arrival
            if(index < strfStart.length - 1) {
                if (!queue.containsKey(index)) {
                    queue.put(index, strfRun[index]);
                }
                if (!queue.containsKey(index + 1)) {
                    queue.put(index + 1, strfRun[index + 1]);
                }
            }

            // check which is lower from queue
            for(Map.Entry<Integer, Integer> entry : queue.entrySet()){
                int val = entry.getValue();
                int key = entry.getKey();
                if(val < currentLowRunTime && val != 0){
                    currentLowRunTime = strfRun[key];
                    runNext = key;
                }


            }
            if(index < strfStart.length - 1) {
                index++;
                if(index >= strfStart.length - 1){
                    clock += 1;
                }else {
                    clock += (strfStart[index + 1] - strfStart[index]);
                }
            }else{
                clock += 1;
            }
            if((runNext) >= strfStart.length - 1){
                strfRun[runNext] -= 1;
            }else{
                if(r[runNext] == strfRun[runNext]) {
                    firstRun[runNext] = clock - 1;
                }
                strfRun[runNext] -= (strfStart[runNext + 1] - strfStart[runNext]);
            }
            //update map
            queue.replace(runNext, strfRun[runNext]);
            //reset lowest run time
            if(strfRun[runNext] == 0){
                currentLowRunTime = totalRunTime;
                ta_total += (clock - strfStart[runNext]);
                wait_total += ((clock - strfStart[runNext])) - r[runNext];
                resp_total += firstRun[runNext] - strfStart[runNext];
            }


        }
        System.out.println("Shortest remaining Time:");
        printAverages(ta_total, wait_total, resp_total, numProcess);
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
        clock = sortedStart[0];
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

    /*
     * Sorts the run times to run shortest first but keeping the start and run times together
     * basically anything done to runtime array is done to start array
     * @params start time array and runtime array
     */
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
