package MainGame;

import java.util.*;

public class game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CardDeckAndPhase cd = new CardDeckAndPhase();
        List<Player> playerList = new LinkedList<>();
        playerList.add(new Player("Ted", 1000));
        playerList.add(new Player("Bat", 1000));
        while(true){
            cd.startGame(playerList);
            System.out.println("if u wanna to end game, please write 'end'");
            String end = scanner.nextLine();
            if (end == "end")
                break;
        }




    }
}
