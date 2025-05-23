package com.gr15.pacman.controller.screen;

import com.gr15.pacman.view.ViewManager;
import com.gr15.pacman.view.ViewManager.ViewKeys;
import com.gr15.pacman.view.screen.YouWonView;

import javafx.event.ActionEvent;

/**
 * YouWonController
 */
public class YouWonController {

    private YouWonView youWonView;
    private ViewManager viewManager;

    public YouWonController(YouWonView youWonView, ViewManager viewManager) {
        if (youWonView == null) {
            throw new IllegalArgumentException("youWonView must not be null");
        }
        if (viewManager == null) {
            throw new IllegalArgumentException("viewManager must not be null");
        }

        this.youWonView = youWonView;
        this.viewManager = viewManager;

        youWonView.getMainMenuButton().setOnAction(this::mainMenu);
    }

    private void mainMenu(ActionEvent event) {
        viewManager.showView(ViewKeys.MAIN_MENU_VIEW);
    }
}
