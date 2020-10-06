package rip.skyland.soup.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.phase.util.CC;

public class MessageBuilder {
    private String text;
    private TextComponent textComponent;

    public MessageBuilder(String text) {
        this.text = text;
        this.textComponent = new TextComponent();
    }

    public MessageBuilder hover(String hoverText) {
        this.textComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder(CC.translate(hoverText))).create()));
        return this;
    }

    public MessageBuilder click(ClickEvent clickEvent) {
        this.textComponent.setClickEvent(clickEvent);
        return this;
    }

    public void send(CommandSender player) {
        this.textComponent.setText(CC.translate(this.text));
        if (player instanceof Player) {
            ((Player)player).spigot().sendMessage(this.textComponent);
        } else {
            player.sendMessage(this.text);
        }

    }

    public String getText() {
        return this.text;
    }

    public TextComponent getTextComponent() {
        return this.textComponent;
    }
}
