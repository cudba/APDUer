package ch.compass.gonzoproxy.relay.parser;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public class ParsingHandler {

	private static final String TEMPLATE_FOLDER = "/home/dinkydau/GonzoProxy/templates/";

	private File[] files;
	private ArrayList<ApduTemplate> templates = new ArrayList<ApduTemplate>();

	private AsciiApduParser asciiParser = new AsciiApduParser();
	private Parser selectedParser;

	public ParsingHandler() {
		locateTemplateFiles();
		loadTemplates();
	}

	private void locateTemplateFiles() {
		File folder = new File(TEMPLATE_FOLDER);
		files = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".template");
			}
		});
	}

	private void loadTemplates() {
		for (int i = 0; i < files.length; i++) {
			ApduTemplate template = new ApduTemplate(TEMPLATE_FOLDER
					+ files[i].getName());
			templates.add(template);
		}
	}

	public void processApdu(Apdu apdu) {
		findMatchingParser(apdu);
		if (!parseByTemplate(apdu))
			parseByDefault(apdu);
	}

	private void parseByDefault(Apdu apdu) {
		// TODO Auto-generated method stub

	}

	private boolean parseByTemplate(Apdu apdu) {
		for (ApduTemplate template : templates) {
			if (template.accept(selectedParser)) {
				return true;
			}
		}
		return false;
	}

	// TODO: fix
	private void findMatchingParser(Apdu apdu) {
		asciiParser.setProcessingApdu(apdu);
		selectedParser = asciiParser;
	}
}
