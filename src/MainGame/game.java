package MainGame;

import java.util.*;

public class game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CardDeckAndPhase cd = new CardDeckAndPhase();
        List<Player> playerList = new LinkedList<>();
        playerList.add(new Player("Ted", 1000));
        playerList.add(new Botty("Botty1", 1000));
        playerList.add(new Botty("Botty2", 1000));
        cd.startGame(playerList);
    }
}
