import java.io.*;
import java.util.*;


public class Knapsack {

    public static void main(String[] args) {

        HashMap<Integer, Integer> fileValues = new HashMap<>();//the values of the file will be stored in this HashMap
        HashMap<Integer, Integer> amountOfBoxes = new HashMap<>();
        int capacity = 0; //the amount of weight that the ship can handle
        int amountOfDifferent = 0; //the amount of the different kinds of boxes

        //-----------------------------READ TXT FILE AND STORE VALUES INTO HASHMAP-----------------------------

        try{

            File file = new File(args[0]);
            Scanner scanner = new Scanner(file); //file has ansi encoding

            capacity = scanner.nextInt(); //reads the first line (capacity)
            amountOfDifferent = scanner.nextInt(); //reads the amount of the different kinds of boxes

            while(scanner.hasNextInt()){
                int key = scanner.nextInt(); //the type of each box
                int value = scanner.nextInt(); //the weight of the box
                int amount = scanner.nextInt(); // reads the amount of boxes of each type

                fileValues.put(key, value); //stores them into the HashMap
                amountOfBoxes.put(key, amount); //stores them into the HashMap
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*--------------------------FOR TESTING--------------------------
          System.out.println(Collections.singletonList(fileValues));
          System.out.println(Collections.singletonList(amountOfBoxes));
        -----------------------------------------------------------------*/


        minInfinityAmount(capacity, amountOfDifferent, fileValues);
        minAmountBoxes(fileValues, amountOfBoxes, capacity, amountOfDifferent);
    }


    /**
     * This method finds the minimum amount of boxes needed in the unbounded case (infinite amount of each box).
     *
     * @param capacity --> the amount of weight that the ship can handle
     * @param amountOfDifferent --> the amount of the different kinds of boxes
     * @param boxes --> HahMap that includes the type of boxes as key and the weight of the box as value
     */
    private static void minInfinityAmount(int capacity, int amountOfDifferent, HashMap<Integer, Integer> boxes){
        HashMap<Integer, Integer> result = new HashMap<>(); //has the result in the end
        result.put(0, 0); //base case, if capacity = 0


        for(int i = 1; i <= capacity; i++){
            result.put(i, Integer.MAX_VALUE); //set result's values to infinite

            for(int j = 1; j <= amountOfDifferent; j++){
                //for all the values smaller than i, find the minimum amount of boxes
                if(i >= boxes.get(j)){
                    //finds the value that exists in the index of difference of i and the value of "boxes" in index j
                    int remaining = result.get(i - boxes.get(j));
                    if((Integer.MAX_VALUE != remaining) && (remaining + 1 < result.get(i))){
                        result.put(i, remaining + 1); // stores the remaining+1 into the result HashMap
                    }
                }
            }
        }

        //checks if the last value of the result HashMap is infinite, if yes -> there is no such combination
        if(result.get(capacity) == Integer.MAX_VALUE){
            System.out.println("There is no solution for the unbounded case.");
            return;
        }

        printResult(result.get(capacity), 1);
    }


    /**
     * This method finds the minimum amount of boxes needed in the bounded case (specific amount of each box).
     *
     * @param boxes --> HahMap that includes the type of boxes as key and the weight of the box as value
     * @param amountOfBoxes --> HahMap that includes the available amount of each kind of box
     * @param capacity --> the amount of weight that the ship can handle
     * @param amountOfDifferent --> the amount of the different kinds of boxes
     */
    private static void minAmountBoxes(HashMap<Integer, Integer> boxes, HashMap<Integer, Integer> amountOfBoxes, int capacity, int amountOfDifferent) {
        ArrayList<Integer> result = new ArrayList<>(); //arraylist to store the minimum amount of boxes needed
        result.add(0); //base case, if capacity = 0

        int[][] valuesUsed = new int[capacity + 1][amountOfDifferent+ 1]; //stores the number of boxes that have been used

        //for value 0 we don't need to use any boxes
        for(int i = 1; i < amountOfDifferent; i++){
            valuesUsed[0][i] = amountOfBoxes.get(i);
        }

        //finds the minimum number of boxes required for each value from 1 to capacity
        for (int i = 1; i <= capacity; i++){
            result.add(i, Integer.MAX_VALUE); //set result's values to infinite

            for(int j = 1; j < amountOfDifferent; j++){
                //if i is greater than the value of box and there is an i that can be computed adding one boxes.get(j)
                if ((i >= boxes.get(j))){

                    if (result.get(i - boxes.get(j)) + 1 < result.get(i) && (result.get(i - boxes.get(j)) < Integer.MAX_VALUE) && (valuesUsed[i - boxes.get(j)][j] > 0)) {
                        result.set(i, 1 + result.get(i - boxes.get(j))); //adds the minimum number of boxes required into the result ArrayList
                        update(valuesUsed,i - boxes.get(j), i, amountOfDifferent, j);//updates the valuesUsed table
                    }
                }else if (i < boxes.get(j)) {
                    valuesUsed[i][j] = valuesUsed[0][j]; //stores the availability of the boxes.get(j) in the valuesUsed
                }
            }
        }

        //checks if the last value of the result ArrayList is infinite, if yes -> there is no such combination
        if(result.get(capacity)==Integer.MAX_VALUE){
            System.out.println("There is no solution for the bounded case.");
            return;
        }

        //calls the printResult function.
        printResult(result.get(capacity), 2);
    }


    /**
     * This method is used to update the values of the given array.
     * @param valuesUsed --> array to update
     * @param value
     * @param position
     * @param limit
     * @param key
     */
    private static void update(int [][] valuesUsed, int value, int position, int limit, int key){
        for(int i= 0; i < limit; i++){
            if(i == key){
                valuesUsed[position][i] = valuesUsed[value][i] - 1;
            }else{
                valuesUsed[position][i] = valuesUsed[value][i];
            }
        }
    }


    /**
     * This method is used to print the results for both cases, depending on the value of flag.
     * If flag = 1 --> unbounded case
     * If flag != 1 --> bounded case
     *
     * @param boxes --> the amount of boxes needed
     * @param flag --> in order to print the right message
     */
    private static void printResult(int boxes, int flag) {
        if (flag == 1) {
            System.out.println("The minimum number of boxes for the unbounded case is: " + boxes);
        } else {
            System.out.println("The minimum number of boxes for the bounded case is: " + boxes);
        }
    }

}
