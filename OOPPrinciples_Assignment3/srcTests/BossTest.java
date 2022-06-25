import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BossTest {
    final int abilityFreq = 3;
    Boss testBossNoVisionRange = new Boss("TestBoss",'T',100,69,420,0,42069, abilityFreq);
    Boss testBossOnlyVisionRange = new Boss("TestBoss",'T',1,69,420,Integer.MAX_VALUE,0,abilityFreq);
    Board b = Board.getInstance();
    Player p = b.getPlayer();
    int pHPBefore = p.healthAmount;
    @BeforeEach
    void setUp() {
    }

    @Test
    void moveProperly() {
        for (int i = 0; i <= abilityFreq ; i++) { //TODO: check if it should be <= or <
            int ct = testBossNoVisionRange.getCombatTicks();
            assertEquals(ct,i);
        }
        assertNotEquals(pHPBefore,p.healthAmount);
    }

    @Test
    void castAbility() {
        assertNotEquals(pHPBefore,p.healthAmount);
    }

    @Test
    void onGameTick() {
        //TODO: check if onGameTick() test is even needed (method only calls other methods)
        
    }

    @Test
    void description() {
    }
}