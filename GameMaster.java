package DataModel;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GameMaster {
    private List<Player> players;
    private Hotel[] hotels;
    private Board board;
    private TileList drawPile;
    private Scanner s;
    private Player nextPlayer;
    private Tile playedTile;
    private Boolean play = true;
    private String resetColor = "\u001B[0m";

    public GameMaster() {
        s = new Scanner(System.in);
        players = new ArrayList<>();
        board = new Board();
        drawPile = new TileList();
        for(char x = 1; x <= 12; x++) {
            for (char y = 'A'; y <= 'I'; y++) {
                drawPile.add(new Tile(x, y));
            }
        }
        drawPile.shuffle();
        //hotels = new Hotel[] {new Hotel("Tower", "\u001B[93m", 1), new Hotel("Luxor", "\u001B[31m", 1), new Hotel("American", "\u001B[34m", 2), new Hotel("Worldwide", "\u001B[90m", 2), new Hotel("Festival", "\u001B[32m", 2), new Hotel("Imperial", "\u001B[95m", 3), new Hotel("Continental", "\u001B[36m", 3)};
        hotels = new Hotel[] {new Hotel("Tower", "\u001B[103m", 1), new Hotel("Luxor", "\u001B[41m", 1), new Hotel("American", "\u001B[44m", 2), new Hotel("Worldwide", "\u001B[40m", 2), new Hotel("Festival", "\u001B[102m", 2), new Hotel("Imperial", "\u001B[105m", 3), new Hotel("Continental", "\u001B[46m", 3)};

    }

    public Boolean playing() {
        return play;
    }

    public void newGame() {
        //query players
        System.out.println("Player 1: ");
        String input = s.nextLine();
        int j = 2;
        while(!input.equals("d")) {
            players.add(new Player(input));
            System.out.println("Player " + j + ": ");
            input = s.nextLine();
            j++;
        }

        //pick 1 tile per player
        Tile lowTile = null;
        for(Player player : players) {
            Tile tile = drawPile.remove();
            if (board.getTiles().getSize() == 0) {
                lowTile = tile;
                nextPlayer = player;
            }
            board.add(tile);
            if (tile.compareTo(lowTile) > 1) {
                lowTile = tile;
                nextPlayer = player;
            }

        }

        //draw hands for each player
        for(Player player : players) {
            for(int i = 0; i < 6; i++) {
                player.addTile(drawPile.remove());
            }
        }
    }

    public void turn() {
        print();

        if(endGame()) {
            return;
        }

        playTile();

        if(endGame()) {
            return;
        }

        buy3Stock();

        if(endGame()) {
            return;
        }

        discardUnplayableAndDraw();

        nextPlayer = players.get((players.indexOf(nextPlayer) + 1 ) % players.size());
    }

    private void playTile() {
        playedTile = getPlayedTile();
        List<Hotel> adjacentHotels = adjacentHotels(playedTile);

        nextPlayer.removeTile(playedTile);

        if (adjacentHotels.size() >= 2) {
            merge();
        } else if (adjacentHotels.size() == 1) {
            TileList adjTiles = board.getTiles().adjacentTiles(playedTile);
            board.getTiles().remove(adjTiles);

            adjacentHotels.get(0).tileList.add(playedTile);
            adjacentHotels.get(0).tileList.add(adjTiles);

        } else if(board.getTiles().adjacent(playedTile).size() > 0) {
            newChain();
        } else {
            board.add(playedTile);
        }
    }

    private void print() {
        for(Player p : players) {
            int assets = p.getMoney();
            for (Hotel h : hotels) {
                assets += h.getStocks(p.getName()) * h.cost();
            }
            p.setTotalAssets(assets);
        }

            int counter = 31;
        for(Player p : players) {
            counter += (p.getName().length() > String.valueOf(p.getTotalAssets()).length() ? p.getName().length() : String.valueOf(p.getTotalAssets()).length()) + 3;
        }
        String separator = "\n";
        for(int i = 0; i < counter; i++) {
            separator += "-";
        }
        separator += "\n";

        System.out.print(separator);
        System.out.print("| Hotel        |  Cost | Bank | ");
        for(Player p : players) {
            System.out.printf("%" + (p.getName().length() > String.valueOf(p.getTotalAssets()).length() ? p.getName().length() : String.valueOf(p.getTotalAssets()).length()) + "s | ", p.getName());
        }
        System.out.print(separator);

        for(Hotel h : hotels) {
            System.out.printf("| %-12s | %5d | %4d | ", h.getName(), h.cost(), h.getStocks(h.getName()));
            for(Player p : players) {
                System.out.printf("%" + (p.getName().length() > String.valueOf(p.getTotalAssets()).length() ? p.getName().length() : String.valueOf(p.getTotalAssets()).length()) + "d | ", h.getStocks(p.getName()));
            }
            System.out.print(separator);
        }

        System.out.printf("| %-12s | %5s | %4s | ", "Cash", "-", "-");
        for(Player p : players) {
            System.out.printf("%" + (p.getName().length() > String.valueOf(p.getTotalAssets()).length() ? p.getName().length() : String.valueOf(p.getTotalAssets()).length()) + "d | ", p.getMoney());
        }
        System.out.print(separator);

        System.out.printf("| %-12s | %5s | %4s | ", "Total Assets", "-", "-");
        for(Player p : players) {
            System.out.printf("%" + (p.getName().length() > String.valueOf(p.getTotalAssets()).length() ? p.getName().length() : String.valueOf(p.getTotalAssets()).length()) + "d | ", p.getTotalAssets());
        }
        System.out.print(separator + "\n");


        TileList hotelTiles = new TileList();
        for(Hotel h : hotels) {
            hotelTiles.getTiles().addAll(h.tileList.getTiles());
        }

        System.out.println("Board: ");
        board.getTiles().sort();

        System.out.println("-------------------------------------------------------------");
        for(char y = 'A'; y <= 'I'; y++) {
            System.out.print("|");
            for(int x = 1; x <= 12; x++) {
                if(board.getTiles().contains(y + "-" + x)) {
                    System.out.print("\u001B[100m" + board.getTiles().getTile(y + "-" + x).getName() + resetColor);
                    System.out.print(x < 10 ? " |" : "|");

                } else {
                    boolean flag = true;
                    for(Hotel h : hotels){
                        if (h.tileList.contains(y + "-" + x)) {
                            System.out.print(h.getColor() + "\u001B[97m" + hotelTiles.getTile(y + "-" + x).getName() + resetColor);
                            System.out.print(x < 10 ? " |" : "|");
                            flag = false;
                        }
                    }
                    if(flag) {
                        System.out.print(resetColor + (y + "-" + x) + resetColor);
                        System.out.print(x < 10 ? " |" : "|");
                    }
//                    if(flag) {
//                        System.out.print("    |");
//                    }
                }
            }
            System.out.println("\n-------------------------------------------------------------");
        }



        System.out.println("\nPlayer " + nextPlayer.getName() + ":");
        nextPlayer.printInfo();

    }

    private List<Hotel> adjacentHotels(Tile t) {
        List<Hotel> h = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.tileList.adjacent(t).size() > 0) {
                h.add(hotel);
            }
        }
        return h;
    }

    private Tile getPlayedTile() {
        System.out.println("What tile do you want to add?");

        String input = s.nextLine();
        int hotelExistsCount = 0;
        int adjacent11HotelCount = 0;
        Tile playedTile = null;
        boolean illegal = true;
        while (illegal) {
            while (!nextPlayer.getHand().contains(input)) {
                System.out.println("Not in hand");
                input = s.nextLine();
            }
            playedTile = nextPlayer.getHand().getTile(input);
            for (Hotel hotel : hotels) {
                if (hotel.tileList.getSize() > 0) {
                    hotelExistsCount++;
                }

                if (hotel.tileList.adjacent(playedTile).size() > 0 && hotel.isSafe()) {
                    adjacent11HotelCount++;
                }
            }

            if (adjacent11HotelCount >= 2) {
                System.out.println("Can't merge two safe hotels");
                input = s.nextLine();
                continue;
            }
            if (adjacentHotels(playedTile).size() == 0 && (board.getTiles().adjacent(playedTile).size() > 0 && hotelExistsCount == 7)) {
                System.out.println("Can't create a new hotel");
                input = s.nextLine();
                continue;

            }

            illegal = false;

        }
        return playedTile;
    }

    private void merge() {
        List<Hotel> mergers = new ArrayList<>();
        for(Hotel h : hotels){
            if(h.tileList.adjacent(playedTile).size() > 0) {
                mergers.add(h);
            }
        }

        Collections.sort(mergers);


        //majority and minority
        List<Player> majority = new ArrayList<>();
        List<Player> minority = new ArrayList<>();
        majority.add(nextPlayer);
        minority.add(nextPlayer);

        for(Player p : players) {
           if(mergers.get(0).getStocks(p.getName()) > mergers.get(0).getStocks(majority.get(0).getName())) {
               minority.clear();
               minority.addAll(majority);
               majority.add(p);
           } else if(mergers.get(0).getStocks(p.getName()) == mergers.get(0).getStocks(majority.get(0).getName())) {
               majority.add(p);
           } else if(mergers.get(0).getStocks(p.getName()) > mergers.get(0).getStocks(minority.get(0).getName())) {
               minority.clear();
               majority.add(p);
           } else if(mergers.get(0).getStocks(p.getName()) == mergers.get(0).getStocks(minority.get(0).getName())) {
               majority.add(p);
           }
        }

        if(majority.size() >= 1) {
            for (Player p : majority) {
                p.addMoney(((int) (((mergers.get(0).cost() * 15 / (double) majority.size()) + 99) / 100) * 100));
            }
        }
        else  {
            majority.get(0).addMoney(((int)(((mergers.get(0).cost() * 10 / (double)majority.size()) + 99) / 100) * 100));

            for(Player p : minority) {
                p.addMoney(((int)(((mergers.get(0).cost() * 5 / (double)minority.size()) + 99) / 100) * 100));
            }
        }

        for(int i = 0; i < players.size(); i++) {
            Player p;
            if (players.indexOf(nextPlayer) + i < players.size()) {
                p = players.get(players.indexOf(nextPlayer) + i);
            } else {
                p = players.get(players.indexOf(nextPlayer) + i - players.size());
            }

            int extraStocks = mergers.get(0).getStocks(p.getName());
            int keptStock = 0;
            while(extraStocks > 0) {
                print();

                String input;
                //sell stock
                input = "";
                while (!input.matches("[0-" + mergers.get(0).getStocks(p.getName()) + "]")) {
                    System.out.println("How many " + mergers.get(0).getName() + " stocks do you want to sell?");
                    input = s.nextLine();
                    p.addMoney(mergers.get(0).cost());
                    mergers.get(0).returnStocks(p.getName(), Integer.parseInt(input));

                }
                extraStocks = mergers.get(0).getStocks(p.getName()) - keptStock;
                if(extraStocks == 0) {
                    continue;
                }

                //trade stock
                input = "";
                while (!input.matches("[0-" + mergers.get(0).getStocks(p.getName()) + "]") || Integer.parseInt(input) % 2 != 0 || mergers.get(1).getStocks(mergers.get(1).getName()) < Integer.parseInt(input) / 2) {
                    System.out.println("How many " + mergers.get(0).getName() + " stocks do you want to sell?");
                    input = s.nextLine();
                    mergers.get(0).returnStocks(p.getName(), Integer.parseInt(input));
                    mergers.get(1).setStocks(p.getName(), Integer.parseInt(input) / 2);
                }
                extraStocks = mergers.get(0).getStocks(p.getName()) - keptStock;
                if(extraStocks == 0) {
                    continue;
                }

                //keep stock
                input = "";
                while (!input.matches("[0-" + (mergers.get(0).getStocks(p.getName()) - keptStock) + "]")) {
                    System.out.println("How many " + mergers.get(0).getName() + " stocks do you want to sell?");
                    input = s.nextLine();
                    keptStock += Integer.parseInt(input);
                }
                extraStocks = mergers.get(0).getStocks(p.getName()) - keptStock;
            }
        }

        mergers.get(1).tileList.getTiles().addAll(mergers.get(0).tileList.getTiles());
        mergers.get(0).tileList.getTiles().clear();
    }

    private void newChain() {
        System.out.println("What hotel do you want to create?");

        Hotel newHotel;
        while(true) {
            newHotel = inputHotel();
            if(newHotel != null && !newHotel.exists()) {
                break;
            }
            System.out.println("That hotel already exists.");
        }

        TileList adjTiles = board.getTiles().adjacentTiles(playedTile);
        board.getTiles().remove(adjTiles);

        newHotel.tileList.add(playedTile);
        newHotel.tileList.add(adjTiles);

        newHotel.setStocks(nextPlayer.getName(), 1);
    }

    private void buy3Stock() {
        int i = 0;
        while (i < 3) {
            int count = buy1Stock();
            if (count < 0) {
                return;
            }
            i += count;
        }
    }

    private int buy1Stock() {
        System.out.println("What stock do you want to buy?");

        Hotel hotel;
        while(true) {
            hotel = inputHotel();
            if(hotel == null) {
                return -1;
            }
            if(hotel.getStocks(hotel.getName()) > 0) {
                break;
            }
            System.out.println("That hotel has no available stock.");
        }

        if (nextPlayer.getMoney() >= hotel.cost()) {
            nextPlayer.removeMoney(hotel.cost());
            hotel.setStocks(nextPlayer.getName(), 1);
            return 1;
        }
        System.out.println("Not enough money!");
        return 0;
    }

    private boolean endGameMega() {
        for(Hotel h : hotels) {
            if (h.isMega()) {
                return true;
            }
        }
        return false;
    }

    private boolean endGameSafe() {
        for(Hotel h : hotels) {
            if (h.tileList.getSize() == 0) {
                continue;
            }
            if(!h.isSafe()) {
                return false;
            }
        }
        return true;
    }

    private boolean endGame() {
        boolean gameEnd = endGameSafe() || endGameMega();

        if(gameEnd){
            System.out.println("Do you want to end the game immediately?");
            String input = s.nextLine();
            if(input.equalsIgnoreCase("y")) {
                play = false;
            }
        }
        return !play;
    }

    private void discardUnplayableAndDraw() {
        discardUnplayable(drawPile);
        discardUnplayable(nextPlayer.getHand());
        while (nextPlayer.getHand().getSize() < 6) {
            nextPlayer.addTile(drawPile.remove());
        }
    }

    private void discardUnplayable(TileList tileList) {
        for (int i = 0; i < tileList.getSize(); i++) {
            if (unplayable(tileList.getTile(i))) {
                tileList.remove(tileList.getTile(i));
            }
        }
    }

    private boolean unplayable(Tile tile) {
        int adjacent11HotelCount = 0;
        List<Hotel> adjHotels = adjacentHotels(tile);
        for (Hotel hotel : adjHotels) {
            if (hotel.isSafe()) {
                adjacent11HotelCount++;
            }
        }
        return adjacent11HotelCount >= 2;
    }

    private Hotel inputHotel() {
        String input;

        while(true) {
            System.out.print("Hotel name: ");
            input = s.nextLine();
            if (input.equals("d")) {
                return null;
            }
            for(Hotel h : hotels) {
                if (input.equalsIgnoreCase(h.getName())) {
                    return  h;
                }
            }
        }

    }
}
