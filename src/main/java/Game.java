public class Game {

    public static int[][] gameSpace = new int[3][3];


    public static int makeAMove(int horizontal, int vertical, int xOrO) {
        gameSpace[horizontal][vertical] = xOrO;
        boolean gameProcessing = false;
        if(winnerCheck()) return 1;
        for (int i = 0; i < gameSpace.length; i++) {
            for (int j = 0; j < gameSpace[i].length; j++) {
                if (gameSpace[i][j] == 0){
                    gameProcessing = true;
                }
            }
        }
        if (!gameProcessing) return 2;
        return 0;
    }

    private static boolean winnerCheck() {
        if (((  gameSpace[0][0] + gameSpace[0][1] + gameSpace[0][2] == 3) || (gameSpace[0][0] + gameSpace[0][1] + gameSpace[0][2] == 30)) ||
              ((gameSpace[1][0] + gameSpace[1][1] + gameSpace[1][2] == 3) || (gameSpace[1][0] + gameSpace[1][1] + gameSpace[1][2] == 30)) ||
              ((gameSpace[2][0] + gameSpace[2][1] + gameSpace[2][2] == 3) || (gameSpace[2][0] + gameSpace[2][1] + gameSpace[2][2] == 30)) ||

              ((gameSpace[0][0] + gameSpace[1][0] + gameSpace[2][0] == 3) || (gameSpace[0][0] + gameSpace[1][0] + gameSpace[2][0] == 30)) ||
              ((gameSpace[0][1] + gameSpace[1][1] + gameSpace[2][1] == 3) || (gameSpace[0][1] + gameSpace[1][1] + gameSpace[2][1] == 30)) ||
              ((gameSpace[0][2] + gameSpace[1][2] + gameSpace[2][2] == 3) || (gameSpace[0][2] + gameSpace[1][2] + gameSpace[2][2] == 30)) ||

              ((gameSpace[0][0] + gameSpace[1][1] + gameSpace[2][2] == 3) || (gameSpace[0][0] + gameSpace[1][1] + gameSpace[2][2] == 30)) ||
              ((gameSpace[0][2] + gameSpace[1][1] + gameSpace[2][0] == 3) || (gameSpace[0][2] + gameSpace[1][1] + gameSpace[2][0] == 30))){
            return true;
        }else return false;
    }
}
