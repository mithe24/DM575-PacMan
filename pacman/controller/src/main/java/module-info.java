/* module-info.java
 * This acts as the manifest for the module.
 */
module com.gr15.pacman.controller {
    requires transitive com.gr15.pacman.view;
    exports com.gr15.pacman.controller;
}
