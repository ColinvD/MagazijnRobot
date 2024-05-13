import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BBP {
    static int[] firstFit(ArrayList<Locatie> weight, int n, int c) {
        // Initialize result (Count of bins)
        int res = 0;
        int[] Box = new int[weight.size()];

        // Create an array to store remaining space in bins
        // there can be at most n bins
        int []bin_rem = new int[n];

        // Place items one by one
        for (int i = 0; i < n; i++)
        {
            // Find the first bin that can accommodate
            // weight[i]
            int j;
            for (j = 0; j < res; j++)
            {
                if (bin_rem[j] >= weight.get(i).getWeight())
                {
                    bin_rem[j] = bin_rem[j] - weight.get(i).getWeight();
                    System.out.println(weight.get(i).getLocation());
                    break;
                }
            }

            // If no bin could accommodate weight[i]
            if (j == res)
            {
                bin_rem[res] = c - weight.get(i).getWeight();
                System.out.println(weight.get(i).getLocation());
                res++;
            }
            Box[i] = res;
        }
        return Box;
    }
    static int[] firstFitDec(ArrayList<Locatie> weight, int n, int c)
    {

        weight.sort(new CompareToWeight().reversed());
        // First sort all weights in decreasing order
        // Now call first fit for sorted items
        return firstFit(weight, n, c);
    }

    // Driver program
    public static void main(String[] args) throws SQLException {
        Database database = new Database();
        database.databaseConnect();

        ArrayList<Locatie> weight = database.getWeights(73595);
        for (Locatie locatie : weight){
            System.out.print(locatie.getWeight());
            System.out.println(locatie.getLocation());
        }
        int c = 20;
        int n = weight.size();
        System.out.print("Number of bins required in First Fit : "
                + Arrays.toString(firstFitDec(weight, n, c)));

    }
}

