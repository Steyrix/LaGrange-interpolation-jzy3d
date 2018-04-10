import javax.swing.*;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            final MainForm form = new MainForm();
            form.setVisible(true);
        });
    }
}
