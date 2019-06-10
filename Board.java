package DataModel;

public class Board {
    private TileList tiles;

    public Board() {
        tiles = new TileList();
    }
    public TileList getTiles() {
        return tiles;
    }

    public void add(Tile t) {
        tiles.add(t);
    }

    public void remove(Tile t) {
        tiles.remove(t);
    }

    public void print() {
        System.out.println("Board: ");
        tiles.sort();
        //tiles.printTiles();

        System.out.println("-------------------------------------------------------------");
        for(char y = 'A'; y <= 'I'; y++) {
            System.out.print("|");
            for(int x = 1; x <= 12; x++) {
                if(tiles.contains(y + "-" + x)) {
                    System.out.print(tiles.getTile(y + "-" + x).getName());
                    System.out.print(x<10 ? " |" : "|");

                } else {
                    System.out.print("    |");
                }
            }
            System.out.println("\n-------------------------------------------------------------");
        }
    }
    public void print(TileList t) {
        System.out.println("Board: ");
        tiles.sort();
        //tiles.printTiles();

        System.out.println("-------------------------------------------------------------");
        for(char y = 'A'; y <= 'I'; y++) {
            System.out.print("|");
            for(int x = 1; x <= 12; x++) {
                if(tiles.contains(y + "-" + x)) {
                    System.out.print(tiles.getTile(y + "-" + x).getName());
                    System.out.print(x<10 ? " |" : "|");

                } else if (t.contains(y + "-" + x)) {
                    System.out.print(t.getTile(y + "-" + x).getName());
                    System.out.print(x<10 ? " |" : "|");
                } else {
                    System.out.print("    |");
                }
            }
            System.out.println("\n-------------------------------------------------------------");
        }
    }
}
