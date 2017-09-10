import entrants.pacman.ShiyuWang.*;
import examples.commGhosts.POCommGhosts;
import pacman.Executor;


/**
 * Created by pwillic on 06/05/2016.
 */
public class Main {

    public static void main(String[] args) {

        //partial observable
        //Executor executor = new Executor(true, true);

        //fully observable
         Executor executor = new Executor(false, true);


        //BFS
        //executor.runGameTimed(new MyPacManBFS(), new POCommGhosts(50), true);
        //A*
        //executor.runGameTimed(new MyPacManA_Star(), new POCommGhosts(50), true);
        //DFS
        //executor.runGameTimed(new MyPacManDFS_V3(), new POCommGhosts(50), true);
        //GreedyBestFirst
        //executor.runGameTimed(new MyPacManGreedyBestFirst(), new POCommGhosts(50), true);
        //Alpha Beta
        executor.runGameTimed(new MyPacManAlphaBetaPruning(), new POCommGhosts(50), true);
    }
}

