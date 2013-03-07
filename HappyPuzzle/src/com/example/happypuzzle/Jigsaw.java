package com.example.happypuzzle;

import java.util.ArrayList;
import java.util.Collections;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.dragdroplayouts.DDGridLayout;
import fi.jasoft.dragdroplayouts.DDGridLayout.GridLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.DDGridLayout.GridLayoutTransferable;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.HorizontalLocationIs;
import fi.jasoft.dragdroplayouts.events.VerticalLocationIs;

/**
 * Actual 4x4 jigsaw puzzle. Load pictures from a folder. Uses DDGridLayout
 * add-on to implement dragging and dropping.
 * 
 * @author Team HappyPuzzle
 */
@SuppressWarnings("serial")
public class Jigsaw extends VerticalLayout {

    private String puzzleSelection;
    private int piecesInCorrectPositions;
    private Label completionLabel;
    final ArrayList<Image> images;
    final DDGridLayout puzzleLayout;
    final SplitPanelLayout layout;
    private int totalMoves;

    /**
     * Constructor
     * 
     * @param layout
     *            SplitPanelLayout containing the main UI
     */
    public Jigsaw(final SplitPanelLayout layout) {
        this.layout = layout;
        puzzleSelection = layout.getSelection();
        puzzleLayout = new DDGridLayout(4, 4);
        puzzleLayout.setWidth("816");
        puzzleLayout.setHeight("616px");
        puzzleLayout.setDragMode(LayoutDragMode.CLONE);
        totalMoves = 0;

        images = new ArrayList<Image>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                images.add(SplitPanelLayout.LoadImage(puzzleSelection + "/" + i
                        + "_" + j + "." + puzzleSelection + ".png"));
            }
        }

        ArrayList<Image> shuffledImages = new ArrayList<Image>();

        for (int i = 0; i < images.size(); i++) {
            shuffledImages.add(images.get(i));
        }

        do {
            Collections.shuffle(shuffledImages);
            puzzleLayout.removeAllComponents();
            int c = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {

                    puzzleLayout.addComponent(shuffledImages.get(c), j, i);
                    c++;
                }
            }
            calculatePiecesInCorrectPositions();
        } while (piecesInCorrectPositions != 0);

        completionLabel = new Label(updateCompletionValue());

        puzzleLayout.setDropHandler(new DropHandler() {

            public AcceptCriterion getAcceptCriterion() {
                return new And(VerticalLocationIs.MIDDLE,
                        HorizontalLocationIs.CENTER);
            }

            public void drop(DragAndDropEvent event) {
                GridLayoutTargetDetails details = (GridLayoutTargetDetails) event
                        .getTargetDetails();

                GridLayoutTransferable transferable = (GridLayoutTransferable) event
                        .getTransferable();

                // Component that is drag and dropped
                Component c = transferable.getComponent();
                // Component where we dropped
                Component d = details.getOverComponent();
                // Swaps those components
                swapComponents(puzzleLayout, c, d);

                totalMoves++;
                calculatePiecesInCorrectPositions();

                if (piecesInCorrectPositions >= 16) {

                    // Freezethegrid
                    // puzzleLayout.setDragMode(LayoutDragMode.NONE);
                    layout.submitHighScore();
                    puzzleLayout.removeAllComponents();
                    puzzleLayout.setWidth("800px");
                    puzzleLayout.setHeight("600px");
                    Notification.show("Congratulations! You solved it!",
                            Notification.Type.WARNING_MESSAGE);
                    puzzleLayout.addComponent(
                            SplitPanelLayout.LoadImage(puzzleSelection
                                    + "/full" + puzzleSelection + ".png"), 0, 0);

                }

                updateCompletionValue();
                layout.updateCompleted();

            }

            private void swapComponents(DDGridLayout layout, Component c,
                    Component d) {
                // find column and row for component c
                int c_row = 0;
                int c_col = 0;

                int d_row = 0;
                int d_col = 0;

                for (int i = 0; i < layout.getColumns(); i++) {
                    for (int a = 0; a < layout.getRows(); a++) {
                        Component comp = layout.getComponent(i, a);
                        if (comp == c) {
                            c_col = i;
                            c_row = a;
                        }
                        if (comp == d) {
                            d_col = i;
                            d_row = a;
                        }
                    }
                }
                // removes both components
                layout.removeComponent(c);
                layout.removeComponent(d);
                // puts them back
                layout.addComponent(c, d_col, d_row);
                layout.addComponent(d, c_col, c_row);

            }
        });

        addComponent(puzzleLayout);

    }

    /**
     * Calculates value for piecesInCorrectPositions.
     */
    public void calculatePiecesInCorrectPositions() {
        ArrayList<Image> tarkastus = new ArrayList<Image>();

        for (int i = 0; i < puzzleLayout.getRows(); i++) {
            for (int a = 0; a < puzzleLayout.getColumns(); a++) {
                tarkastus.add((Image) puzzleLayout.getComponent(a, i));
            }
        }

        piecesInCorrectPositions = 0;
        for (int i = 0; i < 16; i++) {
            if (tarkastus.get(i).equals(images.get(i))) {
                piecesInCorrectPositions++;
            }
        }

    }

    public int getTotalMoves() {
        return totalMoves;
    }

    /**
     * Returns a String of current game statistics in HTML format.
     * 
     * @return String game statistics
     */
    public String updateCompletionValue() {
        return "<h1>Pieces in correct positions: " + piecesInCorrectPositions
                + " / 16</h1>" + "<p><h1>Number of Moves: " + totalMoves
                + "</h1>";
    }

    public Label getCompletionLabel() {
        completionLabel.setValue(updateCompletionValue());
        completionLabel.setStyleName("biglabel");
        return completionLabel;
    }

}