/* module-info.java
 * This acts as the manifest for the module.
 */
module com.gr15.pacman.model {
    requires transitive javafx.controls;
    requires org.json;
    exports com.gr15.pacman.model;
    exports com.gr15.pacman.model.entities;
}
