public class Player extends Unit implements HeroicUnit{
    protected int experience;
    protected int playerLevel;
    protected Board b = Board.getInstance();

    public Player(String _name, int _healthPool, int _attackPoints, int _defensePoints) {
        super(_name,_healthPool,_attackPoints,_defensePoints);
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
        //TODO: implement visitor pattern to check events for user entered action

        if(experience >= playerLevel * 50)
            onLevelUp();

        b.onGameTick();
    }

    /**
     * This method is partially redundant and is for making sure each sub-class will override it
     * and implement it differently according to their abilities.
     */
    @Override
    public void castAbility() {}

    /**
     * This method is part of the combat system of the game, it is used to attack another unit (primarily enemies)
     * @param target The unit to attack
     */
    @Override
    public void dealDamage(Unit target) {
        super.dealDamage(target);
        if (target.healthAmount <= 0) {
            experience += ((Enemy) target).getExperienceValue();
            Position newPos = target.pos; //TODO: Make sure it updates the currentPosition properly in board
            target.death();
            Tile middleman = b.currentPosition[newPos.x][newPos.y];
            b.currentPosition[newPos.x][newPos.y] = this;
            b.currentPosition[pos.x][pos.y] = middleman;
            pos = newPos;
        }
    }

    /**
     * This method describes the death of the player - simply calling the method gameOverLose() in our singleton board - in order to finish the game
     */
    @Override
    public void death() {
        b.gameOverLose();
    }
    /**
     * This method describes how experience is added to the player
     */
    public void addExp(int exp) {
        experience += exp;
    }

    /**
     * This method dictates if the player has enough resources to use his ability
     * because this method is for a player - which should not be instanced at all, it always returns true
     * @return true
     */
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
