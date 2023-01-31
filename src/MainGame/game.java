package MainGame;

import java.util.Scanner;

public class game {
    public static void main(String[] args) {
//        CardDeckAndPhase cd = new CardDeckAndPhase();
//        cd.createDeck();
//        System.out.println(cd.getCardDeck());
//        Player p1 = new Player();
//        cd.dealingCards(p1);
//        System.out.println(cd.getCardDeck());
//        System.out.println(p1.getCardInHand());
        Scanner sc = new Scanner(System.in);
        int howManyLots;
        do{
            howManyLots = sc.nextInt();
        }while(!sc.hasNext() && sc.nextInt() <= 0);
        System.out.println(howManyLots);
    }
}
