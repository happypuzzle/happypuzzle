package com.example.happypuzzle;

import java.util.ArrayList;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;

/**
 * Creates a high score list UI.
 * 
 * @author Team HappyPuzzle
 * 
 */
@SuppressWarnings("serial")
public class AccordionSelection extends HorizontalLayout implements
        Accordion.SelectedTabChangeListener {

    private static final ThemeResource icon1 = new ThemeResource(
            "icons/iconpark.png");
    private static final ThemeResource icon2 = new ThemeResource(
            "icons/iconvillage.png");
    private static final ThemeResource icon3 = new ThemeResource(
            "icons/iconriver.png");
    private static final ThemeResource icon4 = new ThemeResource(
            "icons/iconcastle.png");

    private final Accordion a;
    private final ArrayList<BestPlayerList> scoreLists;

    private String overallPark = "";
    private String overallVillage = "";
    private String overallRiver = "";
    private String overallCastle = "";

    Label l1;
    Label l2;
    Label l3;
    Label l4;
    Label l5;

    public TextField name;

    /**
     * Constructor
     * 
     * @param scoreLists
     *            ArrayList<BestPlayerList> should contain a BestPlayerList for
     *            each Puzzle
     */
    public AccordionSelection(ArrayList<BestPlayerList> scoreLists) {
        // this.name = name;

        this.scoreLists = scoreLists;
        setSpacing(true);

        overallPark = scoreLists.get(0).getString();
        overallVillage = scoreLists.get(1).getString();
        overallRiver = scoreLists.get(2).getString();
        overallCastle = scoreLists.get(3).getString();

        Label l2 = new Label(overallPark);
        Label l3 = new Label(overallVillage);
        Label l4 = new Label(overallRiver);
        Label l5 = new Label(overallCastle);

        l2.setContentMode(ContentMode.HTML);
        l3.setContentMode(ContentMode.HTML);
        l4.setContentMode(ContentMode.HTML);
        l5.setContentMode(ContentMode.HTML);

        a = new Accordion();
        a.setHeight("300px");
        a.setWidth("400px");

        a.addTab(l2, "Best of the park", icon1);
        a.addTab(l3, "Legends of the village", icon2);
        a.addTab(l4, "Masters of the river", icon3);
        a.addTab(l5, "Royalty of the castle", icon4);
        a.addSelectedTabChangeListener(this);
        addComponent(a);

    }

    public void selectedTabChange(SelectedTabChangeEvent event) {
        TabSheet tabsheet = event.getTabSheet();
        Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
        if (tab != null) {
            Notification.show("Selected tab: " + tab.getCaption());
        }
    }

}