package MainGame;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CardDeckAndPhase {
    private List<String> cardDeck;
    private int bank;
    private List<Player>players;
    Scanner sc = new Scanner(System.in);
    List<String> createDeck() {
        String[] suitMarks = new String[]{"♣", "♠", "♥", "♦"};
        String[] value = new String[]{"2", "3", "4", "5", "6", "7",
                "8", "9", "10", "J", "Q", "K", "A"};
        cardDeck = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                cardDeck.add(suitMarks[i] + value[j]);
            }
        }
        Collections.shuffle(cardDeck);
        bank = 0;
        return cardDeck;
    }
    public void startGame(List<Player> players) {
        this.players = players;

    }

    public void dealingCards(Player player) {
        List<String> giveToHand = new LinkedList<>();
        for (int j = 0; j < 2; j++)
            giveToHand.add(cardDeck.get(j));
        player.setCardInHand(giveToHand);
        for (int i = 0; i < 2; i++)
            cardDeck.add(cardDeck.remove(0));
    }

    void doSteps(Player player) {
        System.out.println(Player.class + "u can write only one step, " +
                "its -'fold, check, bet, call, raise'");
        String stepsInFirstPhase = sc.next();
        switch (stepsInFirstPhase) {
            case "fold":
                fold(player);
                break;
            case "check":
                check(player);
                break;
            case "bet":
                bet(player);
                break;
            case "call":
                call(player);
                break;
            case "raise":
                raise(player);
                break;
            default:
                doSteps(player);
        }
    }
    //take down card in hand
     void fold(Player player) {
        if (player.getLot() != 0){
            bank += player.getLot();
            player.setLot(0);
        }
        players.remove(player);
        System.out.println("you fold this game");
    }

    //skip a turn(in first phase it can do only big lot)
    void check(Player player) {
        System.out.println(player.getName()+ " skip this turn");
    }

    //do lot(equals or more when big lot)
    void bet(Player player) {
        System.out.println("you have "+ player.getMoney() +"how many lots u want to do?" +
                "  ( please take number more than 0)");
        int howManyLots;
        do{
            howManyLots = sc.nextInt();
        }while(!sc.hasNext() && sc.nextInt() < 0);
        player.doLot(howManyLots);
    }

    // equal lot
    void call(Player player) {

    }

    //do more lot than there is now in 2 times
    void raise(Player player) {

    }



    //first phase in game
    void preFlop(){
        for (Player player : players) {
            int lots = 10;
            player.setLot(lots);
            player.setMoney(player.getMoney() - player.getLot());
            System.out.println(player.getName() + " have a lots " +player.getLot());
                    lots += 10;
        }
        for (Player player : players) {
            dealingCards(player);
        }
        while(players.get(0).getLot() != players.get(1).getLot()){
        for (Player player : players) {
            doSteps(player);
        }
        }


    }

    //second phase in game
    void flop() {

    }
    //do steps like a first phase


    //third phase
    void turn() {

    }
    //do steps like a first phase


    //fourth phase
    void river() {

    }

    //do steps like a first phase
    //show card and check score
    List getCardDeck() {
        return cardDeck;
    }

}