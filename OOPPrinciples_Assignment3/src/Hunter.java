public class Hunter extends Player {
    private int range;
    private int arrowCount;
    private int tickCount;

    public Hunter(int range){
        this.range = range;
        arrowCount = playerLevel * 10;
        tickCount = 0;
    }

    @Override
    public void onLevelUp() {
        super.onLevelUp();

        arrowCount += playerLevel * 10;
        attackPoints += playerLevel * 2;
        defensePoints += playerLevel;
    }

    @Override
    public void onGameTick() {
        super.onGameTick();

        if(tickCount == 10) {
            arrowCount += playerLevel;
            tickCount = 0;
        }
        else
            tickCount++;
    }

    @Override
    protected boolean enoughResources() {
        return super.enoughResources();
    }

    @Override
    public void castAbility() {
        if(enoughResources()) {
            arrowCount--;
            //TODO: deal damage to the closest enemy within range
        }
        //TODO: provide error message
    }
}
