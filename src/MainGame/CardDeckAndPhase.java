package MainGame;

import java.util.*;

public class CardDeckAndPhase {
    private List<String> cardDeck;
    private int bank;
    private List<Player> players;
    Random random = new Random();
    Scanner sc = new Scanner(System.in);

    public void startGame(List<Player> players) {
        this.players = players;

    }

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

    private int maxLot() {
        int maxLot = 0;
        for (Player player : players) {
            if (maxLot < player.getLot()) {
                maxLot = player.getLot();
            }
        }
        return maxLot;
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
        System.out.println(player.getName() + "u can write only one step, " +
                "its -'fold, check, call, raise'");
        String stepsInFirstPhase = sc.next();
        switch (stepsInFirstPhase) {
            case "fold":
                fold(player);
                break;
            case "check":
                check(player);
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
        if (player.getLot() != 0) {
            bank += player.getLot();
            player.setLot(0);
        }
        players.remove(player);
        System.out.println("you fold this game");
    }

    //skip a turn(in first phase it can do only big lot)
    void check(Player player) {
        System.out.println(player.getName() + " skip this turn with lot " + player.getLot());
    }

    // equal lot
    void call(Player player) {
        player.doLot(maxLot() - player.getLot());
        System.out.println(player.getName()+ "you have " + player.getMoney()+
                " and your lot is " + player.getLot());
    }

    //do more lot than there is now in 2 times
    void raise(Player player) {
        System.out.println(player.getName() + "you have " + player.getMoney() + " and your lot is "
                + player.getLot() + " How many u want to bet?  (multiple 10 please)");
        int howLotBet = 0;
        for (int j = 0; j < 10; j--) {
            howLotBet = sc.nextInt();
            if (howLotBet > 0 && howLotBet % 10 == 0) {
                j = 100;
            } else {
                System.out.println("multiple 10 please !");
            }
        }
        player.doLot(howLotBet);
    }


    //first phase in game
    void preFlop() {
        for(Player player:players){
            dealingCards(player);
        }
        System.out.println("Your card is:");
        System.out.println(players.get(0).getCardInHand().toString());

        for(Player player:players){
            System.out.println(player.getName()+ "you have " + player.getMoney());
            doSteps(player);
        }
        for(Player player:players){
            if(player.getLot() != maxLot()){
                System.out.println(player.getName() +
                        " Your lot is not actual, please use steps call or raise for up it");
                System.out.println(player.getName()+ "you have " + player.getMoney());
                doSteps(player);
            }else {
                System.out.println("several lot is "+ maxLot());
                for(Player player1:players){
                    bank += player1.getLot();
                    player1.setLot(0);
                }
                System.out.println("Now bank is " + bank);
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