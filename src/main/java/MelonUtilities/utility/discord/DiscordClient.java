package MelonUtilities.utility.discord;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import club.minnced.discord.webhook.external.JDAWebhookClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.minecraft.core.net.ChatEmotes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DiscordClient {
    public static JDA jda;
    public static JDAWebhookClient webhook;
    public static StandardGuildMessageChannel channel;

    public static boolean init() {
        if (!Data.MainConfig.config.enableDiscordIntegration) {
            return false;
        }

        try {
            JDABuilder builder = JDABuilder.create(
                    Data.MainConfig.config.token,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.MESSAGE_CONTENT,
                    GatewayIntent.GUILD_WEBHOOKS
            );
            builder.addEventListeners(new Listener());

            jda = builder.build().awaitReady();

            return true;
        } catch (Throwable t) {
            MelonUtilities.LOGGER.debug("Unable to start discord bot.", t);
            return false;
        }
    }

    public static @Nullable StandardGuildMessageChannel getChannel() {
        if (jda == null) {
            return null;
        }

        if (channel == null) {
            channel = jda.getChannelById(StandardGuildMessageChannel.class, Data.MainConfig.config.channelID);
        }

        return channel;
    }

    public static @Nullable JDAWebhookClient getWebhook() {
        if (webhook != null) {
            return webhook;
        }

        StandardGuildMessageChannel channel = getChannel();

        if (channel == null) {
            return null;
        }

        Optional<Webhook> optionalWebhook = channel.retrieveWebhooks().complete().stream().filter((it) -> {
            User owner = it.getOwnerAsUser();
            if (owner == null) {
                return false;
            }
            return owner.getId().equals(jda.getSelfUser().getId());
        }).findFirst();

        Webhook webhook = optionalWebhook.orElseGet(() -> channel.createWebhook("BTA DC Integration Chat Link").complete());

        if (webhook == null) {
            return null;
        }

        DiscordClient.webhook = JDAWebhookClient.from(
                webhook
        );

        return DiscordClient.webhook;
    }

    public static class Listener implements EventListener {

        @Override
        public void onEvent(@NotNull GenericEvent event) {
            if (event instanceof MessageReceivedEvent) {
                MessageReceivedEvent message = (MessageReceivedEvent) event;

                if (message.isWebhookMessage() || message.getAuthor().isBot() || message.getAuthor().isSystem()) {
                    return;
                }

                if (!message.isFromGuild()) {
                    return;
                }

                if (!message.getMessage().getChannel().getId().equals(Data.MainConfig.config.channelID)) {
                    return;
                }

                User user = message.getAuthor();

                String username = getDisplayName(user);
                String content = ChatEmotes.process(message.getMessage().getContentStripped());

                DiscordChatRelay.sendToMinecraft(username, content);
            }
        }

        public static String getDisplayName(User user) {
            return user.getName();
        }

        public static String pick(@Nullable String first, @Nullable String second) {
            if (first == null) {
                return second;
            }
            return first;
        }
    }
}

