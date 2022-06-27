package Backend;

import java.util.List;

public interface HeroicUnit {
    /**
     * This method (when implemented) describes the special ability a heroic unit is able to use
     */
    void castAbility(List<Unit> enemies);
}