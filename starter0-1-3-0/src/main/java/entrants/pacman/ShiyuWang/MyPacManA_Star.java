package entrants.pacman.ShiyuWang;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.*;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.ShiyuWang).
 */
public class MyPacManA_Star extends PacmanController {
    private static final int MIN_DISTANCE = 20;
    private Random random = new Random();

    private static Comparator<NodePath> CPT = new Comparator<NodePath>() {
        @Override
        public int compare(NodePath n1, NodePath n2) {
            int f1 = n1.g + n1.h;
            int f2 = n2.g + n2.h;
            return f1 - f2;
        }
    };


    @Override
    public MOVE getMove(Game game, long timeDue) {
       // Should always be possible as we are PacMan
        int current = game.getPacmanCurrentNodeIndex();
        NodePath currentNode = new NodePath(current, new ArrayList<Integer>(),0,0);
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
        //Strategy 3 A* Search

        //visited node
        Set<Integer> visited = new HashSet<>();

        //Used to store nodes
        Queue<NodePath> nodes =  new PriorityQueue<>(10, CPT);

        nodes.add(currentNode);

        while (!nodes.isEmpty()) {
            NodePath node = nodes.poll();
            if (visited.contains(node.index)) continue;

            visited.add(node.index);
            int pillIndex = game.getPillIndex(node.index);
            int powerPillIndex = game.getPowerPillIndex(node.index);

            //check whether there is a pill
            if (pillIndex != -1
                    && game.isPillStillAvailable(pillIndex) != null
                    && game.isPillStillAvailable(pillIndex)) {
                return game.getMoveToMakeToReachDirectNeighbour(current, node.path.get(0));
            }
            //check whetehr there is a power pill
            if (powerPillIndex != -1
                    && game.isPillStillAvailable(powerPillIndex) != null
                    && game.isPowerPillStillAvailable(powerPillIndex)) {
                return game.getMoveToMakeToReachDirectNeighbour(current, node.path.get(0));
            }

            //if no pill, add its neighbours to the queue.
            int[] neighbours = game.getNeighbouringNodes(node.index);
            for (int i = 0; i < neighbours.length; i++) {

                //make a neighbour nodepath
                ArrayList<Integer> neighbourPath = new ArrayList<>(node.path);
                neighbourPath.add(neighbours[i]);
                NodePath neighbour = new NodePath(neighbours[i], neighbourPath, node.g + game.getManhattanDistance(current, node.index),game.getManhattanDistance(node.index,neighbours[i]));
                nodes.offer(neighbour);
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