package ch.compass.gonzoproxy.relay.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.UnexpectedException;
import java.util.ArrayList;

import org.yaml.snakeyaml.Yaml;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.ForwardingType;
import ch.compass.gonzoproxy.mvc.model.SessionFormat;

public class ParsingHandler {

	private static final String TEMPLATE_FOLDER = "templates/";

	private ArrayList<ApduTemplate> templates = new ArrayList<ApduTemplate>();

	private AbstractParser selectedParser;

	public ParsingHandler(SessionFormat sessionFormat,
			ForwardingType forwardingType) {
		loadTemplates();
		setUpParser(forwardingType, sessionFormat);
	}

	public void processApdu(Apdu apdu) {
		selectedParser.setProcessingApdu(apdu);
		if (!parseByTemplate(apdu))
			parseByDefault(apdu);
	}

	private void loadTemplates() {
		File[] templateFiles = locateTemplateFiles();
		for (int i = 0; i < templateFiles.length; i++) {
			try (InputStream fileInput = new FileInputStream(templateFiles[i])) {
				Yaml beanLoader = new Yaml();
				ApduTemplate template = beanLoader.loadAs(fileInput,
						ApduTemplate.class);
				templates.add(template);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private File[] locateTemplateFiles() {
		File folder = new File(TEMPLATE_FOLDER);
		File[] templateFiles = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".apdu");
			}
		});

		return templateFiles;
	}

	private void parseByDefault(Apdu apdu) {
		// TODO: implement
	}

	private boolean parseByTemplate(Apdu apdu) {
		for (ApduTemplate template : templates) {
			if (selectedParser.templateIsAccepted(template)) {
				selectedParser.tryParse(template);
				return true;
			}
		}
		return false;
	}

	// TODO: fix
	private void setUpParser(ForwardingType apduType,
			SessionFormat sessionFormat) {
		switch (apduType) {
		case COMMAND:
			selectedParser = new CommandParser();
			setParserSettings(sessionFormat);
			break;
		case RESPONSE:
			selectedParser = new ResponseParser();
			setParserSettings(sessionFormat);
			break;
		}
	}

	private void setParserSettings(SessionFormat sessionFormat) {
		selectedParser.setEncodingSettings(sessionFormat.getEncodingOffset(),
				sessionFormat.getWhitespaceOffset());
	}

}
