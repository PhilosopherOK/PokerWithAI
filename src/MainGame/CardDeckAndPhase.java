package MainGame;

import java.util.*;

public class CardDeckAndPhase {
    private List<String> cardDeck;
    List<String> cardDeckWithOutShuffle;

    private List<String> cardInTable;
    private int bank;
    private List<Player> players;

    Random random = new Random();
    Scanner sc = new Scanner(System.in);
    Map<String, Integer> cardValue12 = new HashMap<>();
    Map<String, Integer> cardValue51 = new HashMap<>();

    public void startGame(List<Player> players) {
        this.players = players;

    }

    List<String> createDeck() {
        String[] suitMarks = new String[]{"♣", "♠", "♥", "♦"};
        String[] value = new String[]{"2", "3", "4", "5", "6", "7",
                "8", "9", "10", "J", "Q", "K", "A"};
        cardDeck = new LinkedList<>();
        cardDeckWithOutShuffle = new LinkedList<>();
        int temp2 = 0;
        for (int i = 0; i < 4; i++) {
            int temp1 = 0;
            for (int j = 0; j < 13; j++) {
                cardDeck.add(value[j] + suitMarks[i]);
                cardDeckWithOutShuffle.add(value[j] + suitMarks[i]);
                cardValue12.put(value[j] + suitMarks[i], temp1 + 2);
                cardValue51.put(value[j] + suitMarks[i], temp2);
                temp1++;
                temp2++;
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

    public void dealingCards() {
        for (Player player : players) {
            List<String> giveToHand = new LinkedList<>();
            for (int j = 0; j < 2; j++)
                giveToHand.add(cardDeck.get(j));
            player.setCardInHand(giveToHand);
            for (int i = 0; i < 2; i++)
                cardDeck.add(cardDeck.remove(0));
        }
    }

    List<String> putCartOnTable(int howMuchCard) {
        cardInTable = new LinkedList<>();
        for (int i = 0; i < howMuchCard; i++)
            cardInTable.add(cardDeck.get(i));
        for (int i = 0; i < howMuchCard; i++)
            cardDeck.add(cardDeck.remove(0));
        System.out.println(cardInTable);
        return cardInTable;
    }

    void doSteps(Player player) {
        System.out.println(player.getName() + "You can write only one step, " +
                "its -'fold, check, call, raise'");
        String stepsInFirstPhase = sc.nextLine();
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

    void stepsAllPhase() {
        for (Player player : players) {
            System.out.println(player.getName() + "you have " + player.getMoney());
            System.out.println("Your card is:");
            System.out.println(players.get(0).getCardInHand());
            doSteps(player);
        }
        for (Player player : players) {
            if (player.getLot() != maxLot()) {
                System.out.println(player.getName() +
                        " Your lot is not actual, please use steps call or raise for up it");
                System.out.println(player.getName() + "you have " + player.getMoney());
                doSteps(player);
            } else {
                System.out.println("Lot" + player.getName() + " equal several lot is " + maxLot());
            }
        }
        for (Player player : players) {
            bank += player.getLot();
            player.setLot(0);
        }
        System.out.println("Now bank is " + bank);
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
        System.out.println(player.getName() + "you have " + player.getMoney() +
                " and your lot is " + player.getLot());
    }

    //do more lot
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

    List<String> sortedLastHandFromValue(List<String> list) {
        List<String> sortedByValues = new LinkedList<>(list);
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 6; i++) {
                List<String> tempList = new LinkedList<>();
                if (cardValue12.get(sortedByValues.get(i)) > cardValue12.get(sortedByValues.get(i + 1))) {
                    tempList.add(sortedByValues.get(i + 1));
                    sortedByValues.add(i + 1, sortedByValues.remove(i));
                    sortedByValues.add(i, tempList.get(0));
                    sortedByValues.remove(i + 1);
                }
            }
        }
        return sortedByValues;
    }

    List<String> sortedLastHandFromMark(List<String> list) {
        List<String> sortedByMarks = new LinkedList<>(list);
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 6; i++) {
                List<String> tempList = new LinkedList<>();
                if (cardValue51.get(sortedByMarks.get(i)) > cardValue51.get(sortedByMarks.get(i + 1))) {
                    tempList.add(sortedByMarks.get(i + 1));
                    sortedByMarks.add(i + 1, sortedByMarks.remove(i));
                    sortedByMarks.add(i, tempList.get(0));
                    sortedByMarks.remove(i + 1);
                }
            }
        }
        return sortedByMarks;
    }

    int fleshRoyal(List<String> list) {
        int scoreCombination = 0;
        for (int i = 8; i < 51; i += 13) {
            scoreCombination = 0;
            for (int j = i; j < i + 5; j++) {
                for (int k = 0; k < 7; k++) {
                    if (cardDeckWithOutShuffle.get(j).equals(list.get(k))) {
                        scoreCombination++;
                        break;
                    }
                }
            }
            if (scoreCombination == 5) {
                i = 52;
                return 1000;
            }
        }
        return 0;
    }

    int streetFlesh(List<String> list) {
        list = sortedLastHandFromMark(list);
        int scoreCombination = 0;
        int maxInCombination = 0;
        for (int i = 0; i < 6; i++) {
            if (cardValue51.get(list.get(i + 1)) - cardValue51.get(list.get(i)) == 1) {
                if (maxInCombination < cardValue12.get(list.get(i))) {
                    maxInCombination = cardValue12.get(list.get(i));
                }
                if (scoreCombination == 4) {
                    return 900;
                }
                scoreCombination++;
            } else {
                scoreCombination = 0;
            }
            if (scoreCombination == 4) {
                return 900 + maxInCombination;
            }
        }
        return 0;
    }

    int care(List<String> list) {
        list = sortedLastHandFromValue(list);
        int scoreCombination = 0;
        int maxInCombination = 0;
        for (int i = 0; i < 6; i++) {
            if (cardValue12.get(list.get(i)) == cardValue12.get(list.get(i + 1))) {
                if (maxInCombination < cardValue12.get(list.get(i))) {
                    maxInCombination = cardValue12.get(list.get(i));
                }
                scoreCombination++;
            } else {
                scoreCombination = 0;
            }
            if (scoreCombination == 3) {
                return 800 + maxInCombination;
            }
        }
        return 0;
    }


    int fullHouse(List<String> list) {
        list = sortedLastHandFromValue(list);
        int maxInCombination = 0;
        if (set(list) != 0 && twoPare(list) != 0) {
            if (set(list) - 400 > twoPare(list) - 300) {
                maxInCombination = set(list) - 400;
            } else {
                maxInCombination = twoPare(list) - 300;
            }
            return 700 + maxInCombination;
        }
        return 0;
    }

    int flesh(List<String> list) {
        list = sortedLastHandFromMark(list);
        int scoreCombination = 0;
        int temp = -13;
        int maxInCombination = 0;
        for (int i = 0; i < 4; i++) {
            scoreCombination = 0;
            temp += 13;
            for (int j = temp; j < temp + 13; j++) {
                for (int k = 0; k < 7; k++) {
                    if (j == cardValue51.get(list.get(k))) {
                        if (maxInCombination < cardValue12.get(list.get(k))) {
                            maxInCombination = cardValue12.get(list.get(k));

                        }
                        scoreCombination++;
                        if (scoreCombination == 5) {
                            return 600 + maxInCombination;
                        }
                    }
                }
            }
        }
        return 0;
    }

    int street(List<String> list) {
        list = sortedLastHandFromValue(list);
        int scoreCombination = 0;
        int maxInCombination = 0;
        for (int i = 0; i < 6; i++) {
            if (cardValue12.get(list.get(i + 1)) - cardValue12.get(list.get(i)) == 1) {
                if (maxInCombination < cardValue12.get(list.get(i))) {
                    maxInCombination = cardValue12.get(list.get(i));
                }
                scoreCombination++;
                if (scoreCombination == 4) {
                    return 500 + maxInCombination;
                }
            } else {
                scoreCombination = 0;
            }
        }

        return 0;
    }

    int set(List<String> list) {
        list = sortedLastHandFromValue(list);
        int scoreCombination = 0;
        int maxInCombination = 0;
        for (int i = 0; i < 6; i++) {
            if (cardValue12.get(list.get(i)) == cardValue12.get(list.get(i + 1))) {
                if (maxInCombination < cardValue12.get(list.get(i))) {
                    maxInCombination = cardValue12.get(list.get(i));
                }
                scoreCombination++;
            } else {
                scoreCombination = 0;
            }
            if (scoreCombination == 2) {
                return 400 + maxInCombination;
            }
        }
        return 0;
    }

    int twoPare(List<String> list) {
        list = sortedLastHandFromValue(list);
        int scoreCombination = 0;
        int scoreCombinationTwoPare = 0;
        int maxInCombination = 0;
        for (int i = 0; i < 6; i++) {
            if (cardValue12.get(list.get(i)) == cardValue12.get(list.get(i + 1))) {
                if (maxInCombination < cardValue12.get(list.get(i))) {
                    maxInCombination = cardValue12.get(list.get(i));
                }
                scoreCombination++;
                scoreCombinationTwoPare++;
            } else {
                scoreCombination = 0;
            }
            if (scoreCombinationTwoPare == 2) {
                return 300 + maxInCombination;
            }
        }
        return 0;
    }

    int pare(List<String> list) {
        list = sortedLastHandFromValue(list);
        int maxInCombination = 0;
        for (int i = 0; i < 6; i++) {
            if (cardValue12.get(list.get(i)) == cardValue12.get(list.get(i + 1))) {
                if (maxInCombination < cardValue12.get(list.get(i))) {
                    maxInCombination = cardValue12.get(list.get(i));
                }
                return 200 + maxInCombination;
            }
        }
        return 0;
    }

    //Need to rework mb  SOS
    int bestCard(List<String> list) {
        int maxValueOfCard = cardValue12.get(list.get(0));
        for (int i = 0; i < 6; i++) {
            if (maxValueOfCard < cardValue12.get(list.get(i + 1))) {
                maxValueOfCard = cardValue12.get(list.get(i + 1));
            }
        }
        return maxValueOfCard;
    }

    void foundCombination(List<String> list) {
        for (Player player : players) {
            List<String> cardForFindCombination = new LinkedList<>(cardInTable);
            cardForFindCombination.add(player.getCardInHand().get(0));
            cardForFindCombination.add(player.getCardInHand().get(1));
            if (fleshRoyal(cardForFindCombination) == 0) {
                if (streetFlesh(cardForFindCombination) == 0) {
                    if (care(cardForFindCombination) == 0) {
                        if (fullHouse(cardForFindCombination) == 0) {
                            if (flesh(cardForFindCombination) == 0) {
                                if (street(cardForFindCombination) == 0) {
                                    if (set(cardForFindCombination) == 0) {
                                        if (twoPare(cardForFindCombination) == 0) {
                                            if (pare(cardForFindCombination) == 0) {
                                                if (bestCard(cardForFindCombination) == 0) {
                                                    System.out.println(player.setScore(bestCard(cardForFindCombination)) + "Best cardby " + player.getName());
                                                }
                                            } else {
                                                System.out.println(player.setScore(pare(cardForFindCombination)) + "Pareby " + player.getName());
                                            }
                                        } else {
                                            System.out.println(player.setScore(twoPare(cardForFindCombination)) + "Two Pareby " + player.getName());
                                        }
                                    } else {
                                        System.out.println(player.setScore(set(cardForFindCombination)) + "Setby " + player.getName());
                                    }
                                } else {
                                    System.out.println(player.setScore(street(cardForFindCombination)) + "Streetby " + player.getName());
                                }
                            } else {
                                System.out.println(player.setScore(flesh(cardForFindCombination)) + "Fleshby " + player.getName());
                            }
                        } else {
                            System.out.println(player.setScore(fullHouse(cardForFindCombination)) + "Full Houseby " + player.getName());
                        }
                    } else {
                        System.out.println(player.setScore(care(cardForFindCombination)) + "Careby " + player.getName());
                    }
                } else {
                    System.out.println(player.setScore(streetFlesh(cardForFindCombination)) + "Street Fleshby by " + player.getName());
                }
            } else {
                System.out.println(player.setScore(fleshRoyal(cardForFindCombination)) + "Flesh Royal by " + player.getName());
            }
        }
        if (players.get(0).getScore() > players.get(1).getScore()) {
            System.out.println(players.get(0).getName() + " is Winner with " + players.get(0).getScore());
        } else if (players.get(0).getScore() < players.get(1).getScore()) {
            System.out.println(players.get(1).getName() + " is Winner with " + players.get(1).getScore());
        } else {
            draw(list);
        }
    }

    void draw(List<String> list) {
        if (players.get(0).getScore() == players.get(1).getScore() && players.get(0).getScore() > 100) {
            for (Player player : players) {
                List<String> cardForFindCombination = new LinkedList<>(cardInTable);
                cardForFindCombination.add(player.getCardInHand().get(0));
                cardForFindCombination.add(player.getCardInHand().get(1));
                player.setScore(bestCard(cardForFindCombination));
            }
            if (players.get(0).getScore() > players.get(1).getScore()) {
                System.out.println(players.get(0).getName() + " is Winner with " + players.get(0).getScore());
            } else if (players.get(0).getScore() < players.get(1).getScore()) {
                System.out.println(players.get(1).getName() + " is Winner with " + players.get(1).getScore());
            } else {
                System.out.println("Its a draw !");
            }
        } else {
            System.out.println("Its a draw !");
        }
    }

    //first phase in game
    void preFlop() {
        System.out.println("Now is preFlop phase, please take your card and do your steps");
        dealingCards();
        stepsAllPhase();

    }

    //second phase in game its 3 cards on table and do steps like a first phase
    void flop() throws InterruptedException {
        System.out.println("Now is flop phase, please look on card and do your steps");
        putCartOnTable(3);
        wait(5000);
        stepsAllPhase();
    }

    //third phase in game its 4 cards on table and do steps like a first phase
    void turn() throws InterruptedException {
        System.out.println("Now is turn phase, please look on card and do your steps");
        putCartOnTable(1);
        wait(5000);
        stepsAllPhase();
    }

    //fourth phase in game its 5 cards on table, do steps like a first phase, and finish loop
    void river() throws InterruptedException {
        System.out.println("Now is river phase, please look on card and do your steps," +
                " and in finish we will make a score of points");
        putCartOnTable(1);
        wait(5000);
        for (Player player : players) {
            System.out.println(player.getName() + "you have " + player.getMoney());
            System.out.println("Your card is:");
            System.out.println(players.get(0).getCardInHand());
            doSteps(player);
        }
        for (Player player : players) {
            bank += player.getLot();
            player.setLot(0);
        }
        System.out.println("Now bank is " + bank);


    }

    List getCardDeck() {
        return cardDeck;
    }

}