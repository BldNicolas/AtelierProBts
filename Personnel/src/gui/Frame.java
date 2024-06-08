package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;

import personnel.GestionPersonnel;

public class Frame extends JFrame{
    protected GestionPersonnel gestionPersonnel;

    public Frame(GestionPersonnel gestionPersonnel)
    {
        this.gestionPersonnel = gestionPersonnel;
        setTitle("VDNGestion");
        Style.makeFrameFullSize(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    static boolean verifiePassword(String password, GestionPersonnel gestionPersonnel)
	{
		boolean isCorrect = gestionPersonnel.getRoot().checkPassword(password);
		return isCorrect;
	}

    /**
     * Close a frame, show an other
     * @param frameToClose frame to close
     * @param frameToShow frame to show
     */
    public static void swap(Frame frameToClose, Frame frameToShow) {
        frameToShow.setVisible(true);
        frameToClose.dispose();
    }

    /**
     * Show a frame
     * @param frame frame to show
     */
    public static void show(Frame frame)
    {
        frame.setVisible(true);
    }

    /**
     * Convert a String to a LocalDate
     * @param dateString the date should be "dd-MM-yyyy"
     * @return
     */
    protected LocalDate strToLocalDate(String dateString)
    {
	    LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        date = LocalDate.parse(dateString, formatter);
	    return date;
	}
}
