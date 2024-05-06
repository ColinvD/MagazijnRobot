import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GridSpace extends JPanel {
    private JLabel jlGridName;
    private Color backGroundColor;
    private String gridText;
    private Color textColor;
    private Color borderColor;
    public GridSpace(int cellWidth, int cellHeight, Color color, String gridName){
        setPreferredSize(new Dimension(cellWidth,cellHeight));
        setLayout(new FlowLayout());
        backGroundColor = color;
        setBackground(backGroundColor);
        borderColor = Color.white;
        setBorder(new LineBorder(borderColor));
        gridText = gridName;
        jlGridName = new JLabel(gridText);
        textColor = Color.white;
        jlGridName.setForeground(textColor);
        add(jlGridName);
    }
    public GridSpace(Color backGroundColor, String gridText, Color textColor, Color borderColor){
        this.backGroundColor = backGroundColor;
        this.gridText = gridText;
        this.textColor = textColor;
        this.borderColor = borderColor;
    }
    public void setBackgroundColor(Color color){
        backGroundColor = color;
        setBackground(backGroundColor);
    }

    public void setSpaceName(String newName){
        gridText = newName;
        jlGridName.setText(gridText);
    }

    public void setTextColor(Color color){
        textColor = color;
        jlGridName.setForeground(textColor);
    }
    public void setBorderColor(Color color){
        borderColor = color;
        setBorder(new LineBorder(borderColor));
    }

    public Color getBackGroundColor() {
        return backGroundColor;
    }

    public String getGridText() {
        return gridText;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }
}
