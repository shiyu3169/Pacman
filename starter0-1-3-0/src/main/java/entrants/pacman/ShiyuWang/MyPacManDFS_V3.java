package entrants.pacman.ShiyuWang;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

import java.util.*;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.ShiyuWang).
 */
public class MyPacManDFS_V3 extends PacmanController {
    private static final int MIN_DISTANCE = 20;
    private Random random = new Random();

    @Override
    public MOVE getMove(Game game, long timeDue) {

       // Should always be possible as we are PacMan
        int current = game.getPacmanCurrentNodeIndex();
        NodePath currentNode = new NodePath(current, new ArrayList<Integer>());
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

        /// Strategy 2: Find nearest edible ghost and go after them
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
        }
        //visited node
        Set<Integer> visited = new HashSet<>();

        //Used to store nodes
        Stack<NodePath> nodes = new Stack<>();

        nodes.add(currentNode);

        while (!nodes.isEmpty()) {
            NodePath node = nodes.pop();
            if (visited.contains(node.index)) continue;

            visited.add(node.index);
            int pillIndex = game.getPillIndex(node.index);
            int powerPillIndex = game.getPowerPillIndex(node.index);

            //check whether there is a pill
            if (pillIndex != -1
                    && game.isPillStillAvailable(pillIndex) != null
                    && game.isPillStillAvailable(pillIndex)) {
                MOVE next = game.getMoveToMakeToReachDirectNeighbour(current, node.path.get(0));
                if (game.getPacmanLastMoveMade().opposite() != next) {
                    return next;
                }
            }
            //check whetehr there is a powerpill
            if (powerPillIndex != -1
                    && game.isPillStillAvailable(powerPillIndex) != null
                    && game.isPowerPillStillAvailable(powerPillIndex)) {
                MOVE next = game.getMoveToMakeToReachDirectNeighbour(current, node.path.get(0));
                if (game.getPacmanLastMoveMade().opposite() != next) {
                    return next;
                }
            }

            //if no pill, add its neighbours to the queue.
            int[] neighbours = game.getNeighbouringNodes(node.index);
            for (int i = 0; i < neighbours.length; i++) {

                //make a neighbour nodepath
                ArrayList<Integer> neighbourPath = new ArrayList<>(node.path);
                neighbourPath.add(neighbours[i]);
                NodePath neighbour = new NodePath(neighbours[i], neighbourPath);
                nodes.push(neighbour);
            }
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