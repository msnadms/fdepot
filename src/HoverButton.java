import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverButton extends JButton {

    private final Color c;
    private final Color tc;

    public HoverButton(ImageIcon img, Color clr, Color tclr) {
        super(img);
        setBackground(clr);
        c = clr;
        tc = tclr;
        ml();
    }
    public HoverButton(String s, Color clr, Color tclr) {
        super(s);
        setBackground(clr);
        c = clr;
        tc = tclr;
        ml();
    }
    public void ml() {
        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setBackground(tc);
            }

            public void mouseExited(MouseEvent evt) {
                setBackground(c);
            }

            public void mousePressed(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.BLACK));
                super.mousePressed(e);
            }

            public void mouseReleased(MouseEvent e) {
                setBorder(BorderFactory.createEmptyBorder());
                super.mouseReleased(e);
            }
        });
    }
}