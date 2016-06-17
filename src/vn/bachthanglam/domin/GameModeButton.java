/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.bachthanglam.domin;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * Lớp kế thừa JButton
 * Chứa các thông tin phụ khác như kích thước mong muốn của game
 * Và số mìn trong game
 */
class GameModeButton extends JButton {
    /**
     * Biến chứa kích thước
     */
    public int size;
    /**
     * Biến chứa số mìn
     */
    public int mines;
    /**
     * Biến cấp độ khó
     */
    public String level;
    
    /**
     * Hàm Khởi tạo
     * @param text      Văn bản hiển thị của JButton
     * @param level     Mức độ khó
     * @param size      Kích thước mong muốn của game
     * @param mines     Số mìn trong game
     * @param listener  Đối tượng lắng nghe sự kiện click của JButton
     */
    public GameModeButton(String text,String level, int size, int mines, ActionListener listener) {
        super(text);
        this.size = size;
        this.mines = mines;
        this.level = level;
        
        // Thay đổi font, kích thước, thêm đối tượng lắng nghe sự kiện
        addActionListener(listener);
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        setPreferredSize(new Dimension(200, 50));
    }
    
}
