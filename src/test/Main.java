package test;
// A simple test class to demonstrate the TeleOpController functionality with real-time keyboard input simulation.
import control.TeleOpController;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    private static TeleOpController controller = new TeleOpController(0.05, 3);
    private static volatile double x = 0.0;
    private static volatile double y = 0.0;
    private static volatile double rotation = 0.0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Keyboard Input Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            frame.setVisible(true);

            frame.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    switch (key) {
                        case KeyEvent.VK_W:
                            y = 1.0;
                            break;
                        case KeyEvent.VK_S:
                            y = -1.0;
                            break;
                        case KeyEvent.VK_A:
                            x = -1.0;
                            break;
                        case KeyEvent.VK_D:
                            x = 1.0;
                            break;
                        case KeyEvent.VK_Q:
                            rotation = -1.0;
                            break;
                        case KeyEvent.VK_E:
                            rotation = 1.0;
                            break;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    int key = e.getKeyCode();
                    switch (key) {
                        case KeyEvent.VK_W:
                        case KeyEvent.VK_S:
                            y = 0.0;
                            break;
                        case KeyEvent.VK_A:
                        case KeyEvent.VK_D:
                            x = 0.0;
                            break;
                        case KeyEvent.VK_Q:
                        case KeyEvent.VK_E:
                            rotation = 0.0;
                            break;
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                    // Not used
                }
            });

            // Start a thread to continuously update and print powers
            Thread updateThread = new Thread(() -> {
                while (true) {
                    double[] powers = controller.update(x, y, rotation);
                    System.out.printf("\rFront Left:  %.3f  Front Right: %.3f  Back Left:   %.3f  Back Right:  %.3f", powers[0], powers[1], powers[2], powers[3]);
                    try {
                        Thread.sleep(100); // Update every 100ms
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            updateThread.setDaemon(true);
            updateThread.start();
        });
    }
}