package BusinessLayer;

import BusinessLayer.Board;
import BusinessLayer.HeroicUnit;

public class Player extends Unit implements HeroicUnit {
    protected int experience;
    protected int playerLevel;
    protected Board b = Board.getInstance();

    public Player() {
        tile = '@';
        experience = 0;
        playerLevel = 1;
    }

    protected void onLevelUp() {
        experience -= playerLevel * 50;
        playerLevel++;
        healthPool += playerLevel * 4;
        healthAmount = healthPool;
        attackPoints += playerLevel * 4;
        defensePoints += playerLevel;
    }

    @Override
    public void onGameTick() {
        super.onGameTick();

        if(experience >= playerLevel * 50)
            onLevelUp();
    }

    @Override
    public void castAbility() {

    }

    @Override
    public void dealDamage(Unit target) {
        super.dealDamage(target);
        if (target.healthAmount <= 0) {
            target.death();
            Position newPos = target.pos; //TODO: Make sure it updates the currentPosition properly in board
            Tile middleman = b.currentPosition[newPos.x][newPos.y];
            b.currentPosition[newPos.x][newPos.y] = this;
            b.currentPosition[pos.x][pos.y] = middleman;
            pos = newPos;
        }
    }

    @Override
    public void death() {
        b.gameOver();
    }

    public void addExp(int exp) {
        experience += exp;
    }

    protected boolean enoughResources() {
        return true;
    }

    @Override
    public String description() {
        return super.description() +
            "Experience: " + experience + "\n" +
            "Level: " + playerLevel + "\n";
    }
}
