package com.example.lab11.Database;

import android.provider.BaseColumns;

public final class UserContact {


    private UserContact(){}

    public static class UserTable implements BaseColumns {

        public static final String TABLE_NAME1= "users";

        public static final String COL_2="username";
        public static final String COL_3="password";

    }
}
