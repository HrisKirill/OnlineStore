package com.khrystoforov.onlinestore.user.model;

public enum Role {
    USER, MANAGER, ADMIN;

    Role() {
    }

    public static Role getRole(String name) {
        for (Role role : Role.values()) {
            if (name.equalsIgnoreCase(role.name())) {
                return role;
            }
        }

        return USER;
    }
}
