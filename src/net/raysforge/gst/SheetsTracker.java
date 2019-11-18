package net.raysforge.gst;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.raysforge.easyswing.EasySwing;
import net.raysforge.easyswing.EasyTable;

public class SheetsTracker implements ActionListener {

	SampleCache sampleCache = new SampleCache("C:\\Coding\\Projects\\Private\\Audio\\Samples");
	SheetsCache sheetsCache = new SheetsCache();
	
	PatternPlayer patternPlayer;
	private EasyTable easyTable;

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ClearSheetsCache")) {
			sheetsCache.clear();
		}
		if(e.getActionCommand().equals("play")) {
			String spreadsheetId = easyTable.getValue(0, 1);
			String range = easyTable.getValue(1, 1);
			(patternPlayer = new PatternPlayer(spreadsheetId, range, sheetsCache, sampleCache)).start();
		}
		if(e.getActionCommand().equals("stop")) {
			if(patternPlayer != null)
				patternPlayer.stop_();
		}
	}


	public SheetsTracker() throws Exception {
		EasySwing es = new EasySwing("SheetsTracker 1.0", 800, 600);
		es.addToolBarItem("ClearSheetsCache", "ClearSheetsCache", this);
		es.addToolBarItem("play", "play", this);
		es.addToolBarItem("stop", "stop", this);
		
		easyTable = es.setTableAsMainContent();
		easyTable.addColumn("Name");
		easyTable.addColumn("Value");
		easyTable.setValue("SheetID", 0, 0);
		easyTable.setValue("1XHa-cAyu5Bvf2CcqMO8i5b122P3ChKQNscFyZuhUt2M", 0, 1);
		easyTable.setValue("Range", 1, 0);
		easyTable.setValue("Pattern1!A:E", 1, 1);
		
		es.show();
	}


	public static void main(String... args) throws Exception {
		new SheetsTracker();
	}

}
