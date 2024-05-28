import com.mysql.cj.x.protobuf.MysqlxCrud;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Grid extends JPanel implements Listener {
    private GridSpace[][] grid;
    protected static String locationRobot2 = "X:290 Y:290";
    private int robotX;
    private int robotY;
    private ArrayList<Locatie> route;
    private String name;
    private SerialCommunicator serialCommunicator;

    public Grid(int width, int height) throws IOException, InterruptedException {
        serialCommunicator = new SerialCommunicator("COM6", 9600);
        serialCommunicator.AddListener(this);
        SendOrder sendOrder = new SendOrder(serialCommunicator);
        Thread.sleep(4000);
        ArrayList<String> values = new ArrayList<String>();
        values.add("E5");
        values.add("E4");
        values.add("E3");
        sendOrder.sendOrderValues(values);

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
        locationRobot(locationRobot1(locationRobot2),g);
        if(route != null) {
            drawRoute(route, g);
        }
    }
    public String locationRobot1(String location){
        repaint();
        return location;
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

    public void locationRobot(String locatieRobot,Graphics g){
        g.setColor(Color.green);
        try {
            robotX = Integer.parseInt(locatieRobot.substring(locatieRobot.indexOf(':') + 1, locatieRobot.indexOf('Y') - 1));
            robotY = Integer.parseInt(locatieRobot.substring(locatieRobot.indexOf(":", locatieRobot.indexOf(':') + 1) + 1));
        } catch (NumberFormatException e) {

        }

        if(robotX > 295) {
            robotX = 295;
        }
        if(robotY > 293) {
            robotY = 293;
        }
        g.fillOval(robotX,robotY,15,15);
    }

    public String getNamePositie() {
        System.out.println(name);
        return name;
    }

    public void drawRoute(ArrayList<Locatie> route, Graphics g){
        int prevX = robotX+10;
        int prevY = robotY+10;
        g.setColor(Color.RED);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        for (int i = 0; i < (route.size()); i++) {
            int[] xy = convertLocationToXY(route.get(i).getLocation());
            int x = xy[0];
            int y = xy[1];
            g.drawLine(prevX, prevY, x, y);
            prevX = x;
            prevY = y;
            if(i==2 || i==route.size()-1){
                prevX = 300;
                prevY = 300;
                g.setColor(Color.BLUE);
            }
        }
    }

    public int[] convertLocationToXY(String location){
        int[] xy = new int[2];

        xy[0] = (Integer.parseInt(location.substring(1,2))-1) * 60 + 30;

        switch (location.substring(0,1)){
            case "A":
                xy[1] = 30;
                break;
            case "B":
                xy[1] = 90;
                break;
            case "C":
                xy[1] = 150;
                break;
            case "D":
                xy[1] = 210;
                break;
            case "E":
                xy[1] = 270;
                break;
        }
        return xy;
    }

    public void setRoute(ArrayList<Locatie> r){
        route = r;
    }

    @Override
    public void onMessageReceived(String message) throws SQLException, IOException {
        if(message.startsWith("X:") && message.contains("Y:")) {
//            System.out.println("check");
            serialCommunicator.sendMessageToArduino("checkJavaConnection");
            locationRobot2 = message;
            repaint();
        }
    }
}
