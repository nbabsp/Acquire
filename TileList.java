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

    public List<Tile> adjacent(Tile tile) {
        List<Tile> adjacentList = new ArrayList<>();
        for(Tile t : tiles) {
            if (t.isAdjacent(tile)) {
                adjacentList.add(t);
            }
        }
        return adjacentList;
    }


    //TODO tiles next to eachother need to both be counted for adjacent
//    public List<Tile> adjacent(Tile tile) {
//        List<Tile> adjacentList = new ArrayList<>();
//        for(Tile t : tiles) {
//            if (t.isAdjacent(tile)) {
//                adjacentList.add(t);
//
//                for(Tile t2 : adjacentExcept(t, adjacentList)) {
//                    if(!adjacentList.contains(t2)) {
//                        adjacentList.add(t2);
//                    }
//                }
//            }
//        }
//        return adjacentList;
//    }
//
//    public List<Tile> adjacentExcept(Tile tile, List<Tile> tileList) {
//        List<Tile> adjacentList = new ArrayList<>();
//        for(Tile t : tiles) {
//            if (t.isAdjacent(tile) && !tileList.contains(tile)) {
//                adjacentList.add(t);
//            }
//        }
//        return adjacentList;
//    }

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
