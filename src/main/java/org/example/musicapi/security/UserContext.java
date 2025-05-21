package org.example.musicapi.security;

public class UserContext {

    private static final ThreadLocal<Role> currentRole = new ThreadLocal<>();

    public static void setCurrentRole(Role role) {
        currentRole.set(role);
    }

    public static Role getCurrentRole() {
        return currentRole.get();
    }

    public static void clear() {
        currentRole.remove();
    }
}
