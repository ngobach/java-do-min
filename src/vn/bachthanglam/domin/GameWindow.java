
package vn.bachthanglam.domin;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
/**
 * Lớp GameWindow
 * 
 */
public class GameWindow extends JFrame {
    
    /**
     * Kích thước và số mìn của bản đồ
     */
    private final int size, mines;
    /**
     * JLabel đếm số bom còn lại chưa được cắm cờ
     */
    private JLabel flagCounter;
    /**
     * Ma trận GameButton
     */
    private GameButton[][] buttons;
    /**
     * Biến đếm số cờ còn lại
     */
    private int flagLeft;
    /**
     * Biến đếm số ô chưa được mở
     */
    private int cellLeft;
    /**
     * Biến kiểm tra trạng thái của game đã kết thúc hay chưa
     */
    private boolean gameEnded;
    
    /**
     * Hàm khởi tạo GameWindow
     * @param size      Kích thước của game size * size ô
     * @param mines     Số mìn trong bản đồ game
     * @throws HeadlessException 
     */
    private GameWindow(int size, int mines) throws HeadlessException {
        super("Dò mìn!");
        this.size = size;
        this.mines = mines;
        init();
    }
    
    /**
     * Hàm xây dựng một đối tượng game window
     * 
     * @return đối tượng GameWindow được xây dựng
     */
    public static GameWindow createGameWindow(int size, int mines) {
        return new GameWindow(size, mines);
    }
    
    /**
     * Hàm xây dựng giao diện chương trình của GameWindow
     * Được gọi khi khởi tạo form
     */
    private void init() {
        // Container chứa toàn bộ nội dung form
        JPanel container = new JPanel(new BorderLayout());
        
        // Toolbar chứa bộ đếm mìn và button "Chơi lại"
        JPanel toolbar = new JPanel(new GridLayout(1, 2));
        // Khởi tạo flagCounter
        flagCounter = new JLabel(String.valueOf(mines));
        flagCounter.setFont(new Font(Font.DIALOG,Font.BOLD, 16));
        flagCounter.setForeground(Color.red);
        toolbar.add(flagCounter);
        
        // Khởi tạo button chơi lại
        JButton newGameButton = new JButton("Chơi lại");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
        toolbar.add(newGameButton);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Đẩy toolbar vào container
        // Vịt trí ở trên cùng
        container.add(toolbar, BorderLayout.NORTH);
        
        // JPanel chứa các GameButton
        // Mỗi GameButton tương ứng với một ô mìn
        JPanel gameBoard = new JPanel(new GridLayout(size, size));
        // Khởi tạo ma trận GameButtons
        buttons = new GameButton[size][size];
        // Khởi tạo giá trị cho từng button trong ma trận
        for (int i=0;i<size;i++) {
            for (int j=0;j<size;j++){
                // Mỗi button sẽ chứa thêm thông tin là vị trí ô đó trên ma trận
                // Khi nhận sự kiện click sẽ biết được ô bị click ở vị trí nào
                buttons[i][j] = new GameButton(i, j);
                buttons[i][j].addMouseListener(new MouseListener());
                // Đẩy Button vào bàn chơi (gameBoard)
                gameBoard.add(buttons[i][j]);
            }
        }
        // Đẩy gameboard vào container
        container.add(gameBoard);
        
        // Thiết lập kích thước và các tùy chỉnh khác cho JFrame
        getContentPane().add(container);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Hiển thị Form
        setVisible(true);
        
        // Bắt đầu 1 game mới
        newGame();
    }
    
    /**
     * Hàm trả về 1 ô theo vị trí mong muốn
     * @param x tọa độ x (hàng)
     * @param y tọa độ y (cột)
     * @return GameButton obj nếu ô đó tồn tại hoặc null nếu ngược lại
     */
    private GameButton getCell(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) return null;
        return buttons[x][y];
    }
    
    /**
     * Hàm thiết lập game về giá trị mặc định
     * Bắt đầu một ván chơi mới
     */
    private void newGame() {
        // Lập danh sách các buttons có trong bàn
        // Để có thể lấy ngẫu nhiên các ô trong đó
        // Biến chúng trở thành ô chứa bom
        // Đồng thời đưa giá trị của gamebutton về mặc định
        ArrayList<GameButton> list = new ArrayList<>();
        for (GameButton[] gbs : buttons) {
            for (GameButton gb : gbs) {
                gb.reset();
                list.add(gb);
            }
        }
        
        // List chứa các ô được chọn để chứa bom
        // Các ô được chọn ngẫu nhiên
        ArrayList<GameButton> selected = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < mines; i++) {
            int id = rand.nextInt(list.size());
            // Đẩy ô ngẫu nhiên tìm được vào list "selected"
            selected.add(list.get(id));
            // Và xóa ô đó khỏi "list"
            list.remove(id);
        }
        
        // Thiết lập các ô được chọn trở thành ô chứa bom
        for (GameButton gb : selected) {
            gb.setValue(GameButton.CELL_MINE);
        }
        
        // Thiết lập lại ô số cho các ô xung quanh ô chứa bom
        for (GameButton gb : selected) {
            // Duyệt 8 ô kề đỉnh và kề cạnh vơi ô chứa bom
            // Tăng giá trị của mỗi ô đó lên 1 đơn vị
            for (int i=-1;i<=1;i++) for (int j=-1;j<=1;j++) {
                // Kiểm tra xem nếu là chính ô đó thì bỏ qua
                if ((i|j)==0) continue;
                // Thử lấy ô ở vị trí gb.getPosX() + i, gb.getPosY() + j
                GameButton cell = getCell(gb.getPosX() + i, gb.getPosY() + j);
                // Nếu ô tìm được không phải null thì tăng giá trị ô đó lên 1 đơn vị
                // Nếu là null tức là ô ở vị trí đó không tìm được, bỏ qua
                if (cell != null) cell.inc();
            }
        }
        // Đặt giá trị cho flagLeft chính là số mìn ban đầu
        flagLeft = mines;
        flagCounter.setText(String.valueOf(flagLeft));
        // Trạng thái game kết thúc là false
        gameEnded = false;
        // Số ô chưa mở chính bằng kích thước bản đồ trừ đi số mìn
        cellLeft = size*size - mines;
    }

    /**
     * Hàm lật mở 1 ô trong ma trận
     * Nếu ô đó không tồn tại hoặc đã được mở rồi, thì bỏ qua
     * Nếu ô đó đã cắm cờ thì xóa cờ đó đi, trả lại giá trị cho flagCounter
     * 
     * Các trường hợp mở ô:
     * +    Ô số: hiển thị ô đó
     * +    Ô trống: hiển thị ô đó, và đồng thời gọi hàm reveal các ô xung quanh
     *          theo thuật toán tìm kiếm theo chiều sâu (DFS)
     * +    Ô đó chứa mìn: kết thúc game, thông báo "Thua!"
     * 
     * @param x tọa độ X của ô cần lật mở (chỉ số hàng)
     * @param y tọa độ Y của ô cần lật mở (chỉ số cột)
     */
    private void reveal(int x, int y) {
        // Lấy ô ở vị trí được chỉ định
        GameButton gb = getCell(x, y);
        // Kiểm tra ô đó có tồn tại hoặc đã được mở rồi, thì kết thúc hàm
        if (gb == null || gb.isRevealed()) return;
        // Kiểm tra xem nếu ô đó đang được cắm cờ thì xóa cờ
        if (gb.isFlagged()){
            // Remove flag
            gb.toggleFlag();
            flagLeft++;
            flagCounter.setText(String.valueOf(flagLeft));
        }
        // Gọi hàm reveal() của GameButton
        gb.reveal();
        // Xét trường hợp ô đó chứa mìn
        if (gb.isMine()) {
            // BANG!
            revealAll();
            flagCounter.setText("Thua!");
            gameEnded = true;
            return;
        }
        // Xét trường hợp ô đó là ô trống
        // Tiếp tục lật mở các ô xung quanh
        if (gb.isEmptyCell()) {
            for (int i=-1;i<=1;i++) for (int j=-1;j<=1;j++) {
                if ((i|j)==0) continue; // That is this cell
                reveal(x+i, y+j);
            }
        }
        // Giảm biến cellLeft
        cellLeft--;
        // Kiểm tra người chơi đã chiến thắng chưa
        // Khi cellLeft bằng không
        if (cellLeft == 0) {
            flagCounter.setText("Thắng!");
            gameEnded = true;
        }
    }

    /**
     * Hàm lật mở tất cả các ô
     * Khi người chơi mở phải ô mìn
     */
    private void revealAll() {
        for (GameButton[] gbs : buttons) {
            for (GameButton gb : gbs) {
                if (gb.isRevealed()) continue;
                if (gb.isFlagged()) gb.toggleFlag();;
                gb.reveal();
            }
        }
    }

    /**
     * Lớp lắng nghe sự kiện chuột của GameButton
     */
    private class MouseListener extends MouseAdapter {

        /**
         * Hàm lắng nghe sự kiện click của chuột
         * @param e thông tin của sự kiện
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            // Kiểm tra nếu game đã kết thúc, thì bỏ qua sự kiện
            if (gameEnded) return;
            
            // Lấy GameButton đã thực hiện sự kiện
            GameButton gb = (GameButton) e.getSource();
            // Kiểm tra nếu ô đó đã được lật mở thì kết thúc hàm
            if (gb.isRevealed()) return;
            
            // Kiểm tra xem button nào được click
            if (e.getButton() == 1 && !gb.isFlagged()) {
                // Nếu ô đó là chuột trái, và chưa gắn cờ, gọi hàm reveal mở ô đó
                reveal(gb.getPosX(), gb.getPosY());
            } else if (e.getButton() == 3){
                // Nếu ô đó là chuột phải, xóa/thêm cờ cho ô
                if (!gb.isFlagged() && flagLeft > 0) {
                    // Thêm cờ cho ô
                    gb.toggleFlag();
                    flagLeft--;
                } else if (gb.isFlagged()) {
                    // Xóa cờ cho ô
                    gb.toggleFlag();
                    flagLeft++;
                }
                // Cập nhật flagCounter theo biến flagLeft
                flagCounter.setText(String.valueOf(flagLeft));
            }
        }
        
    }
}

