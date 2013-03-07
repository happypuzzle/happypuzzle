package com.example.happypuzzle;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.BaseTheme;

/**
 * Main layout
 * 
 * <p>
 * Initializes the main application logic and creates the main UI.
 * </p>
 * 
 * @author HappyPuzzle team
 */
public class SplitPanelLayout extends VerticalLayout implements
        Button.ClickListener, Property.ValueChangeListener {

    private final HorizontalSplitPanel horizontalSplitPanel;
    private final VerticalSplitPanel verticalSplitPanel;

    private final Button menuButton;
    private final Button jigsawButton;
    private final Button highscoreButton;

    private Label completionLabel;
    private final TextField nameField;

    private final BestPlayerList bestPark;
    private final BestPlayerList bestVillage;
    private final BestPlayerList bestRiver;
    private final BestPlayerList bestCastle;

    private final AbsoluteLayout rightArea;
    private final AbsoluteLayout welcomeLayout;
    private final GridLayout enterNameLayout;
    private final GridLayout choosePuzzleLayout;
    private final VerticalLayout bestPlayersLayout;

    private AccordionSelection bestPlayersSelection;
    private final ArrayList<BestPlayerList> scoreLists;

    private Jigsaw jigsaw;

    private final Image logoPic;
    private final Image menuLogo;
    private final Image bestplayersLogo;
    private final Image subTitle1;
    private final Image subTitle2;
    private final Image subTitle3;

    private String selection = "Park";
    private String name = "Anonymous";
    private final PuzzleSelection puzzleSelect;

    /**
     * Constructor
     * 
     * @throws ClassNotFoundException
     *             If SQLite driver not installed
     * @throws SQLException
     *             If Problems with database
     */
    public SplitPanelLayout() throws ClassNotFoundException, SQLException {

        setSpacing(true);
        scoreLists = new ArrayList<BestPlayerList>();

        logoPic = SplitPanelLayout.LoadImage("logos/happypuzzle.png");
        menuLogo = SplitPanelLayout.LoadImage("logos/welcome.png");
        bestplayersLogo = SplitPanelLayout.LoadImage("logos/bestplayers.png");
        subTitle1 = SplitPanelLayout.LoadImage("logos/subtitle1.png");
        subTitle2 = SplitPanelLayout.LoadImage("logos/subtitle2.png");
        subTitle3 = SplitPanelLayout.LoadImage("logos/subtitle3.png");

        bestPark = new BestPlayerList("park");
        bestVillage = new BestPlayerList("village");
        bestRiver = new BestPlayerList("river");
        bestCastle = new BestPlayerList("castle");

        scoreLists.add(bestPark);
        scoreLists.add(bestVillage);
        scoreLists.add(bestRiver);
        scoreLists.add(bestCastle);

        bestPlayersSelection = new AccordionSelection(scoreLists);

        jigsaw = new Jigsaw(this);
        nameField = new TextField();
        nameField.setValue(name);
        nameField.addValueChangeListener(this);
        nameField.setImmediate(true);
        nameField.setStyleName("label");

        verticalSplitPanel = new VerticalSplitPanel();
        verticalSplitPanel.setSplitPosition(100, Unit.PIXELS);
        verticalSplitPanel.setLocked(true);
        verticalSplitPanel.setHeight("950px");
        verticalSplitPanel.setWidth("100%");
        verticalSplitPanel.setStyleName("splitpanel");
        verticalSplitPanel.addComponent(logoPic);
        addComponent(verticalSplitPanel);

        enterNameLayout = new GridLayout(1, 2);
        choosePuzzleLayout = new GridLayout(2, 2);
        puzzleSelect = new PuzzleSelection(choosePuzzleLayout);
        puzzleSelect.changeMinipic();

        bestPlayersLayout = new VerticalLayout();

        horizontalSplitPanel = new HorizontalSplitPanel();
        horizontalSplitPanel.setSplitPosition(190, Sizeable.Unit.PIXELS);
        horizontalSplitPanel.setHeight("100%");
        horizontalSplitPanel.setLocked(true);
        horizontalSplitPanel.setStyleName("splitpanel2");

        verticalSplitPanel.addComponent(horizontalSplitPanel);

        menuButton = new Button();
        jigsawButton = new Button();
        highscoreButton = new Button();

        menuButton.setStyleName(BaseTheme.BUTTON_LINK);
        jigsawButton.setStyleName(BaseTheme.BUTTON_LINK);
        highscoreButton.setStyleName(BaseTheme.BUTTON_LINK);

        menuButton.setIcon(LoadFileResource("buttons/buttonMenuDark.png"));
        jigsawButton.setIcon(LoadFileResource("buttons/buttonGame.png"));
        highscoreButton.setIcon(LoadFileResource("buttons/buttonBest.png"));

        menuButton.addClickListener(this);
        jigsawButton.addClickListener(this);
        highscoreButton.addClickListener(this);

        enterNameLayout.addComponent(subTitle1, 0, 0);
        enterNameLayout.addComponent(nameField, 0, 1);
        enterNameLayout.setComponentAlignment(nameField,
                Alignment.MIDDLE_CENTER);
        choosePuzzleLayout.addComponent(subTitle2, 0, 0, 1, 0);
        choosePuzzleLayout.addComponent(puzzleSelect, 0, 1);
        choosePuzzleLayout.setComponentAlignment(puzzleSelect,
                Alignment.MIDDLE_CENTER);

        bestPlayersLayout.addComponent(bestplayersLogo);
        bestPlayersLayout.addComponent(bestPlayersSelection);

        final VerticalLayout leftArea = new VerticalLayout();
        leftArea.addComponent(menuButton);
        leftArea.addComponent(jigsawButton);
        leftArea.addComponent(highscoreButton);
        leftArea.setStyleName("left-area");
        leftArea.addStyleName("measured-from-left");
        leftArea.setWidth("50px");

        horizontalSplitPanel.addComponent(leftArea);

        completionLabel = jigsaw.getCompletionLabel();
        completionLabel.setContentMode(ContentMode.HTML);

        welcomeLayout = new AbsoluteLayout();
        welcomeLayout.addComponent(menuLogo, "top:0px; left:10px");
        welcomeLayout.addComponent(enterNameLayout, "top:100px; left:10px");
        welcomeLayout.addComponent(choosePuzzleLayout, "top:190px; left:10px");
        welcomeLayout.addComponent(subTitle3, "top:350px; left:10px");
        welcomeLayout.setStyleName("right-area");
        welcomeLayout.setSizeFull();

        rightArea = new AbsoluteLayout();
        rightArea.setSizeFull();
        rightArea.addComponent(welcomeLayout, "top:0px; left:10px");
        horizontalSplitPanel.addComponent(rightArea);

    }

    @Override
    public void buttonClick(ClickEvent event) {

        Button pushed_button = event.getButton();
        if (pushed_button == menuButton) {
            menuButton.setIcon(LoadFileResource("buttons/buttonMenuDark.png"));
            jigsawButton.setIcon(LoadFileResource("buttons/buttonGame.png"));
            highscoreButton.setIcon(LoadFileResource("buttons/buttonBest.png"));

            rightArea.removeAllComponents();
            rightArea.addComponent(welcomeLayout);
        }

        if (pushed_button == jigsawButton) {
            menuButton.setIcon(LoadFileResource("buttons/buttonMenu.png"));

            jigsawButton
                    .setIcon(LoadFileResource("buttons/buttonGameDark.png"));
            highscoreButton.setIcon(LoadFileResource("buttons/buttonBest.png"));

            rightArea.removeAllComponents();
            selection = puzzleSelect.getSelection();
            jigsaw = null;
            jigsaw = new Jigsaw(this);
            updateCompleted();
            completionLabel.setContentMode(ContentMode.HTML);
            rightArea.addComponent(completionLabel, "top:600px; left:0px");

            rightArea.addComponent(jigsaw);
        }

        if (pushed_button == highscoreButton) {
            menuButton.setIcon(LoadFileResource("buttons/buttonMenu.png"));
            jigsawButton.setIcon(LoadFileResource("buttons/buttonGame.png"));
            highscoreButton
                    .setIcon(LoadFileResource("buttons/buttonBestDark.png"));

            rightArea.removeAllComponents();
            rightArea.addComponent(bestPlayersLayout);
            bestPlayersLayout.removeAllComponents();
            bestPlayersLayout.addComponent(bestplayersLogo);
            bestPlayersLayout.addComponent(bestPlayersSelection);
        }

    }

    /**
     * Loads an image image without a caption from: VaadinService.getCurrent()
     * .getBaseDirectory().getAbsolutePath() + "/WEB-INF/images/" + path
     * 
     * @param path
     *            Path of the image
     * @return Image fetched without caption
     */
    public static Image LoadImage(String path) {

        return new Image(null, LoadFileResource(path));
    }

    /**
     * Returns FileResource with a path: VaadinService.getCurrent()
     * .getBaseDirectory().getAbsolutePath() + "/WEB-INF/images/" + path
     * 
     * @param path
     *            Path of the image
     * @return FileResource with the given path
     */
    public static FileResource LoadFileResource(String path) {

        return new FileResource(new File(VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath()
                + "/WEB-INF/images/" + path));
    }

    public String getSelection() {
        return selection;
    }

    /**
     * Updates the label located under the jigsaw puzzle with current
     * statistics.
     */
    public void updateCompleted() {
        completionLabel = jigsaw.getCompletionLabel();
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        String temp_name = nameField.getValue();
        temp_name = temp_name.replaceAll(">", "");
        temp_name = temp_name.replaceAll("<", "");
        temp_name = temp_name.replaceAll("&", "");
        if (temp_name.length() > 15) {
            temp_name = temp_name.substring(0, 15);
        }
        name = temp_name;
        Notification.show("Name set to " + nameField.getValue());
    }

    /**
     * Called when submitting a new high score.
     */
    public void submitHighScore() {

        int n = jigsaw.getTotalMoves();
        // "Park", "Village", "River", "Castle"
        // What list to choose
        if (selection == "Park") {
            this.scoreLists.get(0).commitResult(new ResultTuple(name, n));

        }
        if (selection == "Village") {
            this.scoreLists.get(1).commitResult(new ResultTuple(name, n));

        }
        if (selection == "River") {
            this.scoreLists.get(2).commitResult(new ResultTuple(name, n));

        }

        if (selection == "Castle") {
            this.scoreLists.get(3).commitResult(new ResultTuple(name, n));

        }
        bestPlayersSelection = new AccordionSelection(scoreLists);
    }

}