package bugs.bugs;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.filter.type.Include;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.AcceptsItems;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "bugs",
        name = "Bugs"
)
public class Bugs {

    @Inject
    private Logger logger;

    @Inject
    PluginContainer plugin;

    @Listener
    public void preinit(GamePreInitializationEvent e) {
        new NKeys();
        DataRegistration.<MenuInventoryData, MenuInventoryData.Immutable>builder()
                .manipulatorId("menu_inventory")
                .dataName("Menu Item")
                .dataClass(MenuInventoryData.class)
                .immutableClass(MenuInventoryData.Immutable.class)
                .builder(new MenuInventoryData.Builder())
                .buildAndRegister(plugin);

        DataRegistration.<SkillTreeInventoryViewControllsData, SkillTreeInventoryViewControllsData.Immutable>builder()
                .manipulatorId("skilltree_controlls")
                .dataName("SkillTree Controll Buttons")
                .dataClass(SkillTreeInventoryViewControllsData.class)
                .immutableClass(SkillTreeInventoryViewControllsData.Immutable.class)
                .builder(new SkillTreeInventoryViewControllsData.Builder())
                .buildAndRegister(plugin);
    }

    @Listener
    @Include({
            ClickInventoryEvent.Primary.class,
            ClickInventoryEvent.Secondary.class,
            ClickInventoryEvent.Creative.class,
            ClickInventoryEvent.Middle.class,
            ClickInventoryEvent.Shift.class
    })
    public void onClick(ClickInventoryEvent event, @Root Player player) {
        List<SlotTransaction> transactions = event.getTransactions();
        for (SlotTransaction transaction : transactions) {
            Optional<SlotIndex> inventoryProperty = transaction.getSlot().getInventoryProperty(SlotIndex.class);
            if (inventoryProperty.isPresent()) {
                boolean cancel = true;
                if (cancel)
                    event.setCancelled(cancel);
            }
        }
    }

    @Listener(order = Order.FIRST)
    public void onBreak(ChangeBlockEvent.Break e, @Root Player player) {
        Inventory i = createSkillTreeInventoryViewTemplate();
        createSkillTreeView(player, i);
        player.openInventory(i);
    }


    public void onOptionSelect(ClickInventoryEvent event, Player player) {
        System.out.println("===============================================");
        System.out.println("(" + event.getTransactions().size() + ") < slotTransactions().size()");
        System.out.println("===============================================");
        Iterator<SlotTransaction> iterator = event.getTransactions().iterator();
        event.getTransactions().stream().map(SlotTransaction::toString).forEach(System.out::println);
        if (iterator.hasNext()) {
            SlotTransaction t = iterator.next();
            if (t.getOriginal().get(NKeys.SKILLTREE_CONTROLLS).isPresent()) {
                SkillTreeControllsButton command = t.getOriginal().get(NKeys.SKILLTREE_CONTROLLS).get();
                switch (command) {
                    case NORTH:
                        Sponge.getScheduler().createTaskBuilder()
                                .execute(() -> moveSkillTreeMenu(player))
                                .submit(this);
                        break;
                    case SOUTH:
                        Sponge.getScheduler().createTaskBuilder()
                                .execute(() -> moveSkillTreeMenu(player))
                                .submit(this);
                        break;
                    case WEST:
                        Sponge.getScheduler().createTaskBuilder()
                                .execute(() -> moveSkillTreeMenu(player))
                                .submit(this);
                        break;
                    case EAST:
                        Sponge.getScheduler().createTaskBuilder()
                                .execute(() -> moveSkillTreeMenu(player))
                                .submit(this);
                        break;
                    case MODE:
                        Sponge.getScheduler().createTaskBuilder()
                                .execute(() -> moveSkillTreeMenu(player))
                                .submit(this);
                        break;
                    default:
                            Sponge.getScheduler().createTaskBuilder()
                                    .execute(() -> moveSkillTreeMenu(player))
                                    .submit(this);


                }
            }
        }
    }


    public void moveSkillTreeMenu(Player player) {
        Optional<Container> openInventory = player.getOpenInventory();
        if (openInventory.isPresent()) {
            createSkillTreeView(player, openInventory.get().query(GridInventory.class).first());
        }
    }

    private void createSkillTreeView(Player player, Inventory skillTreeInventoryViewTemplate) {

        ItemStack md = interactiveModeToitemStack();
        skillTreeInventoryViewTemplate.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(8,1))).clear();
        skillTreeInventoryViewTemplate.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(8,1))).offer(md);


        for (int k = -3; k <= 3; k++) { //x
            for (int l = -3; l <= 3; l++) { //y
                Inventory query = skillTreeInventoryViewTemplate
                        .query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(l + 3, k + 3)));
                query.clear();
                ItemStack itemStack = null;
                if (itemStack == null) {
                    itemStack = ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1);
                    itemStack.offer(Keys.DISPLAY_NAME, Text.EMPTY);
                    itemStack.offer(Keys.DYE_COLOR, DyeColors.GRAY);
                    itemStack.offer(Keys.HIDE_MISCELLANEOUS, true);
                    itemStack.offer(new MenuInventoryData(true));
                }
                query.offer(itemStack);
            }
        }
    }

    public Inventory createSkillTreeInventoryViewTemplate() {
        Inventory i = Inventory.builder()
                .of(InventoryArchetypes.DOUBLE_CHEST)
                .property(AcceptsItems.of(Collections.EMPTY_LIST))
                .listener(ClickInventoryEvent.Primary.class, event -> new Bugs().onOptionSelect(event, (Player) event.getCause().root()))
                .listener(ClickInventoryEvent.Secondary.class, event -> new Bugs().onOptionSelect(event, (Player) event.getCause().root()))
                .build(plugin);

        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(7, 0))).offer(unclickableInterface());
        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(7, 1))).offer(unclickableInterface());
        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(7, 2))).offer(unclickableInterface());
        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(7, 3))).offer(unclickableInterface());
        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(7, 4))).offer(unclickableInterface());
        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(7, 5))).offer(unclickableInterface());


        ItemStack md = interactiveModeToitemStack();
        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(8, 1))).set(md);

        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(8, 2))).offer(createControlls(SkillTreeControllsButton.NORTH));
        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(8, 3))).offer(createControlls(SkillTreeControllsButton.SOUTH));
        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(8, 4))).offer(createControlls(SkillTreeControllsButton.WEST));
        i.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(8, 5))).offer(createControlls(SkillTreeControllsButton.EAST));

        return i;
    }

    public static ItemStack unclickableInterface() {
        ItemStack of = ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1);
        of.offer(new MenuInventoryData(true));
        of.offer(Keys.DYE_COLOR, DyeColors.YELLOW);
        of.offer(Keys.DISPLAY_NAME, Text.EMPTY);
        return of;
    }

    public static ItemStack interactiveModeToitemStack() {
        ItemStack md = ItemStack.of(ItemTypes.WRITTEN_BOOK, 1);
        md.offer(new SkillTreeInventoryViewControllsData(SkillTreeControllsButton.MODE));
        md.offer(new MenuInventoryData(true));
        return md;
    }

    public static ItemStack createControlls(SkillTreeControllsButton button) {
        ItemStack itemStack = ItemStack.of(ItemTypes.DIAMOND_HOE, 1);
        itemStack.offer(new SkillTreeInventoryViewControllsData(button));
        itemStack.offer(new MenuInventoryData(true));
        return itemStack;
    }
}
