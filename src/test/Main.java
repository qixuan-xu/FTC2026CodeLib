package test;
// A simple test class to demonstrate the TeleOpController functionality with real-time keyboard input simulation.
import control.TeleOpController;
import utils.Constants;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Main {
    private static TeleOpController controller = new TeleOpController(Constants.DEADZONE, Constants.CURVE_POWER);
    private static volatile double x = 0.0;
    private static volatile double y = 0.0;
    private static volatile double rotation = 0.0;
    private static volatile long lastXTime = 0;
    private static volatile long lastYTime = 0;
    private static volatile long lastRotationTime = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Keyboard Input Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(null); // Use absolute layout for simplicity

            JLabel label = new JLabel("FL: 0.000 FR: 0.000\nBL: 0.000 BR: 0.000");
            label.setBounds(10, 10, 380, 100);
            frame.add(label);

            frame.setVisible(true);

            frame.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    long now = System.currentTimeMillis();
                    switch (key) {
                        case KeyEvent.VK_W:
                            if (x < 1.0) {
                                x += 0.05;
                            } // 前进加速
                            lastXTime = now;
                            break;
                        case KeyEvent.VK_S:
                            if (x > -1.0) {
                                x -= 0.05;
                            } // 后退加速
                            lastXTime = now;
                            break;
                        case KeyEvent.VK_A:
                            if (y > -1.0) {
                                y -= 0.05;
                            } // 左移加速
                            lastYTime = now;
                            break;
                        case KeyEvent.VK_D:
                            if (y < 1.0) {
                                y += 0.05;
                            } // 右移加速
                            lastYTime = now;
                            break;
                        case KeyEvent.VK_Q:
                            if (rotation > -1.0) {
                                rotation -= 0.05;
                            } // 左转加速
                            lastRotationTime = now;
                            break;
                        case KeyEvent.VK_E:
                            if (rotation < 1.0) {
                                rotation += 0.05;
                            } // 右转加速
                            lastRotationTime = now;
                            break;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    // No action needed, deceleration handled in update thread
                }

                @Override
                public void keyTyped(KeyEvent e) {
                    // Not used
                }
            });

            // Start a thread to continuously update the label
            Thread updateThread = new Thread(() -> {
                while (true) {
                    // Decelerate to center if no input for 1 second
                    long now = System.currentTimeMillis();
                    if (now - lastXTime > 500) {
                        if (x > 0) x -= 0.005;
                        else if (x < 0) x += 0.005;
                    }
                    if (now - lastYTime > 500) {
                        if (y > 0) y -= 0.005;
                        else if (y < 0) y += 0.005;
                    }
                    if (now - lastRotationTime > 500) {
                        if (rotation > 0) rotation -= 0.005;
                        else if (rotation < 0) rotation += 0.005;
                    }
                    double[] powers = controller.update(x, y, rotation);
                    SwingUtilities.invokeLater(() -> {
                        label.setText(String.format("<html>FL: %.3f FR: %.3f<br>BL: %.3f BR: %.3f</html>", powers[0], powers[1], powers[2], powers[3]));
                    });
                    try {
                        Thread.sleep(10); // Update every 500ms
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