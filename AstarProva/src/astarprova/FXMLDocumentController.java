/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package astarprova;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 *
 * @author Roberto
 */

/*
* non vanno gli import e gli export
*
* */

public class FXMLDocumentController implements Initializable {

    @FXML
    GridPane griglia;
    @FXML
    Label indice, numeroNodi;
    @FXML
    TextField molt, t1, t2, pathFileEsporta, pathFileImporta, iterazioni;// aggiungere anche le altre

    Button tramite;

    private Background BRD = new Background(new BackgroundFill(Color.DARKRED, null, null));
    private Background BG = new Background(new BackgroundFill(Color.LIMEGREEN, null, null));
    private Background BDefault = new Background(new BackgroundFill(Color.DARKCYAN, null, null));//colore di defalult
    private Background yellow = new Background(new BackgroundFill(Color.AZURE, null, null));// annalizzate
    private Background black = new Background(new BackgroundFill(Color.BLACK, null, null));// muri
    private Background red = new Background(new BackgroundFill(Color.BLUEVIOLET, null, null));// foglie
    private Background purple = new Background(new BackgroundFill(Color.BLUE, null, null));//path trovato / che sta venendo analizzato

    Button bottoni[][];
    private int c = 57;// colonne 57
    private int r = 40;// righe 40

    private double moltiplicatore=1;
    private long tempo1 = 10_000_000;
    private long tempo2 = 1_000_000;

    private int size = 100;//grandezza bottoni
    private int depth =0;

    boolean iniziato = false;
    
    ArrayList<Posizione> muri = new ArrayList<>();

    ArrayList<Posizione> migliori = new ArrayList<>();

    AnimazioneAstar pathFinder;

    // per astar



    @FXML
    private void inizia(ActionEvent e) throws InterruptedException {
        pathFinder.stop();
        numeroNodi.setText("nodi: "+pathFinder.numeroNodi);
        //if(true){
            settaMuri();
            resettaCelleGialle();
           Posizione inizio = null;
            Posizione fine = null;
            iniziato = true;
            for(int i=0;i<r;i++) {
                for (int j = 0; j < c; j++) {
                    if (bottoni[i][j].getBackground() == BRD) {
                        System.out.println("rosso scurooo");
                        fine = new Posizione(i, j);
                    } else if (bottoni[i][j].getBackground() == BG) {
                        System.out.println("verdee");
                        inizio = new Posizione(i, j);
                    }
                }
            }
            Astar<Posizione> astar = new Astar<>((start , finish, courrent)->{

               return Math.sqrt((Math.pow((courrent.attributo.x-finish.attributo.x), 2)+(Math.pow((finish.attributo.y-courrent.attributo.y), 2))));
               // return 0;
            });




            astar.setRadice(new Nodo<>(null, inizio, Double.MAX_VALUE));//da contare per entrambi il costo
            astar.setFine(new Nodo<Posizione>(null, fine, Double.MIN_VALUE));
            //astar.radice.costo = astar.getEuristicaNodo(astar.radice);// 0 + h
            astar.setAlbero(astar.getRadice());
            astar.getRadice().visto = true;
            astar.setMoltiplicatore(moltiplicatore);
            pathFinder.setTempo1(tempo1);
            pathFinder.setTempo2(tempo2);

            depth=1;
            if(fine!=null && inizio!=null){
                pathFinder.setta(fine, inizio, astar, muri);
                pathFinder.start();
            }
        //}
    }

    private void settaMuri() {
        muri = new ArrayList<>();
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(bottoni[i][j].getBackground()==black){
                    muri.add(new Posizione(i, j));
                }
            }
        }
    }


    @FXML
    private  void tempo1(ActionEvent actionEvent) {
        try{
            tempo1 = Long.parseLong(t1.getText());
        }catch(Exception e){
            t1.clear();
        }
    }
    @FXML
    private void tempo2(ActionEvent actionEvent) {
        try{
            tempo2 = Long.parseLong(t2.getText());
        }catch(Exception e){
            t2.clear();
        }
    }
    @FXML
    private void iterazioniMax(ActionEvent actionEvent) {
        try{
            pathFinder.setDepthMax(Integer.parseInt(iterazioni.getText()));
        }catch(Exception e){
            if(iterazioni.getText().equals("-")){
                pathFinder.setDepthMax(Integer.MAX_VALUE);
            }
            iterazioni.clear();
        }



    }
    @FXML
    private void moltiplica(ActionEvent actionEvent) {
        try{
            moltiplicatore = Double.parseDouble(molt.getText());
        }catch(Exception e){
            molt.clear();
        }

    }
    @FXML
    private void cancellaMuri(ActionEvent actionEvent) {

        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(bottoni[i][j].getBackground()==black){
                    bottoni[i][j].setBackground(BDefault);
                }
            }
        }

    }

    @FXML
    private  void pulisciFinestra(ActionEvent actionEvent) {
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(bottoni[i][j].getBackground()!=black && bottoni[i][j].getBackground()!=BG && bottoni[i][j].getBackground()!=BRD && bottoni[i][j].getBackground()!=BDefault){
                    bottoni[i][j].setBackground(BDefault);
                }
            }
        }
    }
 @FXML
    private void salvaSuFile(ActionEvent e) throws IOException {
        new FileWriter(pathFileEsporta.getText()).write("");
        FileWriter fw = new FileWriter(pathFileEsporta.getText(), true);

        Background bb = null;
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                bb = bottoni[i][j].getBackground();
                if(bb==black){
                    fw.write('$');
                }else if(bb==BG){
                    fw.write('i');
                }else if(bb==BRD){
                    fw.write('f');
                }else if(bb==yellow){
                    fw.write('c');
                }else if(bb==BDefault){
                    fw.write('-');
                }else if(bb==purple){
                    fw.write('p');
                }else if(bb==red){
                    fw.write('n');//foglia
                }else{
                    fw.write('-');
                }
            }
            fw.write('\n');
        }
        fw.close();
        numeroNodi.setText("salvato!");
    }
    @FXML
    private void prendiDaFile(ActionEvent e) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(pathFileImporta.getText()));
        String app;
        char[] appChar;
        int y=1;
        int x = br.readLine().toCharArray().length;
        while(br.readLine()!=null)y++;
        br.close();
        br = new BufferedReader(new FileReader(pathFileImporta.getText()));
        bottoni = new Button[y][x];
        int cont =0;
        this.r = y;
        this.c = x;
        System.out.println(r+" "+c);

        griglia.getRowConstraints().clear();
        griglia.getColumnConstraints().clear();


        for(int i=0;i<c;i++){// non so perchè bisogna configurarle al contrario
            griglia.getColumnConstraints().add(new ColumnConstraints(15));
        }
        for(int i=0;i<r;i++){//idem
            griglia.getRowConstraints().add(new RowConstraints(15));
        }

        while((app=br.readLine())!=null){
            System.out.println("dentro");
            appChar = app.toCharArray();

            for(int i=0; i<appChar.length;i++){
                bottoni[cont][i] = new Button();
                bottoni[cont][i].setPrefSize(size, size);

                bottoni[cont][i].setOnMouseClicked(this::metodoMousePerBottoni);
                bottoni[cont][i].setOnMouseMoved(this::mouseMouvedBottoni);
                bottoni[cont][i].setScaleShape(true);

                switch(appChar[i]){
                    case '$':
                        bottoni[cont][i].setBackground(black);
                     break;
                     case 'i':
                         bottoni[cont][i].setBackground(BG);
                     break;
                     case 'f':
                         bottoni[cont][i].setBackground(BRD);
                     break;
                     case '-':
                         bottoni[cont][i].setBackground(BDefault);
                     break;
                     case 'p':
                         bottoni[cont][i].setBackground(purple);
                     break;
                     case 'c':
                         bottoni[cont][i].setBackground(yellow);
                     break;
                     case 'n':
                         bottoni[cont][i].setBackground(red);
                     break;

                }
                griglia.add(bottoni[cont][i], i, cont);
                //bottoni[cont][i].toBack();
            }
            // fare un if che controlla se o no il la lunghezza di quel char è uguale a c se no allora metterla apposto
            cont++;
        }
        br.close();
        pathFinder = new AnimazioneAstar(bottoni,  red, yellow, purple, tramite);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        indice.setText("inzio: verde (tasto destro) shift: crea muri\n fine: rosso (tasto sinistro) alt: cancella muri\n muro: nero (clic rotellina)");

        griglia.setGridLinesVisible(false);
        griglia.getRowConstraints().clear();
        griglia.getColumnConstraints().clear();
        griglia.getProperties().clear();
        for(int i=0;i<c;i++){// non so perchè bisogna configurarle al contrario
            griglia.getColumnConstraints().add(new ColumnConstraints(15));
        }
        for(int i=0;i<r;i++){//idem
            griglia.getRowConstraints().add(new RowConstraints(15));
        }

        //int size = 100;// grandezza dei bottoni

        griglia.setOnMouseMoved(this::grigliaMouse);


        System.out.println("c: "+c +" r: "+r);
        System.out.println("entrato ");
            bottoni = new Button[r][c];
            for(int i=0;i<r;i++) {
                for (int j = 0; j < c; j++) {

                    bottoni[i][j] = new Button();
                    bottoni[i][j].setVisible(true);

                    bottoni[i][j].setBackground(BDefault);

                    bottoni[i][j].setPrefSize(size, size);

                    bottoni[i][j].setOnMouseClicked(this::metodoMousePerBottoni);
                    bottoni[i][j].setOnMouseMoved(this::mouseMouvedBottoni);


                    bottoni[i][j].setScaleShape(true);
                    griglia.add(bottoni[i][j], j, i);
                    bottoni[i][j].toBack();
                }
            }
            // le linee della griglia vengon male
           // griglia.setGridLinesVisible(true);



            //pathFinder.start();
            tramite = new Button();
            tramite.setOnAction((e)->{
                numeroNodi.setText("nodi: "+pathFinder.numeroNodi);
            });
            pathFinder = new AnimazioneAstar(bottoni, red, yellow, purple, tramite);

    }

    private void grigliaMouse(MouseEvent e) {
        //System.out.println("ciaooo");

    }


    private void trovaColoreSingoloEResetta(Background bg){
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){

                    if (bottoni[i][j].getBackground() == bg) {
                        bottoni[i][j].setBackground(BDefault);
                        bottoni[i][j].setText("");
                        return;
                    }

            }
        }
    }

    private void resettaCelleGialle(){
         for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(bottoni[i][j].getBackground()!=BRD && bottoni[i][j].getBackground()!=BG && bottoni[i][j].getBackground()!=black){// se non è l'inizio ne la fine ne un muro
                    bottoni[i][j].setBackground(BDefault);
                }
            }
        }
    }


    /*
    * mouseListener per la griglia di bottoni
    * se si clicca il pulsante per selezionare allora si indica l'inizio (tipicamente il destro)
    * se si clicca il pulsante per far comparire il menu a comparsa allora si stabilise la fine (tipicamente il sinistro)
    * se si clissa la rotellina allora si stabilisce un muro
    * */
    public void metodoMousePerBottoni(MouseEvent e){
        if(e.getButton().equals(MouseButton.SECONDARY)){
            for (int k = 0; k < r; k++) {
                for (int l = 0; l < c; l++) {
                    if (e.getSource().equals(bottoni[k][l])) {
                        if(bottoni[k][l].getBackground()==BRD){
                            bottoni[k][l].setBackground(BDefault);
                        }else{
                            trovaColoreSingoloEResetta(BRD);
                            bottoni[k][l].setBackground(BRD);
                        }

                    }
                }
            }
        }else if(e.getButton().equals(MouseButton.PRIMARY)){
            for (int k = 0; k < r; k++) {
                for (int l = 0; l < c; l++) {
                    if (e.getSource().equals(bottoni[k][l])) {
                        if(bottoni[k][l].getBackground()==BG){
                            bottoni[k][l].setBackground(BDefault);// 8:30 12:30 14 18  5g 30g
                        }else{
                            trovaColoreSingoloEResetta(BG);
                            bottoni[k][l].setBackground(BG);
                        }

                    }
                }
            }
        }else if(e.getButton().equals(MouseButton.MIDDLE)) {
            for (int k = 0; k < r; k++) {
                for (int l = 0; l < c; l++) {
                    if (e.getSource().equals(bottoni[k][l])) {
                        if(bottoni[k][l].getBackground()==black){
                            bottoni[k][l].setBackground(BDefault);

                        }else {
                            bottoni[k][l].setBackground(black);
                        }

                    }
                }
            }
        }
    }
    
    public void mouseMouvedBottoni(MouseEvent e){
        for (int k = 0; k < r; k++) {
            for (int l = 0; l < c; l++) {
                if (e.getSource().equals(bottoni[k][l])) {
                    if(e.isShiftDown()){
                        if(bottoni[k][l].getBackground()!=black){
                            bottoni[k][l].setBackground(black);
                            k=r;
                            l=c;
                        }
                    }else if(e.isAltDown() && bottoni[k][l].getBackground()==black){
                        bottoni[k][l].setBackground(BDefault);
                        k=r;
                        l=c;
                    }

                }
            }
        }

    }






}
