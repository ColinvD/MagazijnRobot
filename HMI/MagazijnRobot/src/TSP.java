import java.util.Arrays;
public class TSP {
    static Coordinates startLocation = new Coordinates(6,6);
    static Coordinates currentLocation = new Coordinates(0,0);
    public static String[] getRoute(String[] locations){
        Coordinates[] coordinates = new Coordinates[locations.length];
        String[] path = new String[locations.length];

        for (int i = 0; i < coordinates.length; i++) {
            int x = Integer.parseInt(locations[i].substring(1,2));
            int y = 0;
            switch (locations[i].substring(0,1)){
                case "A":
                    y=1;
                    break;
                case "B":
                    y=2;
                    break;
                case "C":
                    y=3;
                    break;
                case "D":
                    y=4;
                    break;
                case "E":
                    y=5;
                    break;
            }
            coordinates[i] = new Coordinates(locations[i], x,y);
        }
        int carryCount = 0;
        currentLocation.setXY(startLocation.getX(), startLocation.getY());
        for(int i = 0; i < coordinates.length; i++) {
            double mininumDistance = -1;
            int coordinatesID = -1;
            for (int j = 0; j < coordinates.length; j++) {
                if (coordinates[j].hasVisited() == false) {
                    double distance = coordinates[j].distanceTo(currentLocation);
                    if (mininumDistance == -1 || distance < mininumDistance) {
                        mininumDistance = distance;
                        coordinatesID = j;
                    }
                }
            }
            coordinates[coordinatesID].setVisited(true);
            carryCount++;
            if(carryCount>2){
                currentLocation.setXY(startLocation.getX(), startLocation.getY());
                carryCount = 0;
            } else {
                currentLocation.setXY(coordinates[coordinatesID].getX(), coordinates[coordinatesID].getY());
            }
            path[i] = coordinates[coordinatesID].getLocation();
        }


        carryCount++;

        for (int i = 0; i < coordinates.length; i++) {

        }

        return path;
    }

    public static void main(String args[]){
        String Doos[] = {"A1","B2","A3","C5","D3"};
        System.out.println(Arrays.toString(getRoute(Doos)));
    }
}
