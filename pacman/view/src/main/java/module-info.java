/* module-info.java
 * This acts as the manifest for the module.
 */
module com.gr15.pacman.view {
    requires transitive javafx.graphics;
    requires transitive com.gr15.pacman.model;
    exports com.gr15.pacman.view;
}
