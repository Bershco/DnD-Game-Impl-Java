public class Player extends Unit implements HeroicUnit{
    protected int experience;
    protected int playerLevel;

    public Player() {
        tile = 61;
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

    protected boolean enoughResources() {
        return true;
    }
}
