package entrants.pacman.ShiyuWang;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.ShiyuWang).
 */
public class MyPacManGreedyBestFirst extends PacmanController {
    private static final int MIN_DISTANCE = 20;
    private Random random = new Random();

    // use stack to store visited node
    private Stack<Integer> visited = new Stack<Integer>();
    // store visited path
    ArrayList<Integer> path = new ArrayList<Integer>();
    @Override
    public MOVE getMove(Game game, long timeDue) {
       // Should always be possible as we are PacMan
        int current = game.getPacmanCurrentNodeIndex();
       // Strategy 1: Adjusted for PO
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            // If can't see these will be -1 so all fine there
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
                int ghostLocation = game.getGhostCurrentNodeIndex(ghost);
                if (ghostLocation != -1) {
                    if (game.getShortestPathDistance(current, ghostLocation) < MIN_DISTANCE) {
                        return game.getNextMoveAwayFromTarget(current, ghostLocation, Constants.DM.PATH);
                    }
                }
            }
        }
/*        /// Strategy 2: Find nearest edible ghost and go after them
        int minDistance = Integer.MAX_VALUE;
        Constants.GHOST minGhost = null;
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            // If it is > 0 then it is visible so no more PO checks
            if (game.getGhostEdibleTime(ghost) > 0) {
                int distance = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost));

                if (distance < minDistance) {
                    minDistance = distance;
                    minGhost = ghost;
                }
            }
        }

        if (minGhost != null) {
            return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minGhost), Constants.DM.PATH);

        }*/

        // Strategy 2: Using Greedy Best First search to find the best neighbour of all neighbours
        // in order to catch the closest Ghost
        // it must be used in fully observable environment
        double minDisplacement = Double.MAX_VALUE;
        int minNeighbour = -1;
        int[] neighbours = game.getNeighbouringNodes(current);
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            // If it is > 0 then it is visible so no more PO checks
            if (game.getGhostEdibleTime(ghost) > 0) {
                for(int i =0;i< neighbours.length;i++) {
                    //find the displacement between neighbour and ghost
                    double displacement = game.getManhattanDistance(neighbours[i], game.getGhostCurrentNodeIndex(ghost));
                    if(displacement < minDisplacement) {
                        minDisplacement = displacement;
                        minNeighbour = neighbours[i];
                    }
                }
            }
        }

        if (minDisplacement != Double.MAX_VALUE) {
            return game.getNextMoveTowardsTarget(current, minNeighbour, Constants.DM.PATH);

        }

        // Strategy 3: Go after the pills and power pills that we can see
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();

        ArrayList<Integer> targets = new ArrayList<Integer>();

        for (int i = 0; i < pills.length; i++) {
            //check which pills are available
            Boolean pillStillAvailable = game.isPillStillAvailable(i);
            if (pillStillAvailable == null) continue;
            if (game.isPillStillAvailable(i)) {
                targets.add(pills[i]);
            }
        }

        for (int i = 0; i < powerPills.length; i++) {            //check with power pills are available
            Boolean pillStillAvailable = game.isPillStillAvailable(i);
            if (pillStillAvailable == null) continue;
            if (game.isPowerPillStillAvailable(i)) {
                targets.add(powerPills[i]);
            }
        }

        if (!targets.isEmpty()) {
            int[] targetsArray = new int[targets.size()];        //convert from ArrayList to array

            for (int i = 0; i < targetsArray.length; i++) {
                targetsArray[i] = targets.get(i);
            }
            //return the next direction once the closest target has been identified
            return game.getNextMoveTowardsTarget(current, game.getClosestNodeIndexFromNodeIndex(current, targetsArray, Constants.DM.PATH), Constants.DM.PATH);
        }




        // Strategy 4: New PO strategy as now S3 can fail if nothing you can see
        // Going to pick a random action here
        MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
        if (moves.length > 0) {
            return moves[random.nextInt(moves.length)];
        }
        // Must be possible to turn around
        return game.getPacmanLastMoveMade().opposite();
    }
}