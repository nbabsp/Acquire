package DataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel implements Comparable<Hotel>{
    private String name;
    private String color;
    private Integer price;
    private Stock[] stocks;
    private HashMap<String, List<Stock>> stocks2;
    TileList tileList;

    public Hotel(String name, String color, Integer price) {
        this.name = name;
        this.color = color;
        this.price = price;
        stocks = new Stock[25];
        for (int i = 0; i < stocks.length; i++) {
            stocks[i] = new Stock(name);
        }
        tileList = new TileList();
    }

    public Hotel(String name, String color, Integer price, List<Player> players) {
        this.name = name;
        this.color = color;
        this.price = price;
        stocks2 = new HashMap<>();
        List<Stock> hotelStocks = new ArrayList<>();
        for(int i = 0; i < 25; i++) {
            hotelStocks.add(new Stock(name));
        }
        stocks2.put(name, hotelStocks);
        for (Player p : players) {
            stocks2.put(p.getName(), new ArrayList<Stock>());
        }
        tileList = new TileList();
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setStocks(String name, int amount) {
        int i = 0;
        for(Stock s : stocks) {
            if (i >= amount) {
                return;
            }
            if (s.getOwner().equals(this.name)) {
                s.changeOwner(name);
                i++;
            }
        }
    }

    public void returnStocks(String name, int amount) {
        int i = 0;
        for(Stock s : stocks) {
            if (i >= amount) {
                return;
            }
            if (s.getOwner().equals(name)) {
                s.changeOwner(this.name);
                i++;
            }
        }
    }

    public int getStocks(String n) {
        int counter = 0;
        for(Stock s : stocks) {
            if (s.getOwner().equals(n)) {
                counter++;
            }
        }
        return counter;
    }

    public boolean isSafe() {
        return tileList.getSize() >= 11;
    }

    public boolean isMega() {
        return tileList.getSize() >= 41;
    }

    public boolean exists() {
        return tileList.getSize() > 0;
    }

    public List<String> majority() {
        HashMap<String, Integer> hashMap = new HashMap<>();

        for(Stock s : stocks) {
            if(!s.getOwner().equals(name)) {
                if(hashMap.get(s.getOwner()) != null) {
                    hashMap.put(s.getOwner(), hashMap.get(s.getOwner() + 1));
                } else {
                    hashMap.put(s.getOwner(), 1);
                }
            }
        }

        int max = 0;
        for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if(entry.getValue() > max) {
                max = entry.getValue();
            }
        }
        List<String> players = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if(entry.getValue() == max) {
                players.add(entry.getKey());
            }
        }
        return players;

    }

    public List<String> minority() {
        HashMap<String, Integer> hashMap = new HashMap<>();

        for(Stock s : stocks) {
            if(!s.getOwner().equals(name)) {
                if(hashMap.get(s.getOwner()) != null) {
                    hashMap.put(s.getOwner(), hashMap.get(s.getOwner() + 1));
                } else {
                    hashMap.put(s.getOwner(), 1);
                }
            }
        }

        int max = 0;
        for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if(entry.getValue() > max) {
                max = entry.getValue();
            }
        }
        int max2 = 0;
        for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if(entry.getValue() < max && entry.getValue() > max2) {
                max2 = entry.getValue();
            }
        }
        List<String> players = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if(entry.getValue() == max2) {
                players.add(entry.getKey());
            }
        }
        return players;

    }

    public int compareTo(Hotel h) {
        return tileList.getSize() - h.tileList.getSize();
    }

    public boolean equals(Hotel h) {
        return compareTo(h) == 0;
    }

    public void printStocks(Player p) {
        int counter = 0;
        for(Stock s : stocks) {
            if (s.getOwner().equals(p.getName())) {
                counter++;
            }
        }
        System.out.printf("%-13s: %d%n", name, counter);
    }

    public int cost() {
        int[] prices = new int[108];
        prices[0] = 100 + 100*price;
        prices[1] = 100 + 100*price;
        prices[2] = 100 + 100*price;
        prices[3] = 200 + 100*price;
        prices[4] = 300 + 100*price;
        prices[5] = 400 + 100*price;
        for (int i = 6; i < prices.length; i++) {
            if(i <= 10) {
                prices[i] = 500 + 100*price;
            }
            else if(i <= 20) {
                prices[i] = 600 + 100*price;
            }
            else if(i <= 30) {
                prices[i] = 700 + 100*price;
            }
            else if(i <= 40) {
                prices[i] = 800 + 100*price;
            }
            else {
                prices[i] = 900 + 100*price;
            }
        }
        return prices[tileList.getSize()];
    }
//    public int cost() {
//        int[] prices = new int[108];
//        prices[0] = 100 + 100*price;
//        prices[1] = 100 + 100*price;
//
//        prices[2] = 100 + 100*price;
//        prices[3] = 200 + 100*price;
//        prices[4] = 300 + 100*price;
//        prices[5] = 400 + 100*price;
//
//        prices[6] = 500 + 100*price;
//        prices[7] = 500 + 100*price;
//        prices[8] = 500 + 100*price;
//        prices[9] = 500 + 100*price;
//        prices[10] = 500 + 100*price;
//
//        prices[11] = 600 + 100*price;
//        prices[12] = 600 + 100*price;
//        prices[13] = 600 + 100*price;
//        prices[14] = 600 + 100*price;
//        prices[15] = 600 + 100*price;
//        prices[16] = 600 + 100*price;
//        prices[17] = 600 + 100*price;
//        prices[18] = 600 + 100*price;
//        prices[19] = 600 + 100*price;
//        prices[20] = 600 + 100*price;
//
//        prices[21] = 700 + 100*price;
//        prices[22] = 700 + 100*price;
//        prices[23] = 700 + 100*price;
//        prices[24] = 700 + 100*price;
//        prices[25] = 700 + 100*price;
//        prices[26] = 700 + 100*price;
//        prices[27] = 700 + 100*price;
//        prices[28] = 700 + 100*price;
//        prices[29] = 700 + 100*price;
//        prices[30] = 700 + 100*price;
//
//        prices[31] = 800 + 100*price;
//        prices[32] = 800 + 100*price;
//        prices[33] = 800 + 100*price;
//        prices[34] = 800 + 100*price;
//        prices[35] = 800 + 100*price;
//        prices[36] = 800 + 100*price;
//        prices[37] = 800 + 100*price;
//        prices[38] = 800 + 100*price;
//        prices[39] = 800 + 100*price;
//        prices[40] = 800 + 100*price;
//
//        prices[41] = 900 + 100*price;
//        for (int i = 42; i < prices.length; i++) {
//            prices[i] = 900 + 100*price;
//        }
//
//        return prices[tileList.getSize()];
//
//    }

}
