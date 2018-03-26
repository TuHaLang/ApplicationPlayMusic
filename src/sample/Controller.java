package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;


public class Controller implements Initializable{


    @FXML private Button btnPlay, btnPause, btnBack, btnNext;
    @FXML private Button btnChoose, btnSound, btnSoundMin, btnSoundMax, btnSoundMute;
    @FXML private Label lblNameMusic = new Label();
    @FXML private ListView<String> listView = new ListView<>();
    @FXML private Pane pane = new Pane();
    @FXML Slider sliderTime = new Slider();
    @FXML Label lblTotalTime = new Label();
    @FXML Label lblCurentTime = new Label();
    @FXML Slider sliderVolume = new Slider();


    static Media media = new Media(Function.getListFile(new File("Music")).get(0));
    static MediaPlayer player = new MediaPlayer(media);
    static ArrayList<String> listMusic = Function.getListFile(new File("Music"));

    //tìm kiếm vị trí bài hát trong list nhạc
    private int searchIndexFromList(String URIMusic){
        for(int i=0; i<listMusic.size(); i++){
            if(URIMusic.equals(listMusic.get(i))) return i;
        }
        return -1;
    }

    //Tự động next bài hát
    private void autoNextSong(){
        if(sliderTime.getValue()==sliderTime.getMax()){
            int index = this.searchIndexFromList(player.getMedia().getSource());
            if(index < listMusic.size()-1){
                media = new Media(listMusic.get(index + 1));
                player.stop();
                btnPlay.setVisible(false);
                player = new MediaPlayer(media);
                player.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        start();
                    }
                });
                lblNameMusic.setText(Function.URItoName(listMusic.get(index+1)));
                btnPause.setVisible(true);
            }
            else{
                media = new Media(listMusic.get(0));
                player.stop();
                btnPlay.setVisible(false);
                player = new MediaPlayer(media);
                player.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        start();
                    }
                });
                lblNameMusic.setText(Function.URItoName(listMusic.get(0)));
                btnPause.setVisible(true);
            }
        }
    }

    //Chuyển đến màn hình có danh sách bài hát để chọn bài
    public void buttonBack(){
        //Đảo button
        btnBack.setVisible(false);
        btnNext.setVisible(true);
        btnChoose.setVisible(true);
        btnSound.setVisible(false);
        btnSoundMax.setVisible(false);
        btnSoundMin.setVisible(false);
        btnSoundMute.setVisible(false);

        //Thay đổi màn hình
        pane.setVisible(false);
        listView.setVisible(true);
    }

    //Chuyển đến màn hình nghe nhạc
    public void buttonNext(){

        //Đảo button
        btnBack.setVisible(true);
        btnNext.setVisible(false);
        btnChoose.setVisible(false);
        setShowVolume();

        //Thay đổi màn hình hiển thị
        listView.setVisible(false);
        pane.setVisible(true);
    }

    //sự kiện chọn folder nhạc
    public void ChooseFile(ActionEvent event){
        File folder = Function.getNameDirectory();
        if(folder != null){
            listMusic = Function.getListFile(folder);
            listView.setItems((FXCollections.observableArrayList(Function.URItoName(listMusic))));
        }
    }

    //Phát nhạc và chạy slidertime
    public void start(){
        double duration = player.getMedia().getDuration().toSeconds();
        sliderTime.setValue(player.getCurrentTime().toSeconds());
        player.currentTimeProperty().addListener(((observable, oldValue, newValue) -> {
            lblCurentTime.setText(Function.formatTime(newValue.toSeconds()));
            sliderTime.setValue(newValue.toSeconds());
            autoNextSong();
        }));
        sliderTime.setMin(0);
        sliderTime.setMax(duration);
        lblTotalTime.setText(Function.formatTime(duration));
        player.play();
    }

    public void btnVolume(){
        sliderVolume.setVisible(true);
    }

    //Cài đặt hiển thị mức độ loa
    private void setShowVolume(){
        if(sliderVolume.getValue() >= 0.75){
            btnSoundMax.setVisible(true);
            btnSound.setVisible(false);
            btnSoundMin.setVisible(false);
            btnSoundMute.setVisible(false);
            sliderVolume.setVisible(false);
        }else{
            if(sliderVolume.getValue() >= 0.4 && sliderVolume.getValue() < 0.75){
                btnSoundMax.setVisible(false);
                btnSound.setVisible(true);
                btnSoundMin.setVisible(false);
                btnSoundMute.setVisible(false);
                sliderVolume.setVisible(false);
            }else{
                if(sliderVolume.getValue() > 0 && sliderVolume.getValue() < 0.4){
                    btnSoundMax.setVisible(false);
                    btnSound.setVisible(false);
                    btnSoundMin.setVisible(true);
                    btnSoundMute.setVisible(false);
                    sliderVolume.setVisible(false);
                }else{
                    if(sliderVolume.getValue()==0){
                        btnSoundMax.setVisible(false);
                        btnSound.setVisible(false);
                        btnSoundMin.setVisible(false);
                        btnSoundMute.setVisible(true);
                        sliderVolume.setVisible(false);
                    }
                }
            }
        }
    }

    //Bắt sự kiện tăng giảm volume
    public  void slidervolume(MouseEvent event){
        player.setVolume(sliderVolume.getValue());
        setShowVolume();
    }

    //Bắt sự kiện khi nhấn chuột slider time
    public void slidertime(MouseEvent event){
        //tua nhạc đến vị trí tương ứng
        player.seek(Duration.seconds(sliderTime.getValue()));

    }

    //Bắt đầu phát nhạc
    public void Play(ActionEvent event){
        start();
        btnPlay.setVisible(false);
        btnPause.setVisible(true);
    }

    //Tạm dừng phát nhạc
    public void Pause(ActionEvent event){
        player.pause();
        btnPause.setVisible(false);
        btnPlay.setVisible(true);

    }

    //Tua nhạc, mỗi lần nhấn button sẽ tăng 1s
    public void SeekN(ActionEvent event){

        //Lấy thời gian kể từ lúc phát bài hát
        double startTime = player.getCurrentTime().toSeconds();

        //Dịch chuyển đến vị trí mới để phát nhạc
        player.seek(Duration.seconds(1+startTime));
    }

    //Tua nhạc, mỗi lần nhấn button sẽ giảm 1s
    public void SeekB(ActionEvent event){

        //Lấy thời gian kể từ lúc phát nhạc
        double startTime = player.getCurrentTime().toSeconds();

        //Nếu thời gian tuwfw luc phát < 1s thì sẽ phát lại từ đầu, không thì sẽ lùi 1s
        if(startTime - 1 >0){
            player.seek(Duration.seconds(-1+startTime));
        }
        else{
            player.seek(Duration.millis(0));
        }

    }

    //Chuyển bài nhạc tiếm theo trong list nhạc
    public void Next(ActionEvent event){
        int index = this.searchIndexFromList(player.getMedia().getSource());
        if(index < listMusic.size()-1){
            media = new Media(listMusic.get(index + 1));
            player.stop();
            btnPlay.setVisible(false);
            player = new MediaPlayer(media);
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });
            lblNameMusic.setText(Function.URItoName(listMusic.get(index+1)));
            btnPause.setVisible(true);
        }
        else{
            media = new Media(listMusic.get(0));
            player.stop();
            btnPlay.setVisible(false);
            player = new MediaPlayer(media);
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });
            lblNameMusic.setText(Function.URItoName(listMusic.get(0)));
            btnPause.setVisible(true);
        }
    }

    //Chuyển bài lùi
    public void Back(ActionEvent event){
        //lấy index bài nhạc đang phát trong listMusic
        int index = this.searchIndexFromList(player.getMedia().getSource());
        //nều index > 0 thì phát lùi lại 1 bài không thì phát bài cuối cùng trong listmusic
        if(index > 0){
            //khởi tạo media mới
            media = new Media(listMusic.get(index - 1));
            //dừng bài nhạc đang phát
            player.stop();

            //Đổi icon button
            btnPlay.setVisible(false);
            btnPause.setVisible(true);

            //phát nhạc
            player = new MediaPlayer(media);
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });

            //hiển thị tên bài đang hát
            lblNameMusic.setText(Function.URItoName(listMusic.get(index - 1)));

        }
        else{ //Phát bài nhạc cuối cùng của list
            media = new Media(listMusic.get(listMusic.size()-1));
            player.stop();
            btnPlay.setVisible(false);
            player = new MediaPlayer(media);
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });
            lblNameMusic.setText(Function.URItoName(listMusic.get(listMusic.size()-1)));
            btnPause.setVisible(true);
        }
    }


    //Bắt sự kiện khi chọn bài hát trên listview
    public void listViewEven(MouseEvent event){
        int index = listView.getSelectionModel().getSelectedIndex();
        media = new Media(listMusic.get(index));
        player.stop();
        btnPlay.setVisible(false);
        player = new MediaPlayer(media);
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                start();
            }
        });
        lblNameMusic.setText(Function.URItoName(listMusic.get(index)));
        btnPause.setVisible(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(listMusic.size() > 0){
            listView.setItems(FXCollections.observableArrayList(Function.URItoName(listMusic)));
        }
        Media media1 = player.getMedia();
        lblNameMusic.setText(Function.URItoName(media1.getSource()));
        lblTotalTime.setText("00:00");
        lblCurentTime.setText("00:00");
        sliderVolume.setMin(0);
        sliderVolume.setMax(1);
        sliderVolume.setValue(1);
    }
}
