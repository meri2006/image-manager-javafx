module com.imagemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.desktop;

    opens com.imagemanager to javafx.fxml;
    opens com.imagemanager.controller to javafx.fxml;
    opens com.imagemanager.model to com.fasterxml.jackson.databind;
    opens com.imagemanager.dao to com.fasterxml.jackson.databind;

    exports com.imagemanager;
    exports com.imagemanager.controller;
    exports com.imagemanager.model;
    exports com.imagemanager.model.filter;
    exports com.imagemanager.model.transform;
    exports com.imagemanager.model.security;
    exports com.imagemanager.dao;
    exports com.imagemanager.util;
}