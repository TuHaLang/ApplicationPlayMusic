package sample;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Function {

    //Chọn directory
    public static File getNameDirectory() {
        //khởi tạo directory
        DirectoryChooser directoryChooser = new DirectoryChooser();
        //set title
        directoryChooser.setTitle("Select directory music");
        //hiển thị thư mục đầu tiên nhìn thấy
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        //Tạo stage để hiển thị
        Stage stage = new Stage();
        //Lấy directory được chọn
        File directory = directoryChooser.showDialog(stage);
        //trả về directory được chọn
        return directory;
    }

    //Trả về list file nhạc từ directory
    public static ArrayList<String> getListFile(File directory){
        if(directory != null){
            File[] listFile = directory.listFiles();
            ArrayList<String> list = new ArrayList<>();
            for(File file : listFile){
                if(file.toURI().toString().matches(".+.mp3$")){
                    list.add(file.toURI().toString());
                }
            }
            return list;
        }
        return null;
    }

    //Chuyển từ URI sang tên bài hát trả về 1 list
    public static ArrayList<String> URItoName(ArrayList<String> listURI){
        ArrayList<String> listName = new ArrayList<>();
        for(String uri : listURI){
            //Tách chuỗi chỉ lẫy tên bài hát
            String[] list = uri.split("/");
            listName.add(list[list.length-1]);
        }
        return listName;
    }

    //chuyển uri sang tên trả về 1 tên
    public static String URItoName(String URI){
        String[] list = URI.split("/");
        return list[list.length-1];
    }

    //Định dạng thời gian hiển thị
    public static String formatTime(Double seconds){
        int minute = (int) (seconds/60);
        int second = (int) (seconds%60);
        return minute + ":" + second;
    }
}
