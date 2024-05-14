public class Coordinates { //wordt gebruikt bij TSP
    private String location;
    private int x;
    private int y;
    private boolean visited;

    public Coordinates(String location, int x, int y){
        this.location = location;
        this.x = x;
        this.y = y;
        this.visited = false;
    }

    public Coordinates(int x, int y){
        this.location = null;
        this.visited = false;
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Coordinates other){
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(Math.pow(dx,2) +  Math.pow(dy,2));
    }

    public String getLocation() {
        return location;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasVisited() {
        return visited;
    }

    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setVisited(boolean visited){
        this.visited = visited;
    }

    @Override
    public String toString() {
        return "Locatie: " + location + " x:" + x + " y:" + y + " visited: " + visited;
    }
}
