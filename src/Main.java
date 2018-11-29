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
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String data;
        int[] start = new int[]{5,11,238,254,330,637,1042,1163,1364,1404,1737,1885,2149,2230,2273,2327,2441,2498,2875,2954};
        int[] run = new int[]{100,20,80,20,140,220,360,120,170,170,180,40,190,330,360,200,190,110,250,200};
        fcfs(start,run);
        sjf(start, run);
        strf(start, run);
        roundRobin(start, run);
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
        int q = 100;
        int clock = 0;
        int next = 0;
        int temp = 0;
        int ta_total = 0;
        int wait_total = 0;
        int resp_total = 0;
        int qtotal = 100;
        int[] rrStart = new int[s.length];
        int[] rrRun = new int[r.length];
        int[] fr = new int[r.length];
        Queue<Integer> theQueue = new LinkedList<>();
        for(int i = 0; i < s.length; i++){
            rrStart[i] = s[i];
        }
        for(int i = 0; i < r.length; i++){
            rrRun[i] = r[i];
        }
        for(int i = 0; i < fr.length; i++){
            fr[i] = -1;
        }
        fr[0] = clock;
        clock += q;
        for(int i = 0; i < rrStart.length; i++){
            if(rrStart[i] <= clock){
                theQueue.add(i);
            }
        }
        next = theQueue.peek();
        if(fr[next] < 0){
            fr[next] = clock;
        }
        rrRun[next] -= q;
        if(rrRun[next] == 0){
            ta_total += Math.abs(clock - rrStart[next]);
            wait_total += Math.abs((clock - rrStart[next]) - r[next]);
            resp_total += Math.abs(fr[next] - rrStart[next]);
            theQueue.remove();
        }else {
            temp = next;
            theQueue.remove();
            theQueue.add(temp);
        }

        while(!theQueue.isEmpty()){

            clock += q;

            for(int i = 0; i < rrStart.length; i++){
                if(rrStart[i] <= clock && rrStart[i] > qtotal){
                    theQueue.add(i);
                    qtotal = i;
                }
            }
            next = theQueue.peek();
            if(fr[next] < 0){
                fr[next] = clock - q;
            }
            rrRun[next] -= q;
            if(rrRun[next] <= 0){
                ta_total += Math.abs(clock - rrStart[next]);
                wait_total += Math.abs((clock - rrStart[next]) - r[next]);
                resp_total += Math.abs(fr[next] - rrStart[next]);
                theQueue.remove();
            }else {
                temp = next;
                theQueue.remove();
                theQueue.add(temp);
            }
        }
        System.out.println("Round Robin:");
        printAverages(ta_total, wait_total, resp_total, s.length);
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
                if(r[runNext] == strfRun[runNext]) {
                    firstRun[runNext] = clock - 1;
                }
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
            if(strfRun[runNext] <= 0){
                currentLowRunTime = totalRunTime;
                ta_total += Math.abs((clock - strfStart[runNext]));
                wait_total += Math.abs((clock - strfStart[runNext]) - r[runNext]);
                resp_total += Math.abs(firstRun[runNext] - strfStart[runNext]);
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
        int totalTime = 0;
        int numProcess = s.length;
        int count = numProcess;
        int[] sortedStart = new int[s.length];
        int[] sortedRun = new int[r.length];
        for(int i = 0; i < s.length; i++){
            sortedStart[i] = s[i];
        }
        for(int i = 0; i < r.length; i++){
            sortedRun[i] = r[i];
        }
        for(int i = 0; i < r.length; i++){
            totalTime += r[i];
        }

        ArrayList<Integer> runnable = new ArrayList<>();
        clock = sortedStart[0];
        int runNext = 0;
        int removeThis = 0;
        int currentLow = sortedRun[0];

        while(count != 0) {
            for (int i = 0; i < sortedStart.length; i++) {
                if (sortedStart[i] <= clock && sortedRun[i] != -1) {
                    if(runnable.contains(i)){
                        //dont add
                    }else {
                        runnable.add(i);
                    }
                }
            }
            for (int i = 0; i < runnable.size(); i++) {
                if (sortedRun[runnable.get(i)] <= currentLow) {
                    currentLow = sortedRun[runnable.get(i)];
                    runNext = runnable.get(i);
                    removeThis = i;
                }
            }
            clock += sortedRun[runNext];
            runnable.remove(removeThis);
            currentLow = totalTime;
            count--;
            ta = Math.abs(clock - sortedStart[runNext]);
            wait = Math.abs(ta - sortedRun[runNext]);
            resp = Math.abs((clock -  sortedRun[runNext]) - sortedStart[runNext]);
            sortedRun[runNext] = -1;
            ta_total += ta;
            wait_total += wait;
            resp_total += resp;

            if(runnable.isEmpty()){
                if(clock < sortedStart[runNext + 1]) {
                    clock = sortedStart[runNext + 1];
                }
            }

        }
//
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
            ta = Math.abs((clock + r[i]) - s[i]);
            wait = Math.abs(ta - r[i]);
            resp = Math.abs(clock - s[i]);
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
        System.out.printf("Avg. Resp.: %.2f,  Avg. T.A.: %.2f,  Avg. Wait: %.2f\n",(float)r/n, (float)t/n, (float)(w/n) );
    }
}
