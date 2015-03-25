//package Marty.company;

import java.util.Date;

/**
 * Created by marty.farley on 3/1/2015.
 */
public class Ticket {

    private int priority;
    private String reporter;
    private String description;
    private Date dateReported;

    private Date resolutionDate;
    private String resolution;

    private static int staticTicketIDCounter = 1;
    protected int ticketID;

    public Date getDateReported() {
        return dateReported;
    }

    public String getReporter() {
        return reporter;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public Ticket(int p, String rep, String desc, Date date) {
        this.priority = p;
        this.reporter = rep;
        this.description = desc;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

    protected int getPriority(){
        return priority;
    }

        public String toString(){
            return("ID = " + this.ticketID + "\n" +
                    " Priority = " + this.priority + "\n" +
                    " Issued = " + this.description + "\n" +
                    " Reported By = " + this.reporter + "\n" +
                    " Reported on = " + this.dateReported + "\n");
    }
    }
