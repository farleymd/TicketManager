import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jz2438mu on 3/24/2015.
 */
public class TicketManager extends JFrame {
    private JPanel rootPanel;
    private JTextField problemText;
    private JTextField reportedByText;
    private JComboBox priorityCombo;
    private JButton addTicketButton;
    private JButton clearFieldsButton;
    private JList <Ticket> ticketList;
    private JButton deleteTicketButton;
    private JButton resolveTicketButton;
    private JButton quitButton;

    DefaultListModel<Ticket> ticketListModel;

    final String high = "1";
    final String medium = "2";
    final String low = "3";


    public TicketManager(){
        super("Ticket Manager");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(300,500));

        ticketListModel = new DefaultListModel<Ticket>();
        ticketList.setModel(ticketListModel);
        ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        priorityCombo.addItem(high);
        priorityCombo.addItem(medium);
        priorityCombo.addItem(low);

        addTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = problemText.getText();
                String reporter = reportedByText.getText();
                int priority = 0;

                if (priorityCombo.getSelectedItem().equals(high)){
                    priority = 1;

                } else if (priorityCombo.getSelectedItem().equals(medium)){
                    priority = 2;
                } else {
                    priority = 3;
                }

                Date dateReported = new Date();

                Ticket t = new Ticket(priority, reporter, description, dateReported);

                TicketManager.this.ticketListModel.addElement(t);
            }
        });

    }

}
