package com.example.happypuzzle;

/**
 * Represents a single score item, has name and score.
 * 
 * @author Team HappyPuzzle
 * 
 */
public class ResultTuple implements Comparable {
    private String name;
    private int totalMoves;

    /**
     * Constructor
     * 
     * @param name
     *            Name of the Player
     * @param totalMoves
     *            Number of moves by the player
     */
    public ResultTuple(String name, int totalMoves) {
        this.setName(name);
        this.setTotalMoves(totalMoves);

    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " " + totalMoves;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        try {
            return totalMoves - ((ResultTuple) o).getTotalMoves();
        } catch (Exception e) {
            return 0;
        }
    }

}
