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

//        List<String> list1 = new LinkedList<>();
//        list1.add("4♣");
//        list1.add("6♦");
//        list1.add("4♣");
//        list1.add("6♦");
//        list1.add("6♠");
//        list1.add("A♠");
//        list1.add("2♦");
//        List<String> list2 = new LinkedList<>();
//        list2.add("4♦");
//        list2.add("A♠");
//        list2.add("4♣");
//        list2.add("6♦");
//        list2.add("4♦");
//        list2.add("A♠");
//        list2.add("2♦");


        cd.startGame(playerList);


    }
}
