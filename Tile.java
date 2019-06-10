package DataModel;


public class Tile implements Comparable<Tile>{

    private int x;
    private char y;
    private String name;

    public Tile(int x, char y) {
        this.x = x;
        this.y = y;
        name = y + "-" + x;
    }
    public boolean isAdjacent(Tile t) {
        return (x == t.x && y == t.y + 1) ||
                (x == t.x && y == t.y - 1) ||
                (x == t.x + 1 && y == t.y) ||
                (x == t.x - 1 && y == t.y);
    }

    public String getName() {
        return name;
    }

    public int compareTo(Tile t) {
        return (y - t.y == 0) ? x - t.x : y - t.y;
    }

    public void print() {
        System.out.printf("%s\n", name);
    }
}
