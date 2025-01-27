package MelonUtilities.utility.discord;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.MinecraftServer;

import java.time.Instant;

public class DiscordChatRelay {

    private static final JDAWebhookClient webhookClient = DiscordClient.getWebhook();

    public static void sendToMinecraft(String author, String message) {
        MinecraftServer server = MinecraftServer.getInstance();

		author = Character.toUpperCase(author.charAt(0)) + author.substring(1);

        message = TextFormatting.GRAY + "[" + TextFormatting.PURPLE + "♦" + TextFormatting.GRAY + "] <" + TextFormatting.LIGHT_GRAY + author + TextFormatting.GRAY + "> " + TextFormatting.RESET + message;
		MelonUtilities.info(message);
        String[] lines = message.split("\n");
        for (String chatMessage : lines) {
            server.playerList.sendEncryptedChatToAllPlayers(chatMessage);
        }
    }

    public static void sendToDiscord(String author, String message) {
        if (webhookClient == null) return;
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(author);
        builder.setAvatarUrl("https://www.mc-heads.net/head/" + author + "/" + "150");
        builder.setContent(message);
        webhookClient.send(builder.build());
    }

    public static void sendJoinLeaveMessage(String username, boolean joined) {
        if (webhookClient == null) return;
        String avatarUrl = "https://www.mc-heads.net/head/" + username;
        String joinLeaveText = username + (joined ? " joined" : " left") + " the server";
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(joined ? 0x00FF00 : 0xFF0000)
                .setAuthor(new WebhookEmbed.EmbedAuthor(joinLeaveText, avatarUrl, null))
                .build();
        sendMessage(null, embed);
    }

    public static void sendDeathMessage(String deathMessage) {
        if (webhookClient == null) return;
        String avatarUrl = "https://i.imgur.com/l1EJ6fx.png";
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0xFF0000)
                .setAuthor(new WebhookEmbed.EmbedAuthor(deathMessage, avatarUrl, null))
                .build();
        sendMessage(null, embed);
    }

    public static void sendServerStartMessage() {
        if (webhookClient == null) return;
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0x4ae485)
                .setAuthor(new WebhookEmbed.EmbedAuthor("✅ Server started!", null, null))
                .setTimestamp(Instant.now())
                .build();
        sendMessage(null, embed);
    }

    public static void sendServerStoppedMessage() {
        if (webhookClient == null) return;
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0xf92f60)
                .setAuthor(new WebhookEmbed.EmbedAuthor("❌ Server stopped!", null, null))
                .setTimestamp(Instant.now())
                .build();
        sendMessage(null, embed);
    }

    public static void sendServerSleepMessage() {
        if (webhookClient == null) return;
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0x222d5a)
                .setAuthor(new WebhookEmbed.EmbedAuthor("The Night was Skipped", "https://i.imgur.com/R1e1sJS.png", null))
                .build();
        sendMessage(null, embed);
    }
    public static void sendMessage(String content, WebhookEmbed embed) {
        if (webhookClient == null) return;
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(Data.MainConfig.config.serverName);
        builder.setAvatarUrl(Data.MainConfig.config.serverPFPURL);
        if (content != null && !content.isEmpty()) {
            builder.setContent(content);
        }
        if (embed != null) {
            builder.addEmbeds(embed);
        }
        webhookClient.send(builder.build());
    }
}
