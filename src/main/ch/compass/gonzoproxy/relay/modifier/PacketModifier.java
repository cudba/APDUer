package ch.compass.gonzoproxy.relay.modifier;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.mvc.model.Packet;

public class PacketModifier {

	private ArrayList<RuleSet> modifierRules = new ArrayList<RuleSet>();

	public Packet tryModify(Packet originalPacket) {
		for (RuleSet modifier : modifierRules) {
			if (modifierMatches(modifier, originalPacket)) {
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
				field.setValue(rule.getReplacedValue());
			}
		}
		modifiedPacket.isModified(true);
		return modifiedPacket;
	}

	private boolean modifierMatches(RuleSet modifier, Packet originalPacket) {
		return modifier.getCorrespondingPacket().equals(
				originalPacket.getDescription());
	}

	public void add(RuleSet createdModifier) {
		RuleSet existingModifier = findModifier(createdModifier);
		if (existingModifier != null) {
			addRulesToExistingModifier(existingModifier, createdModifier);
		} else {
			modifierRules.add(createdModifier);
		}
	}

	private void addRulesToExistingModifier(RuleSet existingModifier,
			RuleSet createdModifier) {
		for (Rule modifierRule : createdModifier.getRules()) {
			existingModifier.add(modifierRule);
		}
	}

	private RuleSet findModifier(RuleSet modifier) {
		for (RuleSet existingModifier : modifierRules) {
			if (existingModifier.equals(modifier))
				return existingModifier;
		}
		return null;
	}

	public ArrayList<RuleSet> getRuleSets() {
		return modifierRules;
	}

}
