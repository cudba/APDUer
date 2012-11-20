package ch.compass.gonzoproxy.relay.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

import ch.compass.gonzoproxy.mvc.model.Field;


public class YamlParsingTest extends TestCase {
	private static final String TEMPLATE_FOLDER = "templates/";
	
	public static void main(String[] args) throws FileNotFoundException {
//		Yaml yaml = new Yaml();
//		String document = "\n- description\n- Papilionidae\n- Apatelodidae\n- Epiplemidae";
//		List<String> list = (List<String>) yaml.load("/home/dinkydau/GonzoProxy/templates/yamlTest.template");
//		System.out.println(list);
		
		testLoadList();
	}
	
	 public static void testDumpList() {
	        ApduTemplate bean = new ApduTemplate();
	        ArrayList<Field> fields = new ArrayList<Field>();
	        Field field = new Field();
	        field.setName("FooName");
	        field.setDescription("FooDescription");
	        field.setValue("0A");
	        fields.add(field);
	        field.setName("FooName2");
	        field.setDescription("FooDescription2");
	        field.setValue("0B");
	        fields.add(field);
	        Yaml yaml = new Yaml();
	        String output = yaml.dumpAsMap(bean);
	        // System.out.println(output);
	 }
	 
	 public static void testLoadList()  {
		 InputStream input;
		try {
			input = new FileInputStream("templates/ats.apdu");
			Yaml beanLoader = new Yaml();
			ApduTemplate parsed = beanLoader.loadAs(input, ApduTemplate.class);
			assertNotNull(parsed);
			ArrayList<Field> fields = parsed.getFields();
			System.out.println("apdu name " + parsed.getApduDescription());
			for (Field field : fields) {
				System.out.println(field.getDescription());
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    }
	

}
