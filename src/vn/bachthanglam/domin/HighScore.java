package vn.bachthanglam.domin;

import java.io.*;
import java.util.*;
import javax.swing.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

/**
 * Lớp quản lý điểm cao
 */
public class HighScore {
    private final String level;
    private List<Score> scores;
    
    /**
     * Hàm trả về Obj HighScore tương ứng với level
     * @param level cấp độ khó
     * @return      HighScore
     */
    public static HighScore getHighScore(String level) {
        return new HighScore(level);
    }
    
    /**
     * Hàm Khởi tạo đối tượng HighScore
     * @param level cấp độ khó
     */
    private HighScore(String level){
        this.level = level;
        scores = new ArrayList<>();
        File file = new File("hiscores.json");
        if (file.exists()) {
            try {
                FileReader reader = new FileReader(file);
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(reader);
                if (root.containsKey(level)) {
                    JSONArray jsa = (JSONArray) root.get(level);
                    for (Object o : jsa) {
                        JSONObject obj = (JSONObject) o;
                        scores.add(new Score((int)(long)obj.get("score"), (String)obj.get("name")));
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Hàm ghi nhận kết quả 
     * @param score điểm số của game
     */
    public void scored(int score) {
        // Kiểm tra xem đã có đủ tối đa 5 Điểm cao hoặc là điểm này thuộc 
        // Top điểm cao thì ghi vào file
        if (scores.size() < 5 || score < scores.get(4).score) {
            // Ghi vào file
            String name = JOptionPane.showInputDialog(null, "Nhập tên của bạn", "Điểm cao", JOptionPane.INFORMATION_MESSAGE);
            if (name == null) name = "UNNAMED";
            
            if (scores.isEmpty() || score >= scores.get(scores.size()-1).score)
                scores.add(new Score(score, name));
            else
                for (int i=0;i<scores.size();i++) {
                    if (scores.get(i).score > score) {
                        scores.add(i, new Score(score, name));
                        break;
                    }
                }
            
            if (scores.size() > 5) {
                scores.remove(5);
            }
            // Write to file
            File file = new File("hiscores.json");
            JSONObject jso = new JSONObject();
            if (file.exists()) {
                try {
                    FileReader reader = new FileReader(file);
                    JSONParser parser = new JSONParser();
                    jso = (JSONObject) parser.parse(reader);
                    reader.close();
                } catch (Exception e) {
                }
            }
            JSONArray jsa = new JSONArray();
            for (Score sc : scores) {
                JSONObject o = new JSONObject();
                o.put("name", sc.name);
                o.put("score", sc.score);
                jsa.add(o);
            }
            jso.put(level, jsa);
            try (PrintStream out = new PrintStream(file)) {
                out.println(jso.toJSONString());
                out.close();
            } catch (FileNotFoundException ex) {
                
            }
        }
    }

    /**
     * Hiện thị MessageBox điểm cao hoặc hiển thị thông báo nếu chưa có điểm 
     * cao ở mức độ khó này
     */
    public void show() {
        String msg;
        if (scores.isEmpty()) {
            msg = "Chưa có kỷ lục nào cho độ khó này";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            sb.append("Mức độ: <b color='red'>").append(level).append("</b><br>");
            for (Score score : scores) {
                sb.append("<b>").append(score.name).append("</b>").append(": ").append(String.valueOf(score.score)).append("<br>");
            }
            sb.append("</html>");
            msg = sb.toString();
        }
        JOptionPane.showMessageDialog(null, msg);
    }
    
}

/**
 * Lớp điểm
 */
class Score {
    public int score;
    public String name;

    public Score(int score, String name) {
        this.score = score;
        this.name = name;
    }
}