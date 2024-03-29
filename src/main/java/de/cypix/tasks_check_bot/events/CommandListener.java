package de.cypix.tasks_check_bot.events;

import de.cypix.tasks_check_bot.main.TasksCheckBot;
import de.cypix.tasks_check_bot.sql.SQLManager;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        TasksCheckBot.logger.info("Message Received ["+event.getChannel().getName()+"] "+event.getAuthor().getAsTag()+":> "+event.getMessage().getContentRaw());
        //save everyone in database
        if (SQLManager.isConnected()) {
            SQLManager.insertUser(event.getAuthor());
        }

        if (event.isFromGuild()) {
            if (event.getChannel().getName().equals(TasksCheckBot.getConfigManager().getChannelName())) {
                //delete message
                if (!event.getAuthor().isBot()) {
                    event.getChannel().deleteMessageById(event.getChannel().getLatestMessageId()).queue();
                } else return;

                if (!event.getAuthor().hasPrivateChannel()) event.getAuthor().openPrivateChannel().queue();

                //asking private channel id is absolutely useless!
                for (PrivateChannel privateChannel : TasksCheckBot.getJda().getPrivateChannels()) {
                    if (privateChannel.getUser().getId().equals(event.getAuthor().getId())) {
                        //just saving privateChannelId
                        if (SQLManager.isConnected()) {
                            SQLManager.insertPrivateChannelId(privateChannel.getUser().getIdLong(), privateChannel.getIdLong());
                        }
                        privateChannel.sendMessage("Bitte schreibe nicht in diesen Channel!!!").queue();
                    }
                }
                return;
            }
        }

        String message = event.getMessage().getContentRaw();
        String[] args = message.split(" ");
        if(!event.getAuthor().isBot()){
            if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
                //call command
                if(!TasksCheckBot.getCommandManager().perform(args[0], event.getAuthor(), event.getChannel(), event.getMessage(), args)){
                    event.getChannel().sendMessage("Dieser Befehl ist nicht bekannt!").queue();
                }
            }
        }
    }
}
