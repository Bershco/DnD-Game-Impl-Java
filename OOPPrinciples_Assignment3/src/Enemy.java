public class Enemy extends Unit{
    public int experienceValue;

    Board b = Board.getInstance();
    @Override
    public void dealDamage(Unit target) {
        super.dealDamage(target);
        if (target.healthAmount <= 0) {
            target.death();
        }
    }

    public void death() {
        b.currentPosition[pos.x][pos.y] = new Empty();
        b.removeEnemy(this);
    }
}
