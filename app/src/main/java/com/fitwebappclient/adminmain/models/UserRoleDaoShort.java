package com.fitwebappclient.adminmain.models;

public enum UserRoleDaoShort {
    Administrator("Administrator"),
    User("User");

    private String roleName;
    UserRoleDaoShort(String roleName) {
        this.roleName = roleName;
    }

    public static String fromId(int index) {
        switch(index) {
            case 1:
                return Administrator.roleName;
            case 2:
                return User.roleName;
        }
        return null;
    }


    public static int fromName(String roleName) {
        switch(roleName) {
            case "User":
                return 2;
            case "Administrator":
                return 1;
        }
        return 2;
    }
}
