package com.gelato.gelato.cores.database;

import com.gelato.gelato.cores.AppController;

/**
 * Created by Mathpresso3 on 2015-08-05.
 */
public class DatabaseManager {
    // Singleton!
    private final String TAG = "DatabaseManager";

    DatabaseHelper mDatabaseHelper;

    public DatabaseManager() {
        mDatabaseHelper = AppController.getInstance().getDatabaseHelper();
    }
}
