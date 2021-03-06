/* Promise of Originality
I promise that this source code file has, in its entirety, been
written by myself and by no other person or persons. If at any time an
exact copy of this source code is found to be used by another person in
this term, I understand that both myself and the student that submitted
the copy will receive a zero on this assignment.
*/

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String data;
        String[] splitNums = new String[0];

        int[] startUp = new int[100];
        int[] runUp = new int[100];
        int indexStart = 0;

        if(args.length > 0) {
            //read from file
            File file = new File(args[0]);
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((data = br.readLine()) != null){
                splitNums = data.split(" ");
                //make integer arrays
                //fill will appropriate data
                indexStart = makeArray(splitNums, startUp, runUp, indexStart);

            }
        }else{
            //read from stdin
            while((data = in.readLine()) != null){
                splitNums = data.split(" ");
                indexStart = makeArray(splitNums, startUp, runUp, indexStart);
            }

        }
        int[] start = new int[indexStart];
        int[] run = new int[indexStart];
        for(int i = 0; i < start.length; i++){
            start[i] = startUp[i];
            run[i] = runUp[i];
        }

        fcfs(start,run);
        sjf(start, run);
        strf(start, run);
        roundRobin(start, run);


    }

    /**
     * Method to convert read in string data to int and place in correct array
     * @param arr string data read in
     * @param s start array
     * @param r run array
     * @param index index
     * @return index
     */
    public static int makeArray(String[] arr, int[] s, int[] r, int index){
        for(int i = 0; i < arr.length; i += 2){
            s[index] = Integer.parseInt(arr[i]);
            r[index] = Integer.parseInt(arr[i + 1]);
            index++;

        }
        return index;
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
        int ta_total = 0;
        int wait_total = 0;
        int resp_total = 0;
        int totalRunTime = 0;
        boolean keepRunning;
        int[] rrStart = new int[s.length];
        int[] rrRun = new int[r.length];
        int[] fr = new int[r.length];

        Queue<Integer> theQueue = new LinkedList<>();
        for(int i = 0; i < s.length; i++){
            rrStart[i] = s[i];
        }
        for(int i = 0; i < r.length; i++){
            rrRun[i] = r[i];
            totalRunTime += rrRun[i];
        }
        for(int i = 0; i < fr.length; i++){
            fr[i] = -1;
        }

        //start clock
        clock = rrStart[0];
        fr[0] = rrStart[0];

       do{
           //reset boolean
           keepRunning = false;
            // add to the queue
            for(int i = 0; i < rrStart.length; i++){
                if(rrStart[i] <= clock && rrRun[i] > 0 && !theQueue.contains(i)){
                    theQueue.add(i);
                }
            }
            if(theQueue.isEmpty()){
                clock += q;
                //add to queue
                for(int i = 0; i < rrStart.length; i++){
                    if(rrStart[i] <= clock && rrRun[i] > 0 && !theQueue.contains(i)){
                        theQueue.add(i);
                    }
                }
            }
            //first in queue
            next = theQueue.peek();
            //check for first run time
           if(fr[next] < 0){
               fr[next] = clock;
           }
            rrRun[next] -= q;
            clock += q;
            //check if it still has time to run
            if(rrRun[next] > 0){
                //take off top
                theQueue.remove();
                //put on bottom
                theQueue.add(next);
            }
            if(rrRun[next] <= 0){
                ta_total += Math.abs(clock - rrStart[next]);
                wait_total += Math.abs((clock - rrStart[next]) - r[next]);
                resp_total += Math.abs(fr[next] - rrStart[next]);
                theQueue.remove();
            }
            //check if there is more to run
           for(int i = 0; i < rrRun.length; i++){
               if(rrRun[i] > 0){
                   keepRunning = true;
               }
           }
        }while(keepRunning);
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
        currentLowRunTime = totalRunTime;
        firstRun[0] = strfStart[0];
        Map<Integer,Integer> queue = new HashMap<>();
        clock = strfStart[0];
        while(clock < totalRunTime){
            //put runnable in the queue if not already in there
            for(int i = 0; i < strfStart.length; i++){
                if(strfStart[i] <= clock && !queue.containsKey(i)){
                    //add to queue
                    queue.put(i, strfRun[i]);
                }
            }
            if(strfStart[index] > clock && queue.get(index - 1) == 0){
                clock = strfStart[index];
                queue.put(index, strfRun[index]);
            }

            // check which is lower from queue
            for(Map.Entry<Integer, Integer> entry : queue.entrySet()){
                int val = entry.getValue();
                int key = entry.getKey();
                if(val < currentLowRunTime && val > 0){
                    currentLowRunTime = strfRun[key];
                    runNext = key;
                }
            }
            //run lower till next clock
            //increment clock
            firstRun[runNext] = clock;
            clock += strfRun[runNext];
            strfRun[runNext] = 0;
            //update map
            queue.replace(runNext, 0);
            currentLowRunTime = totalRunTime;
            ta_total += Math.abs((clock - strfStart[runNext]));
            wait_total += Math.abs((clock - strfStart[runNext]) - r[runNext]);
            resp_total += Math.abs(firstRun[runNext] - strfStart[runNext]);
            index++;
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

            if(runnable.isEmpty() && (runNext + 1) < sortedStart.length){
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
        int clock = s[0];
        int ta = 0;
        int wait = 0;
        int resp = 0;
        int ta_total = 0;
        int wait_total = 0;
        int resp_total = 0;
        int firstRun = s[0];
        int numProcess = s.length;
        int[] fcStart = new int[s.length];
        int[] fcRun = new int[r.length];
        for(int i = 0; i < s.length; i++){
            fcStart[i] = s[i];
        }
        for(int i = 0; i < r.length; i++){
            fcRun[i] = r[i];
        }
        //start clock
        clock = s[0];
        for(int i = 0; i < s.length; i++){
            if(fcStart[i] > clock){
                clock = fcStart[i];
            }
            firstRun = clock;
            clock += fcRun[i];
            fcRun[i] -= fcRun[i];

            ta = Math.abs((clock) - s[i]);
            wait = Math.abs(ta - r[i]);
            resp = Math.abs(firstRun - s[i]);
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
        System.out.printf("Avg. Resp.: %.2f,  Avg. T.A.: %.2f,  Avg. Wait: %.2f\n",(float)r/n, (float)t/n, (float)w/n );
    }
}
