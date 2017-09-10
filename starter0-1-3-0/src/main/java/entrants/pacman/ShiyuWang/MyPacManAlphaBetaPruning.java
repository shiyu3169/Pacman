package entrants.pacman.ShiyuWang;
import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.DM;
import pacman.game.Game;



import java.util.*;

/**
 * This class implement alpha-beta pruning algorithm on PacMan.
 */
public class MyPacManAlphaBetaPruning extends PacmanController {
    private static int MAX_DEPTH = 4;
    private MOVE lastlastMove;
    private int closetPillIndex = -1;

    /**
     * This function return the move direction of pacman
     *
     * @param game
     * @param timeDue
     * @return MOVE the next move
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {

        // Should always be possible as we are PacMan
        closetPillIndex = findClosetPillIndex(game);
        int current = game.getPacmanCurrentNodeIndex();
        int minDistance = Integer.MAX_VALUE;
        for (GHOST ghost : GHOST.values()) {
            // If can't see these will be -1 so all fine there
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
                int ghostLocation = game.getGhostCurrentNodeIndex(ghost);
                if (ghostLocation != -1) {
                    minDistance = Math.min(minDistance, game.getManhattanDistance(current, ghostLocation));
                }
            }
        }
        if (minDistance > 20) {
            PacmanController BFS = new MyPacManBFS();
            return BFS.getMove(game, timeDue);
        }

        closetPillIndex = findClosetPillIndex(game);
        return alphaBeta(game);
    }

    /**
     * This function return the max value of minimizer layer.
     *
     * @param game
     * @param alpha
     * @param beta
     * @param depth
     * @return max score
     */
    private double maxValue(Game game, double alpha, double beta, int depth) {
        double v = Double.NEGATIVE_INFINITY;
        if (game.wasPacManEaten() || game.gameOver() || depth == 0) {
            return evaluate(game);
        }
        int current = game.getPacmanCurrentNodeIndex();
        for (MOVE move : game.getPossibleMoves(current)) {
            Game newGame = game.copy();
            newGame.updatePacMan(move);
            newGame.updateGame();
            v = Math.max(v, minValue(newGame, alpha, beta, depth - 1));
            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
        }
        return v;
    }

    /**
     * This function minimize the max level value
     *
     * @param game
     * @param alpha
     * @param beta
     * @param depth
     * @return the minimize value of lower level
     */
    private double minValue(Game game, double alpha, double beta, int depth) {
        double v = Double.POSITIVE_INFINITY;
        if (game.wasPacManEaten() || game.gameOver() || depth == 0) {
            return evaluate(game);
        }
        ArrayList<EnumMap<GHOST, MOVE>> ghostsMoves = combineGhostMove(game);
        for (int i = 0; i < ghostsMoves.size(); i++) {
            Game newGame = game.copy();
            newGame.updateGhosts(ghostsMoves.get(i));
            newGame.updateGame();
            v = Math.min(v, maxValue(newGame, alpha, beta, depth - 1));
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
        }
        return v;
    }

    /**
     * This  is the alpha beta pruning function used to find next move.
     *
     * @param game
     * @return next move
     */
    private MOVE alphaBeta(Game game) {
        MOVE bestMove = MOVE.NEUTRAL;
        double v = Double.NEGATIVE_INFINITY;
        double alpha =  Double.NEGATIVE_INFINITY;
        double beta =  Double.POSITIVE_INFINITY;
        MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        for (MOVE move : moves) {
            double prev = v;
            Game newGame = game.copy();
            newGame.updatePacMan(move);
            newGame.updateGame();
            double temp = minValue(newGame, alpha, beta, MAX_DEPTH);
            v = Math.max(v, temp);
            if (v > prev) {
                bestMove = move;
            }
            if (v >= beta) {
                return bestMove;
            }
            alpha = Math.max(alpha, v);
        }
        return bestMove;
    }

    /**
     * find the possible combined moves of ghost
     *
     * @param game
     * @return arraylist of moves combination
     */
    private ArrayList<EnumMap<GHOST, MOVE>> combineGhostMove(Game game) {
        GHOST[] ghosts = GHOST.values();
        ArrayList<EnumMap<GHOST, MOVE>> ghostMoves = new ArrayList<>();
        ghostMoves.add(new EnumMap<GHOST, MOVE>(GHOST.class));
        for (GHOST ghost : ghosts) {
            int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
            MOVE[] possibleMoves = game.getPossibleMoves(ghostIndex);
            if (possibleMoves.length == 0) continue;
            ArrayList<EnumMap<GHOST, MOVE>> temp = new ArrayList<>();
            for (MOVE move : possibleMoves) {
                for (int k = 0; k < ghostMoves.size(); k++) {
                    EnumMap<GHOST, MOVE> newElement = new EnumMap<>(ghostMoves.get(k));
                    newElement.put(ghost, move);
                    temp.add(newElement);
                }
            }
            ghostMoves = new ArrayList<>(temp);
            temp.clear();
        }
        return ghostMoves;
    }

    /**
     * It evaluate the points of next move by pill distance, ghost distance and current score.
     *
     * @param game
     * @return a double represents points
     */
    private double evaluate(Game game) {
        int current = game.getPacmanCurrentNodeIndex();
        int pillIndex = findClosetPillIndex(game);
        MOVE lastMove = game.getPacmanLastMoveMade();
        int pillDistance = game.getShortestPathDistance(current, pillIndex, lastMove);
        double ghostsDistance = 0;
        for (GHOST ghost : GHOST.values()) {
            int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
            ghostsDistance = Math.min(ghostsDistance, game.getManhattanDistance(current, ghostIndex));
        }
        if(ghostsDistance == 0 || ghostsDistance > 20) {
            return pillDistance + game.getScore();
        }
        return ghostsDistance/pillDistance + game.getScore();
    }


    /**
     * find the nearest pill
     *
     * @param game
     * @return closet pill index
     */
    private int findClosetPillIndex(Game game) {
        int current = game.getPacmanCurrentNodeIndex();
        int closetNode = -1;
        // find closet available pills and power pills
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();

        ArrayList<Integer> targets = new ArrayList<>();

        for (int i = 0; i < pills.length; i++) {
            //check which pills are available
            Boolean pillStillAvailable = game.isPillStillAvailable(i);
            if (pillStillAvailable == null) continue;
            if (game.isPillStillAvailable(i)) {
                targets.add(pills[i]);
            }
        }

        for (int i = 0; i < powerPills.length; i++) {
            //check with power pills are available
            Boolean pillStillAvailable = game.isPillStillAvailable(i);
            if (pillStillAvailable == null) continue;
            if (game.isPowerPillStillAvailable(i)) {
                targets.add(powerPills[i]);
            }
        }

        if (!targets.isEmpty()) {
            int[] targetsArray = new int[targets.size()];

            //convert from ArrayList to array
            for (int i = 0; i < targetsArray.length; i++) {
                targetsArray[i] = targets.get(i);
            }
            closetNode = game.getClosestNodeIndexFromNodeIndex(current, targetsArray, DM.MANHATTAN);
        }
        return closetNode;
    }
}

