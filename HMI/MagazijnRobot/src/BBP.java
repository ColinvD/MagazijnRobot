import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BBP {
    static ArrayList<ArrayList<Locatie>> firstFit(ArrayList<Locatie> weight, int n, int c) {
        // Initialize result (Count of bins)
        int oldres = 0;
        int res = 0;
        ArrayList<ArrayList<Locatie>> Boxes = new ArrayList<>();
        ArrayList<Locatie> Box = new ArrayList<>();

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
                    System.out.println("Doos " + j + ": " + bin_rem[j] + " - " + weight.get(i).getWeight());
                    bin_rem[j] = bin_rem[j] - weight.get(i).getWeight();
//                    System.out.println(weight.get(i).getLocation());
                    break;
                }
            }

            // If no bin could accommodate weight[i]
            if (j == res)
            {
                bin_rem[res] = c;
                System.out.println("doos " + j + ": " + bin_rem[j] + " - " + weight.get(i).getWeight());
                bin_rem[res] -= weight.get(i).getWeight();
//                System.out.println(weight.get(i).getLocation());
                res++;
            }

            System.out.println(weight.get(i).getLocation() + ":  " + weight.get(i).getWeight());
            if(oldres < res){
                Box = new ArrayList<>();
                Boxes.add(Box);
                oldres = res;
            }
            Boxes.get(j).add(weight.get(i));
        }
        return Boxes;
    }
    static ArrayList<ArrayList<Locatie>> firstFitDec(ArrayList<Locatie> weight, int n, int c)
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

        ArrayList<Locatie> weight = database.getWeights();
//        for (Locatie locatie : weight){
//            System.out.print(locatie.getWeight());
//            System.out.println(locatie.getLocation());
//        }
        int c = 20;
        int n = weight.size();
//        System.out.print("Number of bins required in First Fit : "
//                + Arrays.toString(firstFitDec(weight, n, c)));
        ArrayList<ArrayList<Locatie>> Boxes = firstFitDec(weight, n, c);
        for (int i = 0; i < Boxes.size(); i++) {
            String box[] = new String[Boxes.get(i).size()];
            for (int j = 0; j < Boxes.get(i).size(); j++) {
                box[j] = Boxes.get(i).get(j).getLocation();
                //System.out.println(Boxes.get(i).get(j).getLocation() + ": " + Boxes.get(i).get(j).getWeight());
            }
            System.out.println(Arrays.toString(TSP.getRoute(box)));
        }
    }
}

