package vn.bachthanglam.domin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Form chọn mức độ khó của game
 * Có 3 mức độ khó là:
 * 
 * +    Dễ
 * +    Trung Bình
 * +    Khó
 */
public class FormModeSelector extends JFrame implements ActionListener {

    /**
     * Biến chứa ảnh nền của Form
     * Sẽ load khi form được khởi tạo
     */
    private Image bgImage;
    
    /**
     * Hàm khởi tạo form
     * 
     * @throws HeadlessException Môi trường đang sử dụng là Headless (CLI)
     */
    public FormModeSelector() throws HeadlessException {
        super("Lựa chọn cấp độ");
        try {
            bgImage = ImageIO.read(getClass().getResource("/assets/bg.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Xây dựng nội dung của Form
     * Và hiển thị lên màn hình
     */
    public void initAndShow() {
        // JPanel chứa nội dung của form
        // Sử dụng GridLayout với 1 hàng 1 cột
        // Khoảng cách giữa các ô 20 pixels
        // Override hàm paintComponent để vẽ Background
        JPanel panel = new JPanel(new GridLayout(0, 1, 20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(bgImage, 0, 0, null);
            }
        };
        
        // Hiển thị các label phụ
        JLabel label = new JLabel("Lựa chọn cấp độ");
        label.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        panel.add(new GameModeButton("Dễ", 9, 10, this));
        panel.add(new GameModeButton("Trung Bình", 16, 40,this));
        panel.add(new GameModeButton("Khó", 24, 99, this));
        panel.add(new JLabel("<html><center>Game được xây dựng bởi<br><b>Ngô Xuân Bách</b>, <b>Đào Quang Thắng</b> và <b>Vũ Văn Lâm</b></center></html>"));
        panel.setBorder(new EmptyBorder(210,60,50,60));
        
        // Đẩy panel vào Form
        getContentPane().add(panel);
        
        // Tự động resize children theo kích thước mong muốn của chúng (prefered dismension)
        pack();
        // Thoát khi đóng form
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Vô hiệu hóa chức năng thay đổi kích thước form (resizing)
        setResizable(false);
        // Đặt vị trí của form ra giữa màn hình
        setLocationRelativeTo(null);
        // Và cuối cùng là hiển thị form lên màn hình
        setVisible(true);
    }

    /**
     * Hàm sử lý sự kiện button được click
     * Của GameModeButton
     * Xây dựng GameWindow tương ứng với mức độ được chọn
     * Và hiển thị GameWindow
     * 
     * @param e thông tin của sự kiện
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Kiểm tra xem GameModeButton nào được click
        GameModeButton btn = (GameModeButton) e.getSource();
        // Xây dựng GameWindow
        GameWindow game = GameWindow.createGameWindow(btn.size, btn.mines);
        // Hiển thị GameWindow
        game.setVisible(true);
        // Ẩn form này đi
        setVisible(false);
        // Lắng nghe sự kiện GameWindow đóng,
        // Khi đó cho form này hiển thị trở lại
        game.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });
    }
}

