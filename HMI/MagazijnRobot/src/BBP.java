import javax.xml.crypto.Data;
import java.sql.SQLException;

public class BBP {
    static int nextFit(int[] weight, int n, int c)
    {
        // Initialize result (Count of bins) and remaining
        // capacity in current bin.
        int res = 0, bin_rem = c;

        // Place items one by one
        for (int i = 0; i < n; i++) {
            // If this item can't fit in current bin
            if (weight[i] > bin_rem) {
                res++; // Use a new bin
                bin_rem = c - weight[i];
            }
            else
                bin_rem -= weight[i];
        }
        return res+1;
    }

    static int[] nextFitBox(int[] weight, int n, int c){
        int box[] = new int[weight.length];
        int res = 0, bin_rem = c;

        for (int i = 0; i < n; i++){
            if (weight[i] > bin_rem){
                res++;
                bin_rem = c - weight[i];
            } else {
                bin_rem -= weight[i];
            }
            box[i] = res;
        }
        return box;
    }

    // Driver program
    public static void main(String[] args) throws SQLException {
        Database database = new Database();
        database.databaseConnect();
        int[] weight = database.getWeights();
        int c = 20;
        int n = weight.length;
        System.out.println("Number of bins required: " + nextFit(weight, n, c));
        for (int i : database.getWeights()){
            System.out.println(i);
        }
    }
}

