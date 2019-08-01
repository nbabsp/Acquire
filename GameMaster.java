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
        hotels =  new Hotel[] {new Hotel("Tower", "\u001B[103m", 1), new Hotel("Luxor", "\u001B[41m", 1), new Hotel("American", "\u001B[44m", 2), new Hotel("Worldwide", "\u001B[40m", 2), new Hotel("Festival", "\u001B[102m", 2), new Hotel("Imperial", "\u001B[105m", 3), new Hotel("Continental", "\u001B[46m", 3)};

    }

    public Boolean playing() {
        return play;
    }

    public void newGame() {
        //query players
        System.out.print("Player 1: ");
        String input = s.nextLine();
        int j = 2;
        while(j <= 6 && (!input.equals("d") || j == 2)) {
            players.add(new Player(input));
            System.out.print("Player " + j + ": ");
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

//        players.get(0).addTile(drawPile.getTile("E-6"));
//        drawPile.remove(drawPile.getTile("E-6"));
//
//        hotels[0].tileList.add(drawPile.getTile("D-6"));
//        drawPile.remove(drawPile.getTile("D-6"));
//        hotels[0].tileList.add(drawPile.getTile("C-6"));
//        drawPile.remove(drawPile.getTile("C-6"));
//        hotels[0].tileList.add(drawPile.getTile("B-6"));
//        drawPile.remove(drawPile.getTile("B-6"));
//        hotels[0].tileList.add(drawPile.getTile("A-6"));
//        drawPile.remove(drawPile.getTile("A-6"));
//
//        hotels[1].tileList.add(drawPile.getTile("E-7"));
//        drawPile.remove(drawPile.getTile("E-7"));
//        hotels[1].tileList.add(drawPile.getTile("E-8"));
//        drawPile.remove(drawPile.getTile("E-8"));
//        hotels[1].tileList.add(drawPile.getTile("E-9"));
//        drawPile.remove(drawPile.getTile("E-9"));
//        hotels[1].tileList.add(drawPile.getTile("E-10"));
//        drawPile.remove(drawPile.getTile("E-10"));
//
//        hotels[2].tileList.add(drawPile.getTile("F-6"));
//        drawPile.remove(drawPile.getTile("F-6"));
//        hotels[2].tileList.add(drawPile.getTile("G-6"));
//        drawPile.remove(drawPile.getTile("G-6"));
//        hotels[2].tileList.add(drawPile.getTile("H-6"));
//        drawPile.remove(drawPile.getTile("H-6"));
//        hotels[2].tileList.add(drawPile.getTile("I-6"));
//        drawPile.remove(drawPile.getTile("I-6"));
////        hotels[2].tileList.add(drawPile.getTile("I-5"));
////        drawPile.remove(drawPile.getTile("I-5"));
//
//        hotels[4].tileList.add(drawPile.getTile("E-5"));
//        drawPile.remove(drawPile.getTile("E-5"));
//        hotels[4].tileList.add(drawPile.getTile("E-4"));
//        drawPile.remove(drawPile.getTile("E-4"));
//        hotels[4].tileList.add(drawPile.getTile("E-3"));
//        drawPile.remove(drawPile.getTile("E-3"));
//        hotels[4].tileList.add(drawPile.getTile("E-2"));
//        drawPile.remove(drawPile.getTile("E-2"));
////        hotels[4].tileList.add(drawPile.getTile("E-1"));
////        drawPile.remove(drawPile.getTile("E-1"));
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
            merge(adjacentHotels);
        } else if (adjacentHotels.size() == 1) {
            TileList adjTiles = board.getTiles().adjacentTilesExtended(playedTile);
            board.getTiles().remove(adjTiles);

            adjacentHotels.get(0).tileList.add(playedTile);
            adjacentHotels.get(0).tileList.add(adjTiles);

        } else if(board.getTiles().adjacentTiles(playedTile).getSize() > 0) {
            newChain();
        } else {
            board.add(playedTile);
        }
    }

    public void gameOver() {
        printBoard();
        printStocksTable();
        List<Player> winners = new ArrayList<>();
        winners.add(nextPlayer);
        for(Player p : players) {
            if (p.getTotalAssets() > winners.get(0).getTotalAssets()) {
                winners.clear();
                winners.add(p);
            } else if (!winners.contains(p) && p.getTotalAssets() == winners.get(0).getTotalAssets()) {
                winners.add(p);
            }
        }
        if(winners.size() == 1) {
            System.out.println("\nThe winner is... " + winners.get(0).getName().toUpperCase());
        } else {
            System.out.println("\nThe winners are... ");
            for(Player winner : winners) {
                 System.out.println(winner.getName().toUpperCase());
            }
        }
    }

    private void print() {
        printStocksTable();

        printBoard();

        System.out.println("\nPlayer " + nextPlayer.getName() + ":");
        //nextPlayer.printInfo();
    }

    private void printStocksTable() {
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
    }

    private void printBoard() {
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
                    if(nextPlayer.getHand().contains(y + "-" + x)) {
                        System.out.print("\033[4;30m" + "\033[1;30m" + (y + "-" + x) + resetColor);
                        System.out.print(x < 10 ? " |" : "|");
                        flag = false;
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
    }

    private List<Hotel> adjacentHotels(Tile t) {
        List<Hotel> h = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.tileList.adjacentTiles(t).getSize() > 0) {
                h.add(hotel);
            }
        }
        return h;
    }

    private Tile getPlayedTile() {  
        while (true) {
            System.out.println("What tile do you want to add?");
            int hotelExistsCount = 0;
            int adjacent11HotelCount = 0;
            String input = s.nextLine();

            while (!nextPlayer.getHand().contains(input)) {
                System.out.println("Not in hand");
                input = s.nextLine();
            }
            playedTile = nextPlayer.getHand().getTile(input);
            for (Hotel hotel : hotels) {
                if (hotel.tileList.getSize() > 0) {
                    hotelExistsCount++;
                }

                if (hotel.tileList.adjacentTiles(playedTile).getSize() > 0 && hotel.isSafe()) {
                    adjacent11HotelCount++;
                }
            }

            if (adjacent11HotelCount >= 2) {
                System.out.println("Can't merge two safe hotels");
                continue;
            }
            if (adjacentHotels(playedTile).size() == 0 && (board.getTiles().adjacentTiles(playedTile).getSize() > 0 && hotelExistsCount == 7)) {
                System.out.println("Can't create a new hotel");
                continue;

            }
            break;
        }
        return playedTile;
    }

    private void merge(List<Hotel> mergingHotels) {

        Collections.sort(mergingHotels);

        int winner;
        int loser;

        System.out.println("MergingHotles.size() : " + mergingHotels.size());

        if (mergingHotels.get(mergingHotels.size() - 1).equals(mergingHotels.get(mergingHotels.size() - 2))) {
            System.out.println("What hotel do you want to win the merge?");

            Hotel newHotel;
            while(true) {
                newHotel = inputHotel();
                if(newHotel != null && newHotel.equals(mergingHotels.get(mergingHotels.size() - 1))) {
                    break;
                }
                System.out.println("That hotel can't win!");
            }
            winner = mergingHotels.indexOf(newHotel);
        } else {
            winner = mergingHotels.size() - 1;
        }
        if(mergingHotels.size() > 2 && mergingHotels.get(mergingHotels.size() - 2).equals(mergingHotels.get(mergingHotels.size() - 3))) {
            System.out.println("What hotel do you want to lose the merge?");

            Hotel newHotel;
            while(true) {
                newHotel = inputHotel();
                if(newHotel != null && newHotel.equals(mergingHotels.get(mergingHotels.size() - 2))) {
                    break;
                }
                System.out.println("That hotel can't lose!");
            }
            loser = mergingHotels.indexOf(newHotel);

        } else {
            if(winner == mergingHotels.size() - 2) {
                loser = mergingHotels.size() - 1;
            } else {
                loser = mergingHotels.size() - 2;
            }
        }
        System.out.println("Loser: " + loser);
        System.out.println("Winner: " + winner);
        //majority and minority
        List<Player> majority = new ArrayList<>();
        List<Player> minority = new ArrayList<>();

        for(Player p : players) {
            if(mergingHotels.get(loser).getStocks(p.getName()) > 0) {
                if (majority.isEmpty()) {
                    majority.add(p);
                } else if (mergingHotels.get(loser).getStocks(p.getName()) > mergingHotels.get(loser).getStocks(majority.get(0).getName())) {
                    minority.clear();
                    minority.addAll(majority);
                    majority.clear();
                    majority.add(p);
                } else if (mergingHotels.get(loser).getStocks(p.getName()) == mergingHotels.get(loser).getStocks(majority.get(0).getName())) {
                    majority.add(p);
                } else if (minority.isEmpty()) {
                    minority.add(p);
                }  else if (mergingHotels.get(loser).getStocks(p.getName()) > mergingHotels.get(loser).getStocks(minority.get(0).getName())) {
                    minority.clear();
                    minority.add(p);
                } else if (mergingHotels.get(loser).getStocks(p.getName()) == mergingHotels.get(loser).getStocks(minority.get(0).getName())) {
                    minority.add(p);
                }
            }
        }

        if(minority.size() == 0 || majority.size() > 1) {
            for (Player p : majority) {
                p.addMoney(((int) (((mergingHotels.get(loser).cost() * 15 / (double) majority.size()) + 99) / 100) * 100));
            }
        }
        else  {
            majority.get(0).addMoney(((int)(((mergingHotels.get(loser).cost() * 10 / (double)majority.size()) + 99) / 100) * 100));

            for(Player p : minority) {
                p.addMoney(((int)(((mergingHotels.get(loser).cost() * 5 / (double)minority.size()) + 99) / 100) * 100));
            }
        }

        //stocks
        for(int i = 0; i < players.size(); i++) {
            Player p = players.get((players.indexOf(nextPlayer) + i ) % players.size());


            int extraStocks = mergingHotels.get(loser).getStocks(p.getName());
            int keptStock = 0;
            while(extraStocks > 0) {
                printStocksTable();
                System.out.println("\nPlayer " + p.getName() + ":");

                String input;
                //sell stock
                input = "";
                while (!input.matches("[0-" + extraStocks + "]")) {
                    System.out.println("How many " + mergingHotels.get(loser).getName() + " stocks do you want to sell?");
                    input = s.nextLine();
                    p.addMoney(mergingHotels.get(loser).cost() * Integer.parseInt(input));
                    mergingHotels.get(loser).returnStocks(p.getName(), Integer.parseInt(input));

                }
                extraStocks = mergingHotels.get(loser).getStocks(p.getName()) - keptStock;
                if(extraStocks == 0) {
                    continue;
                }

                //trade stock
                input = "";
                while (!input.matches("[0-" + extraStocks + "]") || Integer.parseInt(input) % 2 != 0 || mergingHotels.get(winner).getStocks(mergingHotels.get(winner).getName()) < Integer.parseInt(input) / 2) {
                    System.out.println("How many " + mergingHotels.get(loser).getName() + " stocks do you want to trade for " + mergingHotels.get(winner).getName() + " stocks?");
                    input = s.nextLine();
                    mergingHotels.get(loser).returnStocks(p.getName(), Integer.parseInt(input));
                    mergingHotels.get(winner).setStocks(p.getName(), Integer.parseInt(input) / 2);
                }
                extraStocks = mergingHotels.get(loser).getStocks(p.getName()) - keptStock;
                if(extraStocks == 0) {
                    continue;
                }

                //keep stock
                input = "";
                while (!input.matches("[0-" + extraStocks + "]")) {
                    System.out.println("How many " + mergingHotels.get(loser).getName() + " stocks do you want to keep?");
                    input = s.nextLine();
                    keptStock += Integer.parseInt(input);
                }
                extraStocks = mergingHotels.get(loser).getStocks(p.getName()) - keptStock;
            }
        }

        //Tiles
        mergingHotels.get(winner).tileList.add(mergingHotels.get(loser).tileList);
        mergingHotels.get(loser).tileList.getTiles().clear();

        if(mergingHotels.size() > 2) {
            mergingHotels.remove(mergingHotels.indexOf(mergingHotels.get(loser)));
            print();
            merge(mergingHotels);
        } else {
            TileList adjTiles = board.getTiles().adjacentTilesExtended(playedTile);
            board.getTiles().remove(adjTiles);

            mergingHotels.get(winner).tileList.add(playedTile);
            mergingHotels.get(winner).tileList.add(adjTiles);
            print();
        }
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

        TileList adjTiles = board.getTiles().adjacentTilesExtended(playedTile);
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
            if(hotel.getStocks(hotel.getName()) > 0 && hotel.exists()) {
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
