/* module-info.java
 * This acts as the manifest for the module.
 */
module com.gr15.pacman.controller {
    requires javafx.controls;
    requires javafx.graphics;
    requires com.gr15.pacman.view;
    requires com.gr15.pacman.model;

    exports com.gr15.pacman.controller;
    exports com.gr15.pacman.controller.screen;
}
