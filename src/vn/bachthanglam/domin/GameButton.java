package vn.bachthanglam.domin;

import java.awt.*;
import javax.swing.*;

/**
 * Lớp GameButton kế thừa từ JButton
 * Mỗi object tương ứng với 1 ô trong ma trận
 */
class GameButton extends JButton {
    /**
     * Enum chứa các trạng thái của ô này
     */
    private static enum ButtonState {
        /**
         * Trạng thái đã cắm cờ
         * Không cho phép đánh vào ô này
         */
        FLAGGED,
        /**
         * Trạng thái chưa lật mở
         */
        NOT_REVEALED,
        /**
         * Trạng thái đã được lật mở
         */
        REVEALED
    }
    
    /**
     * File ảnh mìn và cờ
     */
    private static final ImageIcon IC_MINE = new ImageIcon("".getClass().getResource("/assets/mine.png"));
    private static final ImageIcon IC_FLAG = new ImageIcon("".getClass().getResource("/assets/flag.png"));
    
    /**
     * Hằng chỉ định ô này là ô chứa mìn
     */
    public static final int CELL_MINE = -1;
    /**
     * Hằng chỉ định ô này là ô trống
     */
    public static final int CELL_EMPTY = 0;
    /**
     * Tọa độ x của ô này trên ma trận
     */
    private final int x;
    /**
     * Tọa độ y của ô này trên ma trận
     */
    private final int y;
    /**
     * Biến trạng thái của ô này
     */
    private ButtonState state;
    /**
     * Biến lưu giá trị của ô
     */
    private int value;

    /**
     * Hàm khởi tạo ô
     * Thiết lập các thông số của ô
     * @param x tọa độ X (hàng) của ô
     * @param y tọa độ Y (cột) của ô
     */
    public GameButton(int x, int y) {
        super();
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        setBorder(BorderFactory.createEmptyBorder());
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(20, 20));
        setOpaque(true);
        this.x = x;
        this.y = y;
    }

    /**
     * Hàm thêm/xóa cờ của ô
     * Cập nhật icon của ô theo giá trị cờ
     * Và trạng thái của ô
     */
    public void toggleFlag() {
        if (state == ButtonState.FLAGGED) {
            state = ButtonState.NOT_REVEALED;
            setIcon(null);
        } else {
            state = ButtonState.FLAGGED;
            setIcon(IC_FLAG);
        }
    }

    /**
     * Hàm đặt giá trị cho ô
     * @param i giá trị mới của ô
     */
    public void setValue(int i) {
        value = i;
    }

    /**
     * Hàm kiểm tra ô này có phải ô mìn không
     * @return boolean
     */
    public boolean isMine() {
        return value == CELL_MINE;
    }

    /**
     * Hàm kiểm tra ô này có phải ô số không
     * @return boolean
     */
    public boolean isNumber() {
        return value > 0;
    }
    
    /**
     * Hàm kiểm tra ô này có phải ô trống hay không
     * @return boolean
     */
    public boolean isEmptyCell() {
        return value == CELL_EMPTY;
    }

    /**
     * Hàm tăng giá trị của ô này lên 1 đơn vị
     */
    public void inc() {
        if (!isMine()) {
            value++;
        }
    }

    /**
     * Hàm kiểm tra ô này đã được lật mở hay chưa
     * @return boolean
     */
    public boolean isRevealed() {
        return state == ButtonState.REVEALED;
    }

    /**
     * Hàm kiểm tra ô này có được cắm cờ hay chưa
     * @return boolean
     */
    public boolean isFlagged() {
        return state == ButtonState.FLAGGED;
    }

    /**
     * Hàm lấy tọa độ X của ô
     * @return tọa độ X của ô
     */
    public int getPosX() {
        return x;
    }

    /**
     * Hàm lấy tọa độ Y của ô
     * @return tọa độ Y của ô
     */
    public int getPosY() {
        return y;
    }

    /**
     * Hàm lật mở ô
     * Hiển thị ô theo giá trị của ô này
     */
    public void reveal() {
        if (isMine()) {
            // Nếu đây là ô mìn
            // Hiển thị icon mìn
            setIcon(IC_MINE);
        } else if (isNumber()) {
            // Nếu đây là ô số
            // Gọi hàm setText
            // Và Màu chỉ thị dựa trên giá trị của ô
            setText(String.valueOf(value));
            switch (value) {
                case 1:
                    setForeground(Color.BLUE);
                    break;
                case 2:
                    setForeground(Color.DARK_GRAY);
                    break;
                default:
                    setForeground(Color.RED);
                    break;
            }
        }
        // Thay đổi diện mạo của ô
        setSelected(true);
        // Thay đổi trạng thái của GameButton
        state = ButtonState.REVEALED;
    }

    /**
     * Hàm đưa giá trị của GameButton về trạng thái ban đầu
     */
    public void reset() {
        value = 0;
        state = ButtonState.NOT_REVEALED;
        setForeground(null);
        setSelected(false);
        setIcon(null);
        setText("");
    }
    
}
