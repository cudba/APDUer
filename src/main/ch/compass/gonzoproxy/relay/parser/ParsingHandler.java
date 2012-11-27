package ch.compass.gonzoproxy.relay.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.yaml.snakeyaml.Yaml;

import ch.compass.gonzoproxy.mvc.model.Packet;
import ch.compass.gonzoproxy.mvc.model.ParserSettings;
import ch.compass.gonzoproxy.utils.ParsingHelper;

public class ParsingHandler {

	private static final String TEMPLATE_FOLDER = "templates/";

	private static final String UNPARSABLE_PACKET = "Unparsable Packet";

	private ArrayList<PacketTemplate> templates = new ArrayList<PacketTemplate>();

	private ParsingUnit parsingUnit;
	private TemplateValidator templateValidator;

	public ParsingHandler(ParserSettings sessionFormat) {
		loadTemplates();
		prepareParsingUnits(sessionFormat);
	}

	public void tryParse(Packet processingPacket) {
		if (!parseByTemplate(processingPacket))
			parseByDefault(processingPacket);
	}

	private void loadTemplates() {
		File[] templateFiles = locateTemplateFiles();
		for (int i = 0; i < templateFiles.length; i++) {
			try (InputStream fileInput = new FileInputStream(templateFiles[i])) {
				Yaml beanLoader = new Yaml();
				PacketTemplate template = beanLoader.loadAs(fileInput,
						PacketTemplate.class);
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

	private void parseByDefault(Packet processingPacket) {
		processingPacket.setDescription(UNPARSABLE_PACKET);
	}

	private boolean parseByTemplate(Packet processingPacket) {

		for (PacketTemplate template : templates) {
			if (templateValidator.accept(template, processingPacket)) {
				parsingUnit.parseBy(template, processingPacket);
				return true;
			}
		}
		return false;
	}

	private void prepareParsingUnits(ParserSettings sessionFormat) {
		parsingUnit = new ParsingUnit();
		templateValidator = new TemplateValidator();
		ParsingHelper.encodingOffset = sessionFormat.getEncodingOffset();
		ParsingHelper.whitespaceOffset = sessionFormat.getWhitespaceOffset();
		
	}
}
