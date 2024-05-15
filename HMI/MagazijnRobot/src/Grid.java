import com.mysql.cj.x.protobuf.MysqlxCrud;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Grid extends JPanel {
    private GridSpace[][] grid;

    private String name;
    public Grid(int width, int height) {
        setBackground(Color.white);
        grid = new GridSpace[width][height];
        setLayout(new FlowLayout());
        //setLayout(new GridLayout(width,height));
        setPreferredSize(new Dimension(300, 300));
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //grid[i][j] = new GridSpace(60,60, Color.BLACK, getSpaceText(width,height,i*5+j));
                grid[i][j] = new GridSpace(Color.BLACK, getSpaceText(width, height, i * 5 + j), Color.white, Color.white);
                //add(grid[i][j]);
            }
        }
        getSpaceName();

    }

    public void getSpaceName(){
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                //System.out.println(me);
                System.out.println("X: " + me.getX() + ", Y: " + me.getY());
                //System.out.println(getSelectedCell(me.getX(), me.getY()).getGridText());
                name = getSelectedCell(me.getX(), me.getY()).getGridText();
                try {
                    StockSituationPanel.changePosition();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                //System.out.println(grid);
            }
        });
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

    private GridSpace getSelectedCell(int xCoordinate, int yCoordinate){
        int x = xCoordinate / 60;
        int y = yCoordinate / 60;
        return grid[y][x];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DrawGrid(g);
    }

    public void DrawGrid(Graphics g){
        int cellWidth = 60;
        int cellHeight = 60;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                GridSpace gridcell = grid[i][j];
                g.setColor(gridcell.getBackGroundColor());
                g.fillRect(j*cellWidth, i*cellHeight, cellWidth-1, cellHeight-1);
                g.setColor(gridcell.getBorderColor());
                g.drawRect(j*cellWidth, i*cellHeight, cellWidth-1, cellHeight-1);
                g.setColor(gridcell.getTextColor());
                g.drawString(gridcell.getGridText(), j*cellWidth + 25, i*cellHeight + 35);
            }
        }

    }

    public String getNamePositie() {
        System.out.println(name);
        return name;

    }
}
