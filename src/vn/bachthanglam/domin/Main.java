package vn.bachthanglam.domin;

import java.awt.EventQueue;
import javax.swing.UIManager;

/**
 * Class chính của chương trình
 * File thực thi sẽ gọi hàm main của class này đầu tiên
 * 
 * @author thanbaiks
 */
public class Main {

    /**
     * Hàm chính
     * 
     * @param args tham số truyền vào từ command line
     */
    public static void main(String[] args) {
        /**
         * Đặt lại look and feel cho java swing
         * ở đây sử dụng System LAF
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {/* Bỏ qua */}
        /**
         * Mở Form chọn mức độ
         * Sử dụng EventQueue.invokeLater để show form từ 1 thread khác
         * Đảm bảo luồng chính chạy ổn định
         */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormModeSelector().initAndShow();
            }
        });
    }
}
