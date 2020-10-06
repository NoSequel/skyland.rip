package rip.skyland.soup.commands;

import org.bukkit.command.CommandSender;
import rip.skyland.core.util.CC;
import rip.skyland.core.util.WoolColor;
import rip.skyland.core.util.command.annotation.Command;
import rip.skyland.core.util.menu.Button;
import rip.skyland.core.util.menu.Menu;
import rip.skyland.soup.SoupPlugin;
import rip.skyland.soup.profiles.SoupProfile;
import rip.skyland.soup.scoreboard.style.Style;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;
import java.util.stream.IntStream;

public class StyleCommand {

    @Command(names={"style", "theme", "themes", "thema", "themas"})
    public void execute(Player player) {
        getMenu().openMenu(player);

    }

    private Menu getMenu() {
        return new Menu() {
            @Override
            public String getTitle(Player player) {
                return "Select your Style";
            }

            @Override
            public Map<Integer, Button> getButtons(Player player) {
                Map<Integer, Button> buttons = new HashMap<>();

                IntStream.range(0, Style.getStyles().size()).forEach(i ->
                    buttons.put(i, getButton(player, Style.getStyles().get(i))));

                buttons.put(8, getButton(player, SoupProfile.getByPlayer(player).getCustomStyle()));
                return buttons;
            }
        };
    }




    private Menu getMenu2() {
        return new Menu() {
            @Override
            public String getTitle(Player player) {
                return "Change your custom Style";
            }

            @Override
            public Map<Integer, Button> getButtons(Player player) {
                Map<Integer, Button> buttons = new HashMap<>();
                Style style = SoupProfile.getByPlayer(player).getCustomStyle();

                buttons.put(0, getOptionButton("Kills", style.isKills(), player));
                buttons.put(9, getOptionButton("Deaths", style.isDeaths(), player));
                buttons.put(9+9, getOptionButton("Killstreak", style.isKillstreak(), player));
                buttons.put(9+9+9, getOptionButton("Highest Killstreak", style.isHighestKillstreak(), player));
                buttons.put(9+9+9+9, getOptionButton("Credits", style.isCredits(), player));
                buttons.put(9+9+9+9+9, getOptionButton("KDR", style.isKdr(), player));

                buttons.put(1, getOptionButton("Modern", style.isModern(), player));

                buttons.put(7, getColorButton("Primary Color", style.getPrimaryColor(), player));
                buttons.put(7+9, getColorButton("Secondary Color", style.getSecondaryColor(), player));
                buttons.put(7+9+9, getColorButton("Event Primary Color", style.getPrimaryColorEvents(), player));
                buttons.put(7+9+9+9, getColorButton("Event Secondary Color", style.getSecondaryColorEvents(), player));

                buttons.put(53, new Button() {
                    @Override
                    public String getName(Player player) {
                        return CC.translate("&aConfirm");
                    }

                    @Override
                    public List<String> getDescription(Player player) {
                        return Collections.emptyList();
                    }

                    @Override
                    public Material getMaterial(Player player) {
                        return Material.WOOL;
                    }

                    @Override
                    public byte getDamageValue(Player player) {
                        return (byte) 4;
                    }

                    @Override
                    public void clicked(Player player, int i, ClickType clickType, int i1) {
                        SoupProfile.getByPlayer(player).setStyle(SoupProfile.getByPlayer(player).getCustomStyle());
                        player.sendMessage(CC.translate("&aYou have successfully edited your custom style."));
                    }
                });
                return buttons;
            }
        };
    }

    private Button getButton(Player player, Style style) {
        return new Button() {
            SoupProfile profile = SoupProfile.getByPlayer(player);

            @Override
            public String getName(Player player) {
                return style.getPrimaryColor() + style.getName() + CC.translate(profile.getStyle().equals(style) ? " &7(Selected)" : " &7(Select)");
            }

            @Override
            public List<String> getDescription(Player player) {
                return CC.translate(Arrays.asList(
                        "",
                        style.getPrimaryColor().toString() + CC.BOLD.toString() + CC.UNDERLINE + style.getName() + " Style",
                        "",
                        style.getPrimaryColor() + "Kills: " + style.getSecondaryColor() + style.isKills(),
                        style.getPrimaryColor() + "Deaths: " + style.getSecondaryColor() + style.isKills(),
                        style.getPrimaryColor() + "Killstreak: " + style.getSecondaryColor() + style.isKillstreak(),
                        style.getPrimaryColor() + "Highest Killstreak: " + style.getSecondaryColor() + style.isHighestKillstreak(),
                        style.getPrimaryColor() + "Credits: " + style.getSecondaryColor() + style.isCredits(),
                        style.getPrimaryColor() + "KDR: " + style.getSecondaryColor() + style.isKdr(),
                        "",
                        style.getSecondaryColor() + "Primary Color: " + style.getPrimaryColor() + style.getPrimaryColor().name(),
                        style.getSecondaryColor() + "Secondary Color: " + style.getSecondaryColor() + style.getSecondaryColor().name(),
                        "",
                        "" + (style.getAuthor() != null ? "&7Created by " + style.getAuthor() : ""),
                        style.equals(profile.getCustomStyle()) ? "&6&lCLICK TO EDIT" : (profile.getStyle().equals(style) ? "&6&lSELECTED" : "&a&lSELECT")

                ));
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.PAPER;
            }

            @Override
            public byte getDamageValue(Player player) {
                return 0;
            }

            @Override
            public void clicked(Player player, int i, ClickType clickType, int i1) {

                if (style.equals(profile.getCustomStyle())) {
                    player.closeInventory();

                    Bukkit.getScheduler().scheduleSyncDelayedTask(SoupPlugin.getInstance(), () -> getMenu2().openMenu(player), 5L);
                    getMenu2().openMenu(player);
                    return;
                }
                profile.setStyle(style);
                player.sendMessage(CC.translate("&aYou have selected the " + style.getPrimaryColor() + style.getName() + " &astyle."));
                player.closeInventory();
            }
        };
    }
    private Button getOptionButton(String name, boolean value, Player player) {
        return new Button() {
            SoupProfile profile = SoupProfile.getByPlayer(player);
            Style style = profile.getCustomStyle();

            @Override
            public String getName(Player player) {
                return style.getPrimaryColor() + name + CC.translate("&7(Click to change)");
            }

            @Override
            public List<String> getDescription(Player player) {
                return CC.translate(Arrays.asList(
                        "",
                        style.getPrimaryColor().toString() + CC.BOLD.toString() + CC.UNDERLINE + style.getName(),
                        style.getPrimaryColor() + "Value: " + style.getSecondaryColor() + value,

                        "",
                        "&6&lCLICK TO EDIT"

                ));
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.TRIPWIRE_HOOK;
            }

            @Override
            public byte getDamageValue(Player player) {
                return 0;
            }

            @Override
            public void clicked(Player player, int i, ClickType clickType, int i1) {
                switch(name.toLowerCase()) {
                    case "kills":
                        style.setKills(!style.isKills());
                        break;

                    case "deaths":
                        style.setDeaths(!style.isDeaths());
                        break;

                    case "killstreak":
                        style.setKillstreak(!style.isKillstreak());
                        break;

                    case "highest killstreak":
                        style.setHighestKillstreak(!style.isHighestKillstreak());
                        break;

                    case "credits":
                        style.setCredits(!style.isCredits());
                        break;

                    case "kdr":
                        style.setKdr(!style.isKdr());
                        break;

                    case "modern":
                        style.setModern(!style.isModern());
                        break;
                }
            }
        };
    }
    private Button getColorButton(String name, CC value, Player player) {
        return new Button() {
            SoupProfile profile = SoupProfile.getByPlayer(player);
            Style style = profile.getCustomStyle();

            @Override
            public String getName(Player player) {
                return style.getPrimaryColor() + name + CC.GRAY + " (Click to change)";
            }

            @Override
            public List<String> getDescription(Player player) {
                return Arrays.asList(
                        "",
                        style.getPrimaryColor().toString() + CC.BOLD.toString() + CC.UNDERLINE + style.getName(),
                        style.getPrimaryColor() + "Color: " + value + value.name(),

                        "",
                        CC.translate("&6&lCLICK TO EDIT")

                );
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.WOOL;
            }

            @Override
            public byte getDamageValue(Player player) {
                return (byte) WoolColor.getWoolColor(value);
            }

            @Override
            public void clicked(Player player, int i, ClickType clickType, int i1) {
                switch(name.toLowerCase()) {
                    case "primary color":
                        style.setPrimaryColor(value.nextCC());
                        break;

                    case "secondary color":
                        style.setSecondaryColor(value.nextCC());
                        break;

                    case "event primary color":
                        style.setPrimaryColorEvents(value.nextCC());
                        break;

                    case "event secondary color":
                        style.setSecondaryColorEvents(value.nextCC());
                        break;
                }
            }
        };
    }

}
