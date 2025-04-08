/* module-info.java
 * This acts as the manifest for the module.
 */
module pacman.model {
    requires javafx.controls;   /* Declares a dependency on another module */
    exports pacman.model;       /* exports make specific packages */
}                               /* public to other modules */
