import frontend.views.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    // Ejecutar la interfaz gráfica de forma segura
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainFrame ventana = new MainFrame();
        ventana.setVisible(true); // Hace que la pantalla aparezca
      }
    });
  }
}