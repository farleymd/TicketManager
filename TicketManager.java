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

    final String high = "1: High";
    final String medium = "2: Medium";
    final String low = "3: Low";


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

        //TODO READ EXISTING TICKETS FROM FILE

        addTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO PRIORITIZE TICKETS BY PRIORITY

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

        clearFieldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                problemText.setText("");
                reportedByText.setText("");
            }
        });

        deleteTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ticket toDelete = TicketManager.this.ticketList.getSelectedValue();
                TicketManager.this.ticketListModel.removeElement(toDelete);
            }
        });

        resolveTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ticket resolvedTicket = TicketManager.this.ticketList.getSelectedValue();
                String resolution = JOptionPane.showInputDialog("How was this issue resolved?");
                Date resolvedDate = new Date();

                resolvedTicket.setResolution(resolution);
                resolvedTicket.setResolutionDate(resolvedDate);

                TicketManager.this.ticketListModel.removeElement(resolvedTicket);

            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

            //TODO WRITE TICKETS TO FILE
        });

    }


}
