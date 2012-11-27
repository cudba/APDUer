package ch.compass.gonzoproxy.relay.modifier;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.mvc.model.Packet;

public class PacketModifierTest {

	@Test
	public void testAddNewRule() {

		PacketModifier packetModifier = new PacketModifier();


		Rule rule = new Rule("Modified Field", "0f", "a5");
		packetModifier.addRule("Modified Packet", rule);

		assertEquals(rule, packetModifier.getRuleSets().get(0).getRules()
				.get(0));
	}

	@Test
	public void testOverrideExistingRule() {
		PacketModifier packetModifier = new PacketModifier();


		Rule rule = new Rule("Modified Field", "0f", "a5");
		packetModifier.addRule("Modified Packet", rule);
		
		Rule overridingRule = new Rule("Modified Field", "0f", "ff");
		
		packetModifier.addRule("Modified Packet", overridingRule);
		
		assertEquals(1, packetModifier.getRuleSets().size());
		assertEquals(overridingRule, packetModifier.getRuleSets().get(0).getRules().get(0));
	}

	@Test
	public void testGetModifiedPacket() {

		PacketModifier packetModifier = new PacketModifier();

		Rule rule = new Rule("Modified Field", "0f", "a5");

		packetModifier.addRule("Modified Packet", rule);

		String fakePlainApdu = "00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		String libnfcInput = "C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		Packet receivedPacket = new Packet(libnfcInput.getBytes());
		receivedPacket.setPlainPacket(fakePlainApdu.getBytes());
		receivedPacket.setDescription("Modified Packet");

		receivedPacket.addField(new Field("FooName", "00", "FooDescription"));
		receivedPacket.addField(new Field("Modified Field", "0f",
				"FooDescriptoin"));

		Packet modifiedPacket = packetModifier.tryModify(receivedPacket);

		assertEquals(rule.getReplacedValue(), modifiedPacket.getFields().get(1)
				.getValue());
		assertEquals("0f", receivedPacket.getFields().get(1).getValue());

	}

}
