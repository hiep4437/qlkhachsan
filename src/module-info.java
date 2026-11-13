module hotel {
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires java.logging;

    exports main;
    exports controller;
    exports dao;
    exports model;
    exports service;
    exports view;
    exports view.view;
    exports view.dialog;
}
