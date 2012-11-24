package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.mvc.model.Packet;
import ch.compass.gonzoproxy.utils.ParsingHelper;

public class ParsingUnit {

	public boolean parseBy(PacketTemplate template, Packet processingPacket) {
		processingPacket.setDescription(template.getPacketDescription());
		ArrayList<Field> templateFields = template.getFields();
		byte[] packet = processingPacket.getPlainPacket();

		int contentStartIndex = 0;
		int contentLength = ParsingHelper.DEFAULT_FIELDLENGTH;

		int fieldLength = ParsingHelper.DEFAULT_FIELDLENGTH;
		int offset = 0;

		for (int i = 0; i < templateFields.size(); i++) {
			Field processingField = getCopyOf(templateFields.get(i));
			parseField(packet, fieldLength, offset, processingField);
			processingPacket.addField(processingField);

			int currentFieldOffset = offset;
			offset += ParsingHelper.getEncodedFieldLength(fieldLength, true);

			if (ParsingHelper.isContentLengthField(processingField)) {
				if (ParsingHelper.isContentIdentifierField(templateFields
						.get(i + 1))) {
					contentLength = Integer.parseInt(
							processingField.getValue(), 16);
					contentStartIndex = offset;
				} else {
					fieldLength = Integer.parseInt(processingField.getValue(),
							16);
				}

			} else if (ParsingHelper.isIdentifiedContent(templateFields, i,
					processingField)) {
				int nextContentIdentifierField = ParsingHelper
						.findNextContentIdentifierField(i + 1, templateFields);

				switch (nextContentIdentifierField) {
				case 0:
					fieldLength = ParsingHelper.getRemainingContentSize(
							contentStartIndex, contentLength, offset);
					break;
				case 1:
					fieldLength = ParsingHelper.DEFAULT_FIELDLENGTH;
					break;
				default:
					int nextIdentifierIndex = ParsingHelper.findFieldInPacket(
							packet, currentFieldOffset,
							templateFields.get(i + nextContentIdentifierField));
					fieldLength = ParsingHelper.calculateSubContentLength(
							offset, nextIdentifierIndex);
					break;
				}

				//
				// int nextIdentifier = 0;
				// if (templateFields.size() > i
				// + ParsingHelper.NEXT_IDENTIFIER_OFFSET) {
				// nextIdentifier = ParsingHelper.findFieldInPacket(
				// packet,
				// currentFieldOffset,
				// templateFields.get(i
				// + ParsingHelper.NEXT_IDENTIFIER_OFFSET));
				// }
				//
				// if (nextIdentifier > 0) {
				// fieldLength = ParsingHelper.calculateSubContentLength(
				// offset, nextIdentifier);
				// } else {
				// fieldLength = ParsingHelper.getRemainingContentSize(
				// contentStartIndex, contentLength, offset);
				// }

			} else {
				fieldLength = ParsingHelper.DEFAULT_FIELDLENGTH;
			}
		}
		return true;
	}

	private void parseField(byte[] payload, int fieldLength, int offset,
			Field processingField) {
		if (ParsingHelper.hasCustomLenght(fieldLength)) {
			parseValueToField(payload, offset,
					ParsingHelper.getEncodedFieldLength(fieldLength, false),
					processingField);
		} else {
			int encodedFieldLength = ParsingHelper.getEncodedFieldLength(
					fieldLength, false);
			parseValueToField(payload, offset, encodedFieldLength,
					processingField);
		}
	}

	private void parseValueToField(byte[] payload, int offset, int fieldLength,
			Field field) {
		if ((offset + fieldLength) <= payload.length) {
			byte[] value = ParsingHelper.extractFieldFromBuffer(payload,
					fieldLength, offset);
			setFieldValue(field, value);
		}
	}

	private void setFieldValue(Field field, byte[] value) {
		field.setValue(new String(value));
	}

	private Field getCopyOf(Field field) {
		return new Field(field.getName(), field.getValue(),
				field.getDescription());
	}
}