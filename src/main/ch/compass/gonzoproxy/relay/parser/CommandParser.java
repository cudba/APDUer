//package ch.compass.gonzoproxy.relay.parser;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import ch.compass.gonzoproxy.mvc.model.Field;
//import ch.compass.gonzoproxy.utils.ByteArrays;
//
//public class CommandParser extends AbstractParser {
//
//	public boolean templateIsAccepted(ApduTemplate template) {
//		byte[] plainApdu = processingApdu.getPlainApdu();
//		ArrayList<Field> templateFields = template.getFields();
//
//		int fieldLength = DEFAULT_FIELDLENGTH;
//		int offset = 0;
//		
//		for (int i = 0; i < templateFields.size(); i++) {
//			if (!apduContainsMoreFields(plainApdu, fieldLength, offset)) {
//				return false;
//			}
//
//			Field processingField = template.getFields().get(i);
//			if (isIdentifierField(processingField)) {
//				if(!fieldIsVerified(plainApdu, fieldLength, offset, processingField)) {
//					return false;
//				}
//			}
//
//			int currentOffset = offset;
//			offset += encodedSingleFieldLength(fieldLength);
//			
//			if (isContentLengthField(processingField)) {
//				byte[] length = extractFieldFromBuffer(plainApdu, fieldLength * encodingOffset, currentOffset);
//				fieldLength = hexToInt(length);
//			} else {
//				fieldLength = DEFAULT_FIELDLENGTH;
//			}
//		}
//		return offset - whitespaceOffset  == plainApdu.length;
//	}
//
//	@Override
//	public boolean tryParse(ApduTemplate template) {
//		setApduDescription(template);
//		List<Field> templateFields = template.getFields();
//		byte[] plainApdu = processingApdu.getPlainApdu();
//
//		int fieldLength = DEFAULT_FIELDLENGTH;
//		int offset = 0;
//		
//		for (int i = 0; i < templateFields.size(); i++) {
//			if (!apduContainsMoreFields(plainApdu, fieldLength, offset)) {
//				return false;
//			}
//			Field processingField = getCopyOf(templateFields.get(i));
//
//			parseField(plainApdu, fieldLength, offset, processingField);
//
//			offset += encodedSingleFieldLength(fieldLength);
//
//			if (isContentLengthField(processingField)) {
//				fieldLength = Integer.parseInt(processingField.getValue(), 16);
//			} else {
//				fieldLength = DEFAULT_FIELDLENGTH;
//			}
//		}
//		return true;
//	}
//
//}
