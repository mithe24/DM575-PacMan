/* module-info.java
 * This acts as the manifest for the module.
 */
module com.gr15.pacman.model {
    requires transitive javafx.controls;
    exports com.gr15.pacman.model;
    exports com.gr15.pacman.model.entities;
}
