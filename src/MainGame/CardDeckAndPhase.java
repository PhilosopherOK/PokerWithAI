package MainGame;

import java.util.*;

public class CardDeckAndPhase {
    Scanner scanner = new Scanner(System.in);
    private List<String> cardDeck;
    List<String> cardDeckWithOutShuffle;

    private List<String> cardInTable = new LinkedList<>();
    ;
    private int bank;
    private List<Player> players = new LinkedList<>();
    private List<Player> startWithPlayers = new LinkedList<>();


    Random random = new Random();
    Scanner sc = new Scanner(System.in);
    Map<String, Integer> cardValue12 = new HashMap<>();
    Map<String, Integer> cardValue51 = new HashMap<>();

    public void startGame(List<Player> playerList) {
        if (!playerList.equals(players)) {
            for (Player player : playerList) {
                this.players.add(player);
                this.startWithPlayers.add(player);
            }
        }
        createDeck();
        preFlop();
        checkToWinner();
        flop();
        checkToWinner();
        turn();
        checkToWinner();
        river();
        cardInTable.clear();
        for (Player player : players)
            player.getCardInHand().clear();
    }

    void checkToWinner() {
        if (players.size() == 1) {
            System.out.println(players.get(0).getName() + " is winner");
            players.get(0).setMoney(players.get(0).getMoney() + bank);
            bank = 0;

            cardInTable.clear();
            for (Player player : players)
                player.getCardInHand().clear();

            System.out.println("if u wanna to end game, please write 'end'");
            String end = scanner.nextLine();
            if (end == "end") {
                players.clear();
                for (Player player : startWithPlayers)
                    players.add(player);
                startGame(players);
            }
        }
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
        for (int i = 0; i < howMuchCard; i++)
            cardInTable.add(cardDeck.get(i));
        for (int i = 0; i < howMuchCard; i++)
            cardDeck.add(cardDeck.remove(0));
        System.out.println(cardInTable);
        return cardInTable;
    }

    void doSteps(Player player) {
        System.out.println(player.getName() + " you can write only one step, " +
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

    void lotsIsntEquals() {
        int temp = 0;
        for (Player player : players) {
            if (player.getLot() != maxLot()) {
                System.out.println(player.getName() +
                        " Your lot " + player.getLot() + " is not actual, please use steps call or raise for up it");
                System.out.println(player.getName() + " you have " + player.getMoney());
                doSteps(player);
                temp = 1;
            } else {
                System.out.println(" Lot" + player.getName() + " equal several lot is " + maxLot());
            }
        }
        if (temp == 1) {
            lotsIsntEquals();
        }

    }

    void stepsAllPhase() {
        for (Player player : players) {
            System.out.println(player.getName() + " you have " + player.getMoney());
            System.out.println("Your card is: ");
            System.out.println(player.getCardInHand());
            System.out.println("And your lot is " + player.getLot());
            doSteps(player);
        }

        lotsIsntEquals();

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
        checkToWinner();
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
        if (player.getClass() == Botty.class) {
            player.doLot(20);
        } else {
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
                scoreCombination++;
                if (scoreCombination == 4) {
                    return 900 + maxInCombination;
                }
            } else {
                scoreCombination = 0;
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
                if (scoreCombination == 3) {
                    return 800 + maxInCombination;
                }
            } else {
                scoreCombination = 0;
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
                if (scoreCombination == 2) {
                    return 400 + maxInCombination;
                }
            } else {
                scoreCombination = 0;
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
                if (scoreCombinationTwoPare == 2) {
                    return 300 + maxInCombination;
                }
            } else {
                scoreCombination = 0;
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

    List<String> listCardForFoundComb(Player player) {
        List<String> list = new LinkedList<>(cardInTable);
        list.add(player.getCardInHand().get(0));
        list.add(player.getCardInHand().get(1));
        return list;
    }

    int foundCombination(List<String> listForFound) {
        if (fleshRoyal(listForFound) == 0) {
            if (streetFlesh(listForFound) == 0) {
                if (care(listForFound) == 0) {
                    if (fullHouse(listForFound) == 0) {
                        if (flesh(listForFound) == 0) {
                            if (street(listForFound) == 0) {
                                if (set(listForFound) == 0) {
                                    if (twoPare(listForFound) == 0) {
                                        if (pare(listForFound) == 0) {
                                            return bestCard(listForFound);
                                            //System.out.println(player.setScore(bestCard(listForFound)) + " Best card by " + player.getName());
                                        } else {
                                            //System.out.println(player.setScore(pare(listForFound)) + " Pare by " + player.getName());
                                            return pare(listForFound);
                                        }
                                    } else {
                                        //System.out.println(player.setScore(twoPare(listForFound)) + " Two Pare by " + player.getName());
                                        return twoPare(listForFound);
                                    }
                                } else {
                                    //System.out.println(player.setScore(set(listForFound)) + " Set by " + player.getName());
                                    return set(listForFound);
                                }
                            } else {
                                //System.out.println(player.setScore(street(listForFound)) + " Street by " + player.getName());
                                return street(listForFound);
                            }
                        } else {
                            //System.out.println(player.setScore(flesh(listForFound)) + " Flesh by " + player.getName());
                            return flesh(listForFound);
                        }
                    } else {
                        //System.out.println(player.setScore(fullHouse(listForFound)) + " Full House by " + player.getName());
                        return fullHouse(listForFound);
                    }
                } else {
                    //System.out.println(player.setScore(care(listForFound)) + " Care by " + player.getName());
                    return care(listForFound);
                }
            } else {
                //System.out.println(player.setScore(streetFlesh(listForFound)) + " Street Flesh by " + player.getName());
                return streetFlesh(listForFound);
            }
        } else {
            //System.out.println(player.setScore(fleshRoyal(listForFound)) + " Flesh Royal by " + player.getName());
            return fleshRoyal(listForFound);
        }
    }


    class ComandBotty {

        List<String> combListCheating(Player player) {
            List<String> listCheating = new LinkedList<>(player.getCardInHand());
            for (int i = 0; i < 5; i++) {
                listCheating.add(cardDeck.get(i));
            }
            return listCheating;
        }

        void checkWhoWillWin() {
            Player winner = players.get(0);
            for (Player player : players) {
                if (foundCombination(combListCheating(player)) > foundCombination(combListCheating(winner))) {
                    winner = player;
                }
            }
            if (winner.getClass() == Botty.class) {
                ((Botty) winner).winner = true;
            }
        }

        String answer1(Player player) {
            if (((Botty) player).winner) {
                if (players.get(0).equals(player)) {
                    boolean answer = random.nextBoolean();
                    if (answer) {
                        return "raise";
                    } else {
                        return "check";
                    }
                } else {
                    boolean answer = random.nextBoolean();
                    if (answer) {
                        return "raise";
                    } else {
                        return "call";
                    }
                }
            } else {
                if (players.get(0).equals(player)) {
                    return "check";
                } else {
                    boolean answer = random.nextBoolean();
                    if (answer) {
                        return "fold";
                    } else {
                        return "call";
                    }
                }
            }
        }
    }

        void checkToWin() {
            Player winner = players.get(0);
            for (Player player : players) {
                if (foundCombination(listCardForFoundComb(player)) > foundCombination(listCardForFoundComb(winner))) {
                    winner = player;
                }
            }
            for (Player player : players) {
                if (foundCombination(listCardForFoundComb(player)) == foundCombination(listCardForFoundComb(winner))) {
                    draw(player, winner);
                }
            }
            System.out.println(winner.getName() + " is Winner with " + foundCombination(listCardForFoundComb(winner)) + " score");
            winner.setMoney(winner.getMoney() + bank);
            bank = 0;
        }

        void draw(Player winner1, Player winner2) {
            if(bestCard(listCardForFoundComb(winner1)) > bestCard(listCardForFoundComb(winner2))){
                System.out.println(winner1.getName() + " is Winner !");
                winner1.setMoney(winner1.getMoney() + bank);
                bank = 0;
            }else if(bestCard(listCardForFoundComb(winner1)) < bestCard(listCardForFoundComb(winner2))){
                System.out.println(winner2.getName() + " is Winner with");
                winner2.setMoney(winner2.getMoney() + bank);
                bank = 0;
            }else{
                System.out.println("Its a draw :)");
                winner1.setMoney(winner1.getMoney() + (bank/2));
                winner2.setMoney(winner2.getMoney() + (bank/2));
                bank = 0;
            }
        }

        //first phase in game
        void preFlop() {
            System.out.println("Now is preFlop phase!");
            System.out.println("Please bet start lots, take your card and do your steps");
            dealingCards();
            int startLot = 20;
            for (int i = random.nextInt(players.size() - 1); i < players.size(); i++) {
                players.get(i).doLot(startLot);
                startLot -= 10;
                System.out.println(players.get(i).getLot() + " is lot on start from " + players.get(i).getName());
            }
            stepsAllPhase();
        }

        //second phase in game its 3 cards on table and do steps like a first phase
        void flop() {
            System.out.println("Now is flop phase, please look on card and do your steps");
            putCartOnTable(3);
            stepsAllPhase();
        }

        //third phase in game its 4 cards on table and do steps like a first phase
        void turn() {
            System.out.println("Now is turn phase, please look on card and do your steps");
            putCartOnTable(1);
            stepsAllPhase();
        }

        //fourth phase in game its 5 cards on table, do steps like a first phase, and finish loop
        void river() {
            System.out.println("Now is river phase, please look on card and do your steps," +
                    " and in finish we will make a score of points");
            putCartOnTable(1);
            stepsAllPhase();
            System.out.println(cardInTable);
            for (Player player : players)
                System.out.println(player.getCardInHand() + " cards by " + player.getName());
            checkToWin();
        }

        List getCardDeck() {
            return cardDeck;
        }

        public List<Player> getPlayers() {
            return players;
        }

        public void setPlayers(List<Player> players) {
            this.players = players;
        }
    }