public class Game {
    private Player player;
    private Level level;
    private String[] levelDirs;


    public Game() {
        this.player = new Player();
        this.level = new Level("./levels/test/test.txt");
        this.levelDirs = new String[1];
    }    

    public void run() {

    }
}
