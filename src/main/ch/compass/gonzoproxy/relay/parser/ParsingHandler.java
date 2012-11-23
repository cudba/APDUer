package ch.compass.gonzoproxy.relay.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.yaml.snakeyaml.Yaml;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.SessionFormat;
import ch.compass.gonzoproxy.utils.ParsingHelper;

public class ParsingHandler {

	private static final String TEMPLATE_FOLDER = "templates/";

	private ArrayList<ApduTemplate> templates = new ArrayList<ApduTemplate>();

	private ParsingUnit parsingUnit;
	private TemplateValidator templateValidator;

	public ParsingHandler(SessionFormat sessionFormat) {
		loadTemplates();
		prepareParsingUnits(sessionFormat);
	}

	public void tryParse(Apdu apdu) {
		parsingUnit.setProcessingApdu(apdu);
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

	private boolean parseByTemplate(Apdu processingApdu) {

		for (ApduTemplate template : templates) {
			if (templateValidator.accept(template, processingApdu)) {
				parsingUnit.parseBy(template, processingApdu);
				return true;
			}
		}
		return false;
	}

	private void prepareParsingUnits(SessionFormat sessionFormat) {
		parsingUnit = new ParsingUnit();
		templateValidator = new TemplateValidator();
		ParsingHelper.encodingOffset = sessionFormat.getEncodingOffset();
		ParsingHelper.whitespaceOffset = sessionFormat.getWhitespaceOffset();
		
	}
}
