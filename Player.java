package DataModel;


public class Player {
    private String name;
    private TileList hand;
    private int money;
    private int totalAssets;

    public Player(String name) {
        this.name = name;
        hand = new TileList();
        money = 6000;
        totalAssets = money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void removeMoney (int amount) {
        money -= amount;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public TileList getHand() {
        return hand;
    }

    public void setTotalAssets(int a) {
        totalAssets = a;
    }

    public int getTotalAssets() {
        return totalAssets;
    }

    public void addTile(Tile t) {
        hand.add(t);
    }

    public boolean removeTile(Tile t) {
        return hand.remove(t);
    }

    public void printHand() {
        hand.printTiles();
    }

    public void printInfo() {
        System.out.println("Tiles:");
        hand.sort();
        hand.printTiles();
        System.out.printf("\n\nMoney        : %d%n", money);
    }

}
