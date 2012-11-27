package ch.compass.gonzoproxy.relay.modifier;

public class Rule {

	private String correspondingField;
	private String originalValue;
	private String replacedValue;

	public Rule(String correspondingField, String originalValue,
			String replacedValue) {
		this.correspondingField = correspondingField;
		this.originalValue = originalValue;
		this.replacedValue = replacedValue;
	}

	public String getCorrespondingField() {
		return correspondingField;
	}

	public void setCorrespondingField(String correspondingField) {
		this.correspondingField = correspondingField;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public String getReplacedValue() {
		return replacedValue;
	}

	public void setReplacedValue(String replacedValue) {
		this.replacedValue = replacedValue;
	}

	@Override
	public boolean equals(Object object) {
		Rule rule = (Rule) object;
		return correspondingField.equals(rule.getCorrespondingField())
				&& originalValue.equals(rule.getOriginalValue());
	}

}
