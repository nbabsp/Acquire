package DataModel;


public class Main {
    public static void main(String[] args) {
        GameMaster g = new GameMaster();
        g.newGame();
        while(g.playing()) {
            g.turn();
        }
        g.gameOver();
    }
}
