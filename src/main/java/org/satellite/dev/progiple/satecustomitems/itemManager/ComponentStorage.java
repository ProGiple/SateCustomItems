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

    public <C extends ItemComponent> Stream<C> getComponents(ItemStack itemStack, Class<C> componentClass) {
        return getComponents(componentClass).filter(i -> i.itemIsComponent(itemStack));
    }

    public Stream<ItemComponent> getComponents(ItemStack itemStack) {
        return realizedComponents
                .stream()
                .filter(i -> i.itemIsComponent(itemStack));
    }

    public ItemComponent getComponent(String id) {
        return Utils.find(realizedComponents, c -> c.getId().equalsIgnoreCase(id)).orElse(null);
    }

    public <C extends ItemComponent> C getComponent(ItemStack itemStack, Class<C> componentClass) {
        return getComponents(itemStack, componentClass).findFirst().orElse(null);
    }

    public <C extends ItemComponent> Stream<C> getComponents(Class<C> componentClass) {
        return realizedComponents
                .stream()
                .filter(c -> componentClass.isAssignableFrom(c.getClass()))
                .map(componentClass::cast);
    }

    @SneakyThrows
    public void loadComponents() {
        NotRealized.loadComponents();
        realizedComponents.clear();

        Set<ClassEntry<RealizedComponent>> entries = AnnotationScanner.findAnnotatedClasses(SateCustomItems.getINSTANCE(), RealizedComponent.class, "#.itemManager");
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

            Set<ClassEntry<Component>> entries = AnnotationScanner.findAnnotatedClasses(SateCustomItems.getINSTANCE(), Component.class, "#.itemManager");
            for (ClassEntry<Component> entry : entries) {
                components.add(entry.getClazz());
            }
        }
    }
}
