package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Packet;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ParsingHelper;

public class TemplateValidator {

	public boolean accept(PacketTemplate template, Packet processingPacket) {
		byte[] packet = processingPacket.getPlainPacket();
		ArrayList<Field> templateFields = template.getFields();

		int contentStartIndex = 0;
		int contentLength = ParsingHelper.DEFAULT_FIELDLENGTH;

		int fieldLength = ParsingHelper.DEFAULT_FIELDLENGTH;
		int offset = 0;

		for (int i = 0; i < templateFields.size(); i++) {
			if (!packetContainsMoreFields(packet, fieldLength, offset)) {
				return false;
			}

			Field processingField = templateFields.get(i);
			if (isIdentifierField(processingField)) {
				if (!fieldIsVerified(packet, fieldLength, offset,
						processingField)) {
					return false;
				}
			}

			int currentFieldOffset = offset;
			offset += ParsingHelper.getEncodedFieldLength(fieldLength, true);
			if (ParsingHelper.isContentLengthField(processingField)) {
				int encodedFieldLength = ParsingHelper.getEncodedFieldLength(
						fieldLength, false);
				byte[] length = ParsingHelper.extractFieldFromBuffer(packet,
						encodedFieldLength, currentFieldOffset);
				if (ParsingHelper.isContentIdentifierField(templateFields
						.get(i + 1))) {
					contentLength = hexToInt(length);
					contentStartIndex = offset;
				} else {
					fieldLength = hexToInt(length);
				}
			}

			else if (ParsingHelper.isIdentifiedContent(templateFields, i, processingField)) {
				int nextIdentifierIndex = 0;

					int nextContentIdentifierField = ParsingHelper
							.findNextContentIdentifierField(i + 1, templateFields);

					if(nextContentIdentifierField > 0) {
						nextIdentifierIndex = ParsingHelper.findFieldInPacket(
								packet,
								currentFieldOffset,
								templateFields.get(i
										+ ParsingHelper.NEXT_IDENTIFIER_OFFSET));
					}
					
					switch (nextContentIdentifierField) {
					case 0:
						fieldLength = ParsingHelper.getRemainingContentSize(
								contentStartIndex, contentLength, offset);
						break;
					case 1:
						fieldLength = ParsingHelper.DEFAULT_FIELDLENGTH;
					default:
						fieldLength = ParsingHelper.calculateSubContentLength(
								offset, nextIdentifierIndex);
						break;
					}
			} else {
				fieldLength = ParsingHelper.DEFAULT_FIELDLENGTH;
			}
		}
		return offset - ParsingHelper.whitespaceOffset == packet.length;
	}

	private boolean packetContainsMoreFields(byte[] packet, int fieldLength,
			int offset) {
		return (offset + fieldLength * ParsingHelper.encodingOffset) <= packet.length;
	}

	private boolean isIdentifierField(Field processingField) {
		return processingField.getValue() != null;
	}

	private boolean fieldIsVerified(byte[] packet, int fieldLength, int offset,
			Field processingField) {
		byte[] idByte;
		int encodedFieldLength = ParsingHelper.getEncodedFieldLength(
				fieldLength, false);
		if (fieldLength > ParsingHelper.DEFAULT_FIELDLENGTH) {
			idByte = ParsingHelper.extractFieldFromBuffer(packet,
					encodedFieldLength, offset);
		} else {
			idByte = ParsingHelper.extractFieldFromBuffer(packet,
					encodedFieldLength, offset);
		}
		if (!valueMatches(idByte, processingField)) {
			return false;
		}
		return true;
	}

	private boolean valueMatches(byte[] idByte, Field field) {
		String packetValue = new String(idByte);
		String templateValue = field.getValue();
		return packetValue.equalsIgnoreCase(templateValue);
	}

	private int hexToInt(byte[] hexValue) {
		return Integer.parseInt(new String(hexValue), 16);
	}

}
