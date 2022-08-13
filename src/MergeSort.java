import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MergeSort{


    public static void main(String[] args) {
        ArrayList<Integer> fileValues = new ArrayList<>(); //the values of the file will be stored in this arraylist

        //-----------------------------READ TXT FILE AND STORE VALUES INTO ARRAYLIST-----------------------------

        try{
            File file = new File(args[0]);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextInt()){
                int numToAdd = scanner.nextInt();
                fileValues.add(numToAdd);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //converting the arraylist to an int array
        int[] inputArray = fileValues.stream().mapToInt(Integer::valueOf).toArray();
        int[] helpArray = new int[inputArray.length];

        /*------------------------ FOR TESTING-------------------------
         for(int c =0; c<inputArray.length;c++){
            System.out.println("INPUT ARRAY: "+ inputArray[c]);
        }
        -------------------------------------------------------------*/

        System.out.println("Total cost: " + mergeSort(inputArray, helpArray, 0, inputArray.length - 1));
    }

    /**
     * Recursive method that sorts the input array and returns the cost of the inversions
     *
     * @param inputArray => the array that contains the values of the input file
     * @param helpArray => array needed in mergeAndCount method
     * @param left => the left index (beginning) of the inputArray
     * @param right => the right index (end) of the inputArray
     * @return => the cost of the inversions
     */
    static long mergeSort(int[] inputArray, int[] helpArray, int left, int right)
    {
        // Keeps track of the inversion cost at a particular node of the recursion tree
        long  weightCost = 0;
        if (right > left) {

            int middle = (left + right) / 2;

            // Count cost of the left sub-array
            weightCost += mergeSort(inputArray, helpArray, left, middle);

            // Count cost of the right sub-array
            weightCost += mergeSort(inputArray, helpArray, middle + 1, right);

            // Count cost of merge
            weightCost += mergeAndCount(inputArray, helpArray, left, middle +1, right);
        }
        return weightCost;
    }

    /**
     * Function that counts the cost of the inversions during the merge process
     *
     * @param inputArray => the array that contains the values of the input file
     * @param helpArray => array needed for the merging process
     * @param left => the left index (beginning) of the inputArray
     * @param middle => the middle index of the inputArray
     * @param right => the right index (end) of the inputArray
     * @return => the cost of the inversions
     */
    static long mergeAndCount(int[] inputArray, int[] helpArray, int left, int middle, int right)
    {
        /*
          i => index for left subarray
          j => index for right subarray
          l =>index of help array
          weightCost => cost of inversions
          sumNextInversion => sum of the special case inversions (A_I = A_J + 1)
          sumNormInversion => sum of the normal inversions
         */
        int i =left, j = middle, l = left;
        long weightCost = 0, sumNextInversion = 0, sumNormInversion = 0;

        /*while loop to compare the values of the to sub-arrays
          (left sub-array => inputArray[left...middle-1], right sub-array => inputArray[middle...right])
          and copy the smaller value to input array
         */
        while ((i <= middle - 1) && (j <= right)) {
            if (inputArray[i] <= inputArray[j]) {
                helpArray[l++] = inputArray[i++];
            }
            else {
                //check in the remaining left sub-array
                for(int p=i; p<=middle-1;p++)
                {
                    //if the index of the right array in the positions j, is smaller from the value of the left array by
                    // 1, then we have a special case inversion. Otherwise, we have a simple inversion.
                    if(inputArray[j]==(inputArray[p]-1)){
                        weightCost+=2;
                        //sumNextInversion++;
                        // System.out.println("im in next num inversion"); --> FOR TESTING
                    }
                    else {
                        weightCost+=3;
                        // sumNormInversion++;
                        // System.out.println("im in simple inversion"); --> FOR TESTING
                    }
                }

                helpArray[l++] = inputArray[j++];
            }
        }

        //calculate the cost: 2 for special case inversion,  3 for simple inversion
        // weightCost = (2 * sumNextInversion) + (3 * sumNormInversion);

        //copy the remaining elements of left sub-array to helpArray (if there' s any)
        while (i <= middle - 1){
            helpArray[l++] = inputArray[i++];
        }


        //copy the remaining elements of right sub-array to helpArray (if there' s any)
        while (j <= right){
            helpArray[l++] = inputArray[j++];
        }


        //Copy the merged elements to inputArray
        for (i = left; i <= right; i++){
            inputArray[i] = helpArray[i];
        }

        return weightCost;
    }

}
