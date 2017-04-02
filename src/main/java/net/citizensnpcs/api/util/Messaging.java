package net.citizensnpcs.api.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import net.minecraft.util.text.TextFormatting;

public class Messaging {
    private static class DebugFormatter extends Formatter {
        private final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

        @Override
        public String format(LogRecord rec) {
            Throwable exception = rec.getThrown();

            String out = this.date.format(rec.getMillis());

            out += "[" + rec.getLevel().getName().toUpperCase() + "] ";
            out += rec.getMessage() + '\n';

            if (exception != null) {
                StringWriter writer = new StringWriter();
                exception.printStackTrace(new PrintWriter(writer));

                return out + writer;
            }

            return out;
        }
    }

    public static void configure(File debugFile, boolean debug, String messageColour, String highlightColour) {
        DEBUG = debug;
        MESSAGE_COLOUR = messageColour;
        HIGHLIGHT_COLOUR = highlightColour;

        if (debugFile != null) {
            try {
                FileHandler fh = new FileHandler(debugFile.getAbsolutePath(), true);
                fh.setFormatter(new DebugFormatter());
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void debug(Object... msg) {
        if (isDebugging()) {
            LOGGER.info(SPACE.join(msg));
        }
    }

    public static boolean isDebugging() {
        return DEBUG;
    }

    public static void log(Object... msg) {
        log(Level.INFO, msg);
    }

    public static void logTr(String key, Object... msg) {
        log(Level.INFO, Translator.translate(key, msg));
    }

    private static String prettify(String message) {
        String trimmed = message.trim();
        String messageColour = Colorizer.parseColors(MESSAGE_COLOUR);
        if (!trimmed.isEmpty()) {
            if (trimmed.charAt(0) == '\u00A7') {
                TextFormatting test = TextSerializers.LEGACY_FORMATTING_CODE(trimmed.substring(1, 2));
                if (test == null) {
                    message = messageColour + message;
                } else
                    messageColour = test.toString();
            } else
                message = messageColour + message;
        }
        message = message.replace("[[", Colorizer.parseColors(HIGHLIGHT_COLOUR));
        message = message.replace("]]", messageColour);
        return message;
    }

    public static void send(CommandSource sender, Object... msg) {
        sendMessageTo(sender, SPACE.join(msg));
    }

    public static void sendError(CommandSource sender, Object... msg) {
        send(sender, TextFormatting.RED.toString() + SPACE.join(msg));
    }

    public static void sendErrorTr(CommandSource sender, String key, Object... msg) {
        sendMessageTo(sender, TextFormatting.RED + Translator.translate(key, msg));
    }

    private static void sendMessageTo(CommandSource sender, String rawMessage) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            rawMessage = rawMessage.replace("<player>", player.getName());
            rawMessage = rawMessage.replace("<world>", player.getWorld().getName());
        }
        rawMessage = Colorizer.parseColors(rawMessage);
        for (String message : CHAT_NEWLINE_SPLITTER.split(rawMessage)) {
            sender.sendMessage(prettify(message));
        }
    }

    public static void sendTr(CommandSource sender, String key, Object... msg) {
        sendMessageTo(sender, Translator.translate(key, msg));
    }

    public static void sendWithNPC(CommandSource sender, Object msg, NPC npc) {
        String send = msg.toString();
        send = send.replace("<owner>", npc.getTrait(Owner.class).getOwner());
        send = send.replace("<npc>", npc.getName());
        send = send.replace("<id>", Integer.toString(npc.getId()));
        send(sender, send);
    }

    public static void severe(Object... messages) {
        log(Level.SEVERE, messages);
    }

    public static void severeTr(String key, Object... messages) {
        log(Level.SEVERE, Translator.translate(key, messages));
    }

    public static String tr(String key, Object... messages) {
        return prettify(Translator.translate(key, messages));
    }

    public static String tryTranslate(Object possible) {
        if (possible == null)
            return "";
        String message = possible.toString();
        int count = 0;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == '.')
                count++;
        }
        return count >= 2 ? tr(message) : message;
    }

    private static final Pattern CHAT_NEWLINE = Pattern.compile("<br>|<n>|\\n", Pattern.MULTILINE);

    private static final Splitter CHAT_NEWLINE_SPLITTER = Splitter.on(CHAT_NEWLINE);
    private static boolean DEBUG = false;
    private static String HIGHLIGHT_COLOUR = TextFormatting.YELLOW.toString();
    private static org.slf4j.Logger LOGGER = CitizensAPI.getPlugin().getLogger();
    private static String MESSAGE_COLOUR = TextFormatting.GREEN.toString();
    private static final Joiner SPACE = Joiner.on(" ").useForNull("null");
}
