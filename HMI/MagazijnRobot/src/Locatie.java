import java.util.ArrayList;

public class Locatie {
    private String location;
    private int weight;


    public Locatie(String location, int weight) {
        this.location = location;
        this.weight = weight;
        //list.add(weight);

    }

    public String getLocation() {
        return location;
    }

    public int getWeight() {
        return weight;
    }

}
