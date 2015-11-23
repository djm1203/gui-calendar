import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 Created by scot on 11/21/15.
 */
public class ActionView extends JPanel
{
   CalendarModel calendarModel;
   JButton create;
   JButton delete;
   JButton exit;

   public ActionView(CalendarModel c)
   {
      calendarModel = c;

      setLayout(null);
      setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

      final int PANEL_WIDTH = 698;
      final int PANEL_HEIGHT = 44;
      final int BUTTON_WIDTH = 100;
      final int BUTTON_HEIGHT = 25;
      final String CREATE_EVENT = "Create";
      final String DELETE_EVENTS = "Delete";
      final String EXIT_APPLICATION = "Exit";

      create = new JButton(CREATE_EVENT);
      create.setFocusPainted(false);
      delete = new JButton(DELETE_EVENTS);
      delete.setFocusPainted(false);
      exit = new JButton(EXIT_APPLICATION);
      exit.setFocusPainted(false);

      // Set bounds
      setBounds(0, 340, PANEL_WIDTH, PANEL_HEIGHT);
      create.setBounds((PANEL_WIDTH-BUTTON_WIDTH)/2-120, (PANEL_HEIGHT-BUTTON_HEIGHT)/2, BUTTON_WIDTH, BUTTON_HEIGHT);
      delete.setBounds((PANEL_WIDTH-BUTTON_WIDTH)/2, (PANEL_HEIGHT-BUTTON_HEIGHT)/2, BUTTON_WIDTH, BUTTON_HEIGHT);
      exit.setBounds((PANEL_WIDTH-BUTTON_WIDTH)/2+120, (PANEL_HEIGHT-BUTTON_HEIGHT)/2, BUTTON_WIDTH, BUTTON_HEIGHT);

      add(create);
      add(delete);
      add(exit);
   }

   /**
    Event listener to view the next month
    @param listenForButton a button listener
    */
   public void createButtonListener(ActionListener listenForButton)
   {
      create.addActionListener(listenForButton);
   }

   public void deleteButtonListener(ActionListener listenForButton)
   {
      delete.addActionListener(listenForButton);
   }

   /**
    Event listener to view the next month
    @param listenForButton a button listener
    */
   public void exitButtonListener(ActionListener listenForButton)
   {
      exit.addActionListener(listenForButton);
   }
}
