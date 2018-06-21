package bugs.bugs;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

public class NKeys {

    public static Key<Value<Boolean>> MENU_INVENTORY = null;

    public static Key<Value<SkillTreeControllsButton>> SKILLTREE_CONTROLLS = null;

    public NKeys() {
        MENU_INVENTORY = Key.builder()
                .type(new TypeToken<Value<Boolean>>() {	})
                .query(DataQuery.of(".", "ntrpg.inventory.menu"))
                .name("Inventory menu")
                .id("nt-rpg:menu_inventory")
                .build();

        SKILLTREE_CONTROLLS = Key.builder()
                .type(new TypeToken<Value<SkillTreeControllsButton>>() {})
                .id("nt-rpg:skilltree_controlls")
                .query(DataQuery.of(".", "ntrpg.skilltree.controlls"))
                .name("Skilltree controls")
                .build();
    }
}
