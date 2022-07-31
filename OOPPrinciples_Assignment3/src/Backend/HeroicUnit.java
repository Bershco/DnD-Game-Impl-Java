package Backend;

import Backend.Tiles.Unit;

import java.util.List;

public interface HeroicUnit {
    /**
     * This method (when implemented) describes the special ability a heroic unit is able to use
     */
    void castAbility(List<? extends Unit> enemies);
}