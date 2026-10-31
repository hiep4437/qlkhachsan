module hotel {
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;

    exports main;
    exports controller;
    exports dao;
    exports model;
    exports service;
    exports view.view;
    exports view.dialog;
}
