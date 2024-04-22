import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GridSpace extends JPanel {
    private JLabel jlGridName;
    public GridSpace(int cellWidth, int cellHeight, Color color, String gridName){
        setPreferredSize(new Dimension(cellWidth,cellHeight));
        setLayout(new FlowLayout());
        setBackground(color);
        setBorder(new LineBorder(Color.white));
        jlGridName = new JLabel(gridName);
        jlGridName.setForeground(Color.white);
        add(jlGridName);
    }
    public void setBackgroundColor(Color color){
        setBackground(color);
    }

    public void setSpaceName(String newName){
        jlGridName.setText(newName);
    }

    public void setTextColor(Color color){
        jlGridName.setForeground(color);
    }
}
