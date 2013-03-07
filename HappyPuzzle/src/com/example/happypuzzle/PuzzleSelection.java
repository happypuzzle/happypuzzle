package com.example.happypuzzle;

import java.util.Arrays;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * For selecting a puzzle using OptionGroup.
 * 
 * @author Team HappyPuzzle
 */
@SuppressWarnings("serial")
public class PuzzleSelection extends VerticalLayout implements
        Property.ValueChangeListener {

    private static final List<String> puzzles = Arrays.asList(new String[] {
            "Park", "Village", "River", "Castle" });
    private String puzzleSelection = "Park";

    private final GridLayout optionsLayout;

    private final Panel miniPicPanel;

    /**
     * Constructor
     * 
     * @param optionsLayout
     *            GridLayout where PuzzleSelection is created to.
     */
    public PuzzleSelection(GridLayout optionsLayout) {
        setSpacing(true);
        this.optionsLayout = optionsLayout;
        // 'Shorthand' constructor - also supports data binding using Containers
        // Select a puzzle <-- lisaa kuva
        OptionGroup citySelect = new OptionGroup(null, puzzles);
        citySelect.setNullSelectionAllowed(false); // user can not 'unselect'
        citySelect.select("Park"); // select this by default
        citySelect.setImmediate(true); // send the change to the server at once
        citySelect.addValueChangeListener(this); // react when the user selects
        citySelect.setStyleName("label");
        miniPicPanel = new Panel();
        miniPicPanel.setHeight(102, Unit.PIXELS);
        miniPicPanel.setWidth(135, Unit.PIXELS);
        Image miniPic = SplitPanelLayout.LoadImage(puzzleSelection + "/small"
                + puzzleSelection + ".png");
        miniPicPanel.setContent(miniPic);
        optionsLayout.addComponent(miniPicPanel, 1, 1);
        optionsLayout.setComponentAlignment(miniPicPanel,
                Alignment.MIDDLE_CENTER);
        addComponent(citySelect);

    }

    /*
     * The listener will be called whenever the value of the component changes,
     * i.e when the user makes a new selection.
     */
    public void valueChange(ValueChangeEvent event) {
        @SuppressWarnings("unchecked")
        Property<String> p = event.getProperty();
        puzzleSelection = p.getValue();
        changeMinipic();
    }

    public String getSelection() {
        return puzzleSelection;
    }

    /**
     * Updates the small picture of the puzzle.
     */
    public void changeMinipic() {
        Image miniPic = SplitPanelLayout.LoadImage(puzzleSelection + "/small"
                + puzzleSelection + ".png");
        miniPicPanel.setContent(miniPic);
    }
}