package ch.compass.gonzoproxy.relay.modifier;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.mvc.model.Packet;
import ch.compass.gonzoproxy.utils.PacketUtils;

public class PacketModifier {

	private ArrayList<RuleSet> modifierRules = new ArrayList<RuleSet>();

	public Packet tryModify(Packet originalPacket) {
		for (RuleSet modifier : modifierRules) {
			if (ruleSetMatches(modifier, originalPacket)) {
				return applyRules(modifier, originalPacket);
			}
		}

		return originalPacket;
	}

	private Packet applyRules(RuleSet modifier, Packet originalPacket) {

		Packet modifiedPacket = originalPacket.clone();

		for (Field field : modifiedPacket.getFields()) {
			Rule rule = modifier.findMatchingRule(field);

			if (rule != null && rule.isActive()) {
				int fieldLengthDiff = 0;

				if (rule.getOriginalValue().isEmpty()) {
					if (modifier.shouldUpdateLength()) {
						fieldLengthDiff= computeLengthDifference(field.getValue(),
								rule.getReplacedValue());
						updateContentLengthField(modifiedPacket,
								fieldLengthDiff);
					}
					field.setValue(rule.getReplacedValue());

				} else {
					if (modifier.shouldUpdateLength()) {
						fieldLengthDiff = computeLengthDifference(rule.getOriginalValue(),
								rule.getReplacedValue());
						updateContentLengthField(modifiedPacket,
								fieldLengthDiff);
					}
					field.replaceValue(rule.getOriginalValue(),
							rule.getReplacedValue());
				}
				updatePacketLenght(modifiedPacket, fieldLengthDiff);
				modifiedPacket.isModified(true);
			}
		}
		return modifiedPacket;
	}

	private void updatePacketLenght(Packet modifiedPacket, int fieldLengthDiff) {
		int updatedPacketSize = modifiedPacket.getSize() + fieldLengthDiff;
		modifiedPacket.setSize(updatedPacketSize);
	}

	private void updateContentLengthField(Packet packet, int fieldLengthDiff) {

		Field contentLengthField = findContentLengthField(packet);
		int currentContentLength = Integer.parseInt(
				contentLengthField.getValue(), 16);
		int newContentLength = currentContentLength + fieldLengthDiff;
		contentLengthField.setValue(toHexString(newContentLength));

	}

	private String toHexString(int newContentLength) {
		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toHexString(newContentLength));
		if (sb.length() < 2) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}

	private Field findContentLengthField(Packet packet) {
		for (Field field : packet.getFields()) {
			if (field.getName().equals(PacketUtils.CONTENT_LENGTH_FIELD))
				return field;
		}
		return new Field();
	}

	private int computeLengthDifference(String originalValue,
			String replacedValue) {
		int diff = (replacedValue.length() - originalValue.length())
				/ (PacketUtils.ENCODING_OFFSET + PacketUtils.WHITESPACE_OFFSET);
		return diff;
	}

	private boolean ruleSetMatches(RuleSet existingRuleSet,
			Packet originalPacket) {
		return existingRuleSet.getCorrespondingPacket().equals(
				originalPacket.getDescription());
	}

	public void addRule(String packetName, Rule fieldRule, Boolean updateLength) {
		RuleSet existingRuleSet = findRuleSet(packetName);
		if (existingRuleSet != null) {
			existingRuleSet.add(fieldRule);
			existingRuleSet.shouldUpdateLength(updateLength);
		} else {
			RuleSet createdRuleSet = new RuleSet(packetName);
			createdRuleSet.add(fieldRule);
			modifierRules.add(createdRuleSet);
			createdRuleSet.shouldUpdateLength(updateLength);
		}
	}

	private RuleSet findRuleSet(String packetName) {
		for (RuleSet existingModifier : modifierRules) {
			if (existingModifier.getCorrespondingPacket().equals(packetName))
				return existingModifier;
		}
		return null;
	}

	public ArrayList<RuleSet> getRuleSets() {
		return modifierRules;
	}

}
