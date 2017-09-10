package entrants.pacman.ShiyuWang;

import pacman.game.Constants;

import java.util.ArrayList;

/**
 * Created by Shiyu on 7/19/2016.
 */
//Record node with path from current
public class NodePath {
    int index;
    ArrayList<Integer> path;
    int g;
    int h;

    //for BFS and DFS to record path
    public NodePath(int index, ArrayList<Integer> path) {
        this.index = index;
        this.path = path;
    }
    // for A_star to calculate cost
    public NodePath(int index, ArrayList<Integer> path, int g, int h) {
        this.index = index;
        this.path = path;
        this.g = g; //cost so far to reach node
        this.h = h; //estimated cost from n to goal
    }
}
