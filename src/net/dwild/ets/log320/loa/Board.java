package net.dwild.ets.log320.loa;


public class Board {

    int[][] boardConfiguration = new int[8][8];

    public Board(){
        initializeBoard();
    }

    public void initializeBoard() {
        // Normalement, le serveur devrait envoyer ce message en début de partie (on pourra supprimer la méthode plus tard)
        String serverMessage = "1 0 2 2 2 2 2 2 0 4 0 0 0 0 0 0 4 4 0 0 0 0 0 0 4 4 0 0 0 0 0 0 4 4 0 0 0 0 0 0 4 4 0 0 0 0 0 0 4 4 0 0 0 0 0 0 4 0 2 2 2 2 2 2 0";
        createBoard(serverMessage);
    }

    public void createBoard(String serverMessage) {
        String[] values;
        values = serverMessage.split(" ");
        int x=0,y=0;
        // On doit commencer à 1 pour sauter par-dessus le message du serveur
        for(int i=1; i<values.length;i++){
            boardConfiguration[x][y] = Integer.parseInt(values[i]);
            x++;
            if(x == 8){
                x = 0;
                y++;
            }
        }
    }
}
