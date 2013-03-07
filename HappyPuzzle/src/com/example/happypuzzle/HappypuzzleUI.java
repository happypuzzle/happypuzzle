package com.example.happypuzzle;

import java.sql.SQLException;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * Main UI class
 * 
 * @author Team HappyPuzzle
 */
@Theme("happypuzzletheme")
@SuppressWarnings("serial")
public class HappypuzzleUI extends UI {
    SplitPanelLayout layout;

    @Override
    protected void init(VaadinRequest request) {

        try {
            layout = new SplitPanelLayout();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        setContent(layout);

    }

}