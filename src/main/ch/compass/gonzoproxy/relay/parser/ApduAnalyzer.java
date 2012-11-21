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

public class ApduAnalyzer {

	private static final String TEMPLATE_FOLDER = "templates/";

	private ArrayList<ApduTemplate> templates = new ArrayList<ApduTemplate>();

	private Parser selectedParser;

	public ApduAnalyzer(SessionFormat sessionFormat,
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
				System.out.println("template " + template.getApduDescription()
						+ " added");

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
				return selectedParser.tryParse(template);
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
		// TODO: response parser not implemented yet, exception
		case RESPONSE:
			selectedParser = new CommandParser();
			setParserSettings(sessionFormat);
			break;

		default:
			try {
				throw new UnexpectedException("No Parser found");
			} catch (UnexpectedException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void setParserSettings(SessionFormat sessionFormat) {
		selectedParser.setEncodingSettings(sessionFormat.getEncodingOffset(),
				sessionFormat.getWhitespaceOffset());
	}

}
