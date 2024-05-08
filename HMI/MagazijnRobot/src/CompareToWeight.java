import java.util.Comparator;

public class CompareToWeight implements Comparator<Locatie> {
    @Override
    public int compare(Locatie o1, Locatie o2) {
        return o1.getWeight() - o2.getWeight();
    }
}

