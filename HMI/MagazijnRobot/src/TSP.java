import java.util.ArrayList;

public class TSP {
    static Coordinates startLocation = new Coordinates(6,6);
    static Coordinates currentLocation = new Coordinates(0,0);
    public static ArrayList<Locatie> getRoute(ArrayList<Locatie> locations) {
        try {
            Coordinates[] coordinates = new Coordinates[locations.size()];
            ArrayList<Locatie> path = new ArrayList<>();

            for (int i = 0; i < coordinates.length; i++) {
                int x = Integer.parseInt(locations.get(i).getLocation().substring(1, 2));
                int y = switch (locations.get(i).getLocation().substring(0, 1)) {
                    case "A" -> 1;
                    case "B" -> 2;
                    case "C" -> 3;
                    case "D" -> 4;
                    case "E" -> 5;
                    default -> 0;
                };
                coordinates[i] = new Coordinates(locations.get(i).getLocation(), x, y,locations.get(i).getWeight(),locations.get(i).getOrderlineID());
            }
            int carryCount = 0;
            currentLocation.setXY(startLocation.getX(), startLocation.getY());
            for (int i = 0; i < coordinates.length; i++) {
                double mininumDistance = -1;
                int coordinatesID = -1;
                for (int j = 0; j < coordinates.length; j++) {
                    if (!coordinates[j].hasVisited()) {
                        double distance = coordinates[j].distanceTo(currentLocation);
                        if (mininumDistance == -1 || distance < mininumDistance) {
                            mininumDistance = distance;
                            coordinatesID = j;
                        }
                    }
                }
                coordinates[coordinatesID].setVisited(true);
                carryCount++;
                if (carryCount > 2) {
                    currentLocation.setXY(startLocation.getX(), startLocation.getY());
                    carryCount = 0;
                } else {
                    currentLocation.setXY(coordinates[coordinatesID].getX(), coordinates[coordinatesID].getY());
                }
                path.add(new Locatie(coordinates[coordinatesID].getLocation(),coordinates[coordinatesID].getGewicht(),coordinates[coordinatesID].getOrderlineid()));
            }
            return path;
        } catch (NullPointerException e) {
            System.out.println("Selecteer een juist order");
        }
        return null;
    }

    public static void main(String args[]){
//        String Doos[] = {"A1","B2","A2", "D3", "E3", "E5", "C4", "E1"}; //testData
//        System.out.println(Arrays.toString(getRoute(Doos)));
    }
}
