package dev.migx3.core.managers;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class StaffChatManager {

    private final Set<UUID> playersInStaffChat = new HashSet<>();

    public void addInChat(UUID uuid) {
        playersInStaffChat.add(uuid);
    }

    public void removeFromChat(UUID uuid) {
        playersInStaffChat.remove(uuid);
    }
}
