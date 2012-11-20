package ch.compass.gonzoproxy.relay.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import ch.compass.gonzoproxy.mvc.model.Field;


public class YamlParsingTest extends TestCase {
	@
	Test
	public void testLoadAtsTemplate() {
		try (InputStream input = new FileInputStream("templates/ats.apdu")) {
			Yaml beanLoader = new Yaml();
			ApduTemplate parsed = beanLoader.loadAs(input, ApduTemplate.class);
			
			assertNotNull(parsed);
			
			int expectedAtsFields = 9;
			ArrayList<Field> fields = parsed.getFields();
			assertEquals(expectedAtsFields, fields.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
