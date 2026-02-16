package projectoprograii;

import javax.swing.*;

public class ProjectoPrograII {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(() -> {
            Menuinicio menu = new Menuinicio();
            menu.setVisible(true);
        });
    }
}
