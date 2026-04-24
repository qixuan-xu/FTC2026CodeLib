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
                            if (x < 1.0) {
                                x += 0.05;
                            } // 前进加速
                            break;
                        case KeyEvent.VK_S:
                            if (x > -1.0) {
                                x -= 0.05;
                            } // 后退加速
                            break;
                        case KeyEvent.VK_A:
                            if (y > -1.0) {
                                y -= 0.05;
                            } // 左移加速
                            break;
                        case KeyEvent.VK_D:
                            if (y < 1.0) {
                                y += 0.05;
                            } // 右移加速
                            break;
                        case KeyEvent.VK_Q:
                            if (rotation > -1.0) {
                                rotation -= 0.05;
                            } // 左转加速
                            break;
                        case KeyEvent.VK_E:
                            if (rotation < 1.0) {
                                rotation += 0.05;
                            } // 右转加速
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
                    System.out.print("\033[H"); // Move cursor to top left
                    System.out.printf("FL: %.3f FR: %.3f\nBL: %.3f BR: %.3f\n", powers[0], powers[1], powers[2], powers[3]);
                    try {
                        Thread.sleep(500); // Update every 500ms
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