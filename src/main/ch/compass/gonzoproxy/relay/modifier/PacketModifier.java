package ch.compass.gonzoproxy.relay.modifier;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.mvc.model.Packet;

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
			if (rule != null) {
				if (rule.getOriginalValue().isEmpty()) {
					field.setValue(rule.getReplacedValue());
				} else {
					field.replaceValue(rule.getOriginalValue(),
							rule.getReplacedValue());
				}
			}
		}
		modifiedPacket.isModified(true);
		return modifiedPacket;
	}

	private boolean ruleSetMatches(RuleSet existingRuleSet,
			Packet originalPacket) {
		return existingRuleSet.getCorrespondingPacket().equals(
				originalPacket.getDescription());
	}

	public void addRule(String packetName, Rule fieldRule) {
		RuleSet existingRuleSet = findRuleSet(packetName);
		if (existingRuleSet != null) {
			existingRuleSet.add(fieldRule);
		} else {
			RuleSet createdRuleSet = new RuleSet(packetName);
			createdRuleSet.add(fieldRule);
			modifierRules.add(createdRuleSet);
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
