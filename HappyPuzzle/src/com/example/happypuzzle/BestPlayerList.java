package com.example.happypuzzle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.VaadinService;

/**
 * Single high score list, uses DatabaseTableContainer to store and fetch data.
 * 
 * @author Team HappyPuzzle
 */
public class BestPlayerList {

    private int limit = 10;
    private ArrayList<ResultTuple> results;
    private final String id;
    private final DatabaseTableContainer db;

    /**
     * Constructor
     * 
     * @param id
     *            Name of the Puzzle
     * 
     * @throws ClassNotFoundException
     *             If SQLite driver is not installed
     * @throws SQLException
     *             If database queries fail
     */
    public BestPlayerList(String id) throws ClassNotFoundException,
            SQLException {
        this.id = id;
        results = new ArrayList<ResultTuple>();
        this.db = new DatabaseTableContainer(id, VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath()
                + "/WEB-INF/db/highscores.db");
        this.updateResults();

    }

    /**
     * Fetches data from DatabaseTableContainer
     */
    public void updateResults() {
        ArrayList<ResultTuple> temp_results = new ArrayList<ResultTuple>();

        for (Object id : this.db.getItemIds()) {
            temp_results.add((ResultTuple) this.db.getItem(id)
                    .getItemProperty("Result").getValue());

        }

        Collections.sort(temp_results);
        this.results = temp_results;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Takes result in ResultTuple type, checks whether the result is good
     * enough for high score list and adds it in to the list if it is.
     * 
     * @param rt
     *            ResultTuple to be committed
     * 
     * @return boolean Whether result was accepted into best players list
     */
    public boolean commitResult(ResultTuple rt) {
        if (doesQualify(rt)) {

            results.add(rt);
            prune();

            this.db.removeAllItems();
            for (ResultTuple r : this.results) {

                Object o = this.db.addItem();
                Item i = this.db.getItem(o);
                i.addItemProperty("Result", new ObjectProperty<ResultTuple>(r));

            }
            return true;

        }

        return false;
    }

    /**
     * Keep the high score list short (max 10 items).
     */
    private void prune() {
        if (results.size() > limit) {

            while (results.size() > limit) {
                ResultTuple t = Collections.max(results);
                results.remove(t);
            }
        }
    }

    public ArrayList<ResultTuple> getResults() {
        this.updateResults();
        return results;
    }

    /**
     * Checks whether result is good enough for the high score list.
     * 
     * @param rt
     *            ResultTuple to be checked
     * @return boolean whether result is good enough
     */
    public boolean doesQualify(ResultTuple rt) {
        this.updateResults();
        if (results.size() < limit) {
            return true;
        } else {
            Collections.sort(results);
            if (Collections.max(results).getTotalMoves() > rt.getTotalMoves()) {
                return true;
            }

        }

        return false;
    }

    /**
     * Returns high score content in HTML format.
     * 
     * @return String HTML formatted String
     */
    public String getString() {
        Collections.sort(results);
        String res = "<br>";
        int c = 1;
        for (ResultTuple tuple : results) {
            res += c + ". " + tuple.toString() + "<br>";
            c++;
        }
        return res;
    }
}
