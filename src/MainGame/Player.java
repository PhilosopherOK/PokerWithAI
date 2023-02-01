package MainGame;

import java.util.List;

public class Player {
    private String name;
    private List <String> cardInHand;
    private int lot;
    private int money;


    public int doLot(int lot){
        if(lot % 10 == 0){
            this.lot += lot;
            money -= lot;
            System.out.println("Your lot is "+ this.lot);
            return 1;
        }else{
            System.out.println("please write lot multiple 10");
            return 0;
        }
    }


    void discardingCards(List <String> cardInHand){
        cardInHand.clear();
    }
    public List<String> getCardInHand() {
        return cardInHand;
    }

    public void setCardInHand(List <String> cardInHand) {
        this.cardInHand = cardInHand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLot() {
        return lot;
    }

    public void setLot(int lot) {
        this.lot = lot;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Player " +name+
                " with "+ money +
                " money";
    }
}
