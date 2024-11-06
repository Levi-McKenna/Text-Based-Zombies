public class Tests {
    public static void readFile() {
        Level level = new Level("levels/test/Level1.txt");

        for (String line : level.level) {
            System.out.println(line);
        }

    }
}
