import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public class BBP {
    static int firstFit(Integer[] weight, int n, int c)
    {
        // Initialize result (Count of bins)
        int res = 0;

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
                if (bin_rem[j] >= weight[i])
                {
                    bin_rem[j] = bin_rem[j] - weight[i];
                    break;
                }
            }

            // If no bin could accommodate weight[i]
            if (j == res)
            {
                bin_rem[res] = c - weight[i];
                res++;
            }
        }
        return res;
    }
    static int firstFitDec(Integer[] weight, int n, int c)
    {

        // First sort all weights in decreasing order
        Arrays.sort(weight, Collections.reverseOrder());

        // Now call first fit for sorted items
        return firstFit(weight, n, c);
    }

    // Driver program
    public static void main(String[] args) throws SQLException {
        Database database = new Database();
        database.databaseConnect();
        Integer[] weight = {1,2,3};
        int c = 20;
        int n = weight.length;
        System.out.print("Number of bins required in First Fit : "
                + firstFitDec(weight, n, c));

    }
}

