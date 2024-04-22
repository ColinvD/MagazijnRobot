import javax.swing.*;
import java.awt.*;

public class Grid extends JPanel {
    private GridSpace[][] grid;
    public Grid(int width, int height){
        grid = new GridSpace[width][height];
        setLayout(new GridLayout(width,height));
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new GridSpace(60,60, Color.BLACK, getSpaceText(width,height,i*5+j));
                add(grid[i][j]);
            }
        }
    }

    public GridSpace[][] getGrid(){
        return grid;
    }
    private String getSpaceText(int gridWidth, int gridHeight, int position){
        if(position>=gridWidth*gridHeight || position<0) return "Error";
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        alphabet = alphabet.toUpperCase();
        String text = String.valueOf(alphabet.charAt(position/gridWidth))+ ((position%gridWidth)+1);
        return text;
    }
}
