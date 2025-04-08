/* module-info.java
 * This acts as the manifest for the module.
 */
module pacman.controller {
    requires javafx.controls;   /* Declares a dependency on another module */
    requires pacman.model;      /* Should depenend on model, */
    requires pacman.view;       /* and view */
    exports pacman.controller;  /* exports make specific packages */
}                               /* public to other modules */
