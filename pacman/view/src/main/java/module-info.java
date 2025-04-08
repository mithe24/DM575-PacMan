/* module-info.java
 * This acts as the manifest for the module.
 */
module pacman.view {
    requires javafx.controls;   /* JavaFX dependencies */
    requires javafx.graphics;
    requires pacman.model;
    exports pacman.view;        /* exports make specific packages */
}                               /* public to other modules */
