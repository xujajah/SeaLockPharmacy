package printing;

import database.DBUtil;
import javafx.collections.ObservableSet;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.PosPrinter;

public class POS {

	public static void pageSetup(Node node,Stage owner) 
	{
		PosPrinter posPrinter = (PosPrinter) DBUtil.getObject(PosPrinter.class, 1);
		Printer printer = Printer.getDefaultPrinter();		 
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job == null) 
		{
			return;
		}
		if(!printer.getName().equals(posPrinter.getName())) {
			boolean proceed = job.showPrintDialog(owner);

			if (proceed) 
			{
				PageLayout pageLayout = printer.createPageLayout(printer.getPrinterAttributes().getDefaultPaper(), PageOrientation.PORTRAIT, 0,0,0,0);
				job.getJobSettings().setPageLayout(pageLayout);
				print(job, node);
			}

		}
		else if(printer.getName().equals(posPrinter.getName())) {
			PageLayout pageLayout = printer.createPageLayout(printer.getPrinterAttributes().getDefaultPaper(), PageOrientation.PORTRAIT, 0,0,0,0);
			job.getJobSettings().setPageLayout(pageLayout);
			print(job, node);
		}
	}

	public static void pageSetup(Node node) 
	{
		PosPrinter posPrinter = (PosPrinter) DBUtil.getObject(PosPrinter.class, 1);
		Printer printer = Printer.getDefaultPrinter();
		ObservableSet<Printer> printers = Printer.getAllPrinters();
		for(Printer p : printers){
			if(p.getName().matches(posPrinter.getName())){
				printer = p;
			}
		}		 
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job == null) 
		{
			return;
		}
		PageLayout pageLayout = printer.createPageLayout(printer.getPrinterAttributes().getDefaultPaper(), PageOrientation.PORTRAIT, 0,0,0,0);
		job.getJobSettings().setPageLayout(pageLayout);
		job.setPrinter(printer);
		print(job, node);
	}

	private static void print(PrinterJob job, Node node) 
	{

		// Print the node
		boolean printed = job.printPage(node);

		if (printed) 
		{
			job.endJob();
		}
	}	

}
