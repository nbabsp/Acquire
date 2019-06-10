package DataModel;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileList {
    private List<Tile> tiles;

    public TileList() {
        tiles = new ArrayList<>();
    }

    public TileList(Tile tile) {
        tiles = new ArrayList<>();
        tiles.add(tile);
    }

    public TileList(TileList tileList) {
        tiles = new ArrayList<>();
        tiles.addAll(tileList.tiles);
    }

    public void add(Tile t) {
        tiles.add(t);
    }

    public void add(TileList t) {
        tiles.addAll(t.tiles);
    }

    public Tile remove() {
        return tiles.remove(tiles.size() - 1);
    }

    public boolean remove(Tile t) {
        return tiles.remove(t);
    }

    public boolean remove(TileList t) {
        return tiles.removeAll(t.tiles);
    }

    public int getSize() {
        return tiles.size();
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public TileList adjacentTilesExtended(Tile tile) {
        TileList adjacentList = new TileList();
        TileList oldAdjacentList = new TileList();
        for (Tile t : tiles) {
            if (t.isAdjacent(tile)) {
                adjacentList.add(t);
            }
        }
        while(!adjacentList.equals(oldAdjacentList)) {
            oldAdjacentList.replaceAll(adjacentList);
            for (int i = 0; i < adjacentList.getSize(); i++) {
                for (Tile t2 : tiles) {
                    if (t2.isAdjacent(adjacentList.getTile(i)) && !adjacentList.contains(t2.getName())) {
                        adjacentList.add(t2);
                    }
                }
            }
        }

        return adjacentList;
    }

    public TileList adjacentTiles(Tile tile) {
        TileList adjacentList = new TileList();
        for(Tile t : tiles) {
            if (t.isAdjacent(tile)) {
                adjacentList.add(t);
            }
        }
        return adjacentList;
    }

    public boolean contains(String name) {
        for(Tile t : tiles) {
            if(name.equals(t.getName())) {
                return true;
            }
        }
        return false;
    }

    public Tile getTile(String name) {
        for(Tile t : tiles) {
            if(name.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    public boolean equals(TileList t) {
        if(tiles.size() != t.getSize()) {
            return false;
        }
        sort();
        t.sort();

        for(int i = 0; i < tiles.size(); i++) {
            if(!tiles.get(i).getName().equals(t.getTile(i).getName())) {
                return false;
            }
        }
        return true;
    }

    public void replaceAll(TileList tileList) {
        tiles.clear();
        add(tileList);
    }

    public Tile getTile(int n) {
        return tiles.get(n);
    }

    public void shuffle() {
        Collections.shuffle(tiles);
    }

    public void sort() {
        Collections.sort(tiles);
    }

    public void printTiles() {
        for (Tile tile : tiles) {
            System.out.println(tile.getName());
        }

    }

}
