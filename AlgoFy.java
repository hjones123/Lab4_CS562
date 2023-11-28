import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
/**
 You are working for a promising new music streaming service “Algo-fy”.
 Algo-fy would like to offer a new ranking feature that will track the top k most streamed songs of the day.

 You’ve been tasked with building a class “AlgoFy” that can track the most streamed songs
 and return an accurate list top k list at any point in time.

 Another team has already developed the song streaming feature: iStream and you will need to integrate with it.
 iStream will send AlgoFy play data as it processes customer requests.
 This data will be batched so the AlgoFy class will need to ingest lists of played songs multiple times

 A song is identified with an integer id number.

 The AlgoFy class will need to have the following methods:
 Constructor(k): the number of songs to track in the topk
 streamSongs(List of songIds): Receive a batch of streamed songs => updates internal state
 getTopK(): return the top k most streamed songs in order

 As an example, the AlgoFy class may see the following usage:
 ranker = new AlgoFy(2)
 ranker.streamSongs([1, 1, 2, 2, 3])
 ranker.streamSongs([3, 3, 3, 1, 2, 1, 3])
 ranker.getTopK() => [3, 1]
 ranker.streamSongs([2, 2, 2, 2, 2])
 ranker.getTopK() => [2, 3]

 You must solve this problem by using either a Priority Queue or Heap.

 You must also provide an explanation of the running time of both getTopK and streamSongs

 Code Author: <Hal Jones>

 Running Time Analysis of getTopK
 --------------------
 <This method returns the top streamed song in the form of a List, one by one. It copies the
 elements from the PriorityQueue, one song at a time. As a result, the running time can be O(n),
 where n is the number of entries. For this specific assignment, it can be written as O(k),
 where k is the number of songs. >

 Running Time Analysis of streamSongs
 --------------------
 <From Module 4, we know that a heap is a complete binary tree, whose height can be written as h =[log(n)],
 where n is the number of entries. Since the height is a logarithmic function of the number of entries, and since the
 streamSongs method updates the heap by calling upon updateQueue method, it can be stated that the running time is O(log(n)).
 The method's performance is directly proportional to the amount of entries in the heap, thus proving the O(log(n)) run time.
 For this specific assignment, the running time can be written as O(log(k)), where k is the number of songs.> 
 */

public class AlgoFy {
    private int k;
    //Map to store the song & number of plays. Key is the song and value is number of plays.  
    private HashMap<Integer, Integer> songCount;
    private PriorityQueue<Integer> topSongQueue;

    public AlgoFy(int k) {
        this.k = k;
        this.songCount = new HashMap<>();
        //Setup PrioirtyQueue that uses a Comparator to setup as a max heap
        this.topSongQueue = new PriorityQueue<>((songId1, songId2) -> songCount.get(songId1) - songCount.get(songId2));
    }
    
    //Each time this method is called, need to rebuild the max Heap. Add the key-value pairs to my heap.
    public void streamSongs(List<Integer> songIds) {
    	for (int songId : songIds) {
    		songCount.put(songId, songCount.getOrDefault(songId, 0) + 1);
    		updateQueue(songId);
    	}
    }
    //rebuilding the queue
    private void updateQueue(int songId) {
        topSongQueue.remove(songId);
        topSongQueue.offer(songId);

        if (topSongQueue.size() > k) {
            topSongQueue.poll();
        }
    }
    
    public List<Integer> getTopK() {
        List<Integer> topStreamedSong = new ArrayList<>(topSongQueue);
        //getting the opposite output than expected, reversing to fix it
        Collections.reverse(topStreamedSong);
    	
    	return topStreamedSong;
    }


    public static void runUnitTests() {
        testExample();
        testExample2();
        testManyBatches();
        testFewerThanK();
        testEmpty();
    }

    private static void printTestResult(String testName, boolean result) {
        String color = result ? "\033[92m" : "\033[91m";
        String reset = "\033[0m";
        System.out.println(color + "[" + result + "] " + testName + reset);
    }

    private static void testAnswer(String testName, List<Integer> result, List<Integer> expected) {
        if (result.equals(expected)) {
            printTestResult(testName, true);
        } else {
            printTestResult(testName, false);
            System.out.println("Expected: " + expected);
            System.out.println("Got:      " + result);
        }
    }

    public static void testExample() {
        AlgoFy ranker = new AlgoFy(2);
        ranker.streamSongs(List.of(1, 1, 2, 2, 3));
        ranker.streamSongs(List.of(3, 3, 3, 1, 2, 1, 3));
        List<Integer> result = ranker.getTopK();
        List<Integer> expectedAnswer = List.of(3, 1);
        testAnswer("testExample", result, expectedAnswer);
    }

    public static void testExample2() {
        AlgoFy ranker = new AlgoFy(2);
        ranker.streamSongs(List.of(1, 1, 2, 2, 3));
        ranker.streamSongs(List.of(3, 3, 3, 1, 2, 1, 3));
        ranker.getTopK();
        ranker.streamSongs(List.of(2, 2, 2, 2, 2));
        List<Integer> result = ranker.getTopK();
        List<Integer> expectedAnswer = List.of(2, 3);
        testAnswer("testExample2", result, expectedAnswer);
    }

    public static void testManyBatches() {
        AlgoFy ranker = new AlgoFy(5);

        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 15; j++) {
                ranker.streamSongs(List.of(i, j));
            }
            ranker.getTopK();
        }
        ranker.streamSongs(List.of(3, 1, 1, 1, 2, 3));
        ranker.streamSongs(List.of(5, 4, 4, 3, 2, 2, 2, 1, 1));
        List<Integer> result = ranker.getTopK();
        List<Integer> expectedAnswer = List.of(1, 2, 3, 4, 5);
        testAnswer("testManyBatches", result, expectedAnswer);
    }

    public static void testFewerThanK() {
        AlgoFy ranker = new AlgoFy(4);

        ranker.streamSongs(List.of(1, 2, 3, 1, 2, 3, 1, 2, 1));
        List<Integer> result = ranker.getTopK();
        List<Integer> expectedAnswer = List.of(1, 2, 3);
        testAnswer("testFewerThanK", result, expectedAnswer);
    }

    public static void testEmpty() {
        AlgoFy ranker = new AlgoFy(3);
        List<Integer> result = ranker.getTopK();
        List<Integer> expectedAnswer = Collections.emptyList();
        testAnswer("testEmpty", result, expectedAnswer);
    }

    public static void main(String[] args) {
        AlgoFy.runUnitTests();
    }
}
