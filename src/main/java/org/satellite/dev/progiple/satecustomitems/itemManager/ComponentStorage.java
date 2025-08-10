package org.satellite.dev.progiple.satecustomitems.itemManager;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.API.util.utilities.reflection.ClassEntry;
import org.satellite.dev.progiple.satecustomitems.SateCustomItems;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@UtilityClass
public class ComponentStorage {
    @Getter
    private final Set<ItemComponent> realizedComponents = new HashSet<>();

    public void register(ItemComponent itemComponent) {
        realizedComponents.add(itemComponent);
    }

    public void unregister(ItemComponent itemComponent) {
        realizedComponents.remove(itemComponent);
    }

    public <T extends ItemComponent> T getComponent(ItemStack itemStack, Class<T> componentClass) {
        return Utils.find(realizedComponents, c -> c.itemIsComponent(itemStack) &&
                componentClass.isAssignableFrom(c.getClass())).map(componentClass::cast).orElse(null);
    }

    public ItemComponent getComponent(ItemStack itemStack) {
        return Utils.find(realizedComponents, c -> c.itemIsComponent(itemStack)).orElse(null);
    }

    public ItemComponent getComponent(String name) {
        return Utils.find(realizedComponents, c -> c.getClass().getSimpleName().startsWith(name)).orElse(null);
    }

    public <C extends ItemComponent> C getComponent(Class<C> componentClass) {
        for (ItemComponent c : realizedComponents) {
            if (componentClass.isInstance(c)) {
                return componentClass.cast(c);
            }
        }
        return null;
    }

    @SneakyThrows
    public void loadComponents() {
        NotRealized.loadComponents();
        realizedComponents.clear();

        Set<ClassEntry<RealizedComponent>> entries = AnnotationScanner.findAnnotatedClasses(SateCustomItems.getINSTANCE(), RealizedComponent.class,
                "org.satellite.dev.progiple.satecustomitems.itemManager");
        for (ClassEntry<RealizedComponent> entry : entries) {
            if (NotRealized.components.stream().noneMatch(c -> c.isAssignableFrom(entry.getClazz()))) continue;

            ItemComponent itemComponent = (ItemComponent) entry.getClazz().getDeclaredConstructor().newInstance();
            register(itemComponent);
        }
    }

    public Stream<ItemStack> scanInventory(PlayerInventory inventory, ItemComponent itemComponent) {
        Stream<ItemStack> stream;
        if (itemComponent instanceof SlotFilteringComponent sfc) {
            stream = sfc.getEnabledSlots() == null || sfc.getEnabledSlots().length == 0 ?
                    Arrays.stream(inventory.getContents()) :
                    Arrays.stream(sfc.getEnabledSlots()).filter(Objects::nonNull).map(inventory::getItem);
        } else {
            stream = Arrays.stream(inventory.getContents());
        }

        return stream.filter(i -> i != null && !i.getType().isAir() && itemComponent.itemIsComponent(i));
    }

    public <T extends ItemComponent> Stream<T> getRealizedComponents(Class<T> componentClass) {
        return realizedComponents
                .stream()
                .filter(c -> componentClass.isAssignableFrom(c.getClass()))
                .map(componentClass::cast);
    }

    @UtilityClass
    private static class NotRealized {
        private final Set<Class<?>> components = new HashSet<>();

        @SneakyThrows
        public void loadComponents() {
            components.clear();

            Set<ClassEntry<Component>> entries = AnnotationScanner.findAnnotatedClasses(SateCustomItems.getINSTANCE(), Component.class,
                    "org.satellite.dev.progiple.satecustomitems.itemManager");
            for (ClassEntry<Component> entry : entries) {
                components.add(entry.getClazz());
            }
        }
    }
}
