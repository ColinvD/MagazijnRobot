import java.util.ArrayList;

public class Locatie {
    private String location;
    private int weight;
    private int orderlineID;


    public Locatie(String location, int weight,int orderlineID) {
        this.location = location;
        this.weight = weight;
        this.orderlineID = orderlineID;
        //list.add(weight);

    }

    @Override
    public String toString() {
        return "Locatie{" +
                "location='" + location + '\'' +
                ", weight=" + weight +
                ", orderlineID=" + orderlineID +
                '}';
    }

    public String getLocation() {
        return location;
    }

    public int getWeight() {
        return weight;
    }

    public int getOrderlineID(){
        return orderlineID;
    }

}
