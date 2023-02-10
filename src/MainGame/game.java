package MainGame;

import java.util.*;

public class game {
    public static void main(String[] args) {
        CardDeckAndPhase cd = new CardDeckAndPhase();
        List<Player> playerList = new LinkedList<>();
        playerList.add(new Player("Ted", 1000));
        playerList.add(new Player("Bat", 1000));
        cd.createDeck();
        cd.startGame(playerList);



    }
}
