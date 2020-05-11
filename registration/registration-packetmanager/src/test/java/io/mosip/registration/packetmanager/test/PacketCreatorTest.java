package io.mosip.registration.packetmanager.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import io.mosip.kernel.core.cbeffutil.jaxbclasses.SingleType;
import io.mosip.kernel.core.exception.IOException;
import io.mosip.registration.packetmanager.spi.PacketCreator;
import io.mosip.registration.packetmananger.constants.ErrorCode;
import io.mosip.registration.packetmananger.dto.AuditDto;
import io.mosip.registration.packetmananger.dto.BiometricsDto;
import io.mosip.registration.packetmananger.dto.DocumentDto;
import io.mosip.registration.packetmananger.dto.SimpleDto;
import io.mosip.registration.packetmananger.dto.metadata.ModalityException;
import io.mosip.registration.packetmananger.exception.PacketCreatorException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PacketCreatorTest {
	
	@Autowired
	private PacketCreator packetCreatorImpl;
	
	
	private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvgvz5D4KEK_kHjrexIUFRzw2K29yIUhWKRbRJAGNE0MGMwseVffnp-Zh0ynMeHiBgA9RzyLuZnZZrPYkmyVzsJaeS3g9TnjzkIphc4DkIFVJqGV2S0LJye6EA_kOY6CY6LbtIo-8Z6ZU1kwG8Med-DA3ig04uJXJ_kzNcqvTT2VKrDJwmp6wA5Vk9L6G3Jvau0cgEOnXl3k2Vdzr_J9r8fbXhgHpiQUYk_bFSq9noMnZWKPV2odYvfQA9WaxHAYVQ6rNVPku58eGOYWx2gyjVNhRSc9dowOqPBylh_Y2PBtSO1bKija2uBJW81Rc2y2rOjFP7MroaUrGkocADiFg_wIDAQAB";
	private String schemaJson = "{\"$schema\":\"http:\\/\\/json-schema.org\\/draft-07\\/schema#\",\"description\":\"test mosip id schema\",\"additionalProperties\":false,\"title\":\"mosip id schema\",\"type\":\"object\",\"definitions\":{\"simpleType\":{\"uniqueItems\":true,\"additionalItems\":false,\"type\":\"array\",\"items\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"language\",\"value\"],\"properties\":{\"language\":{\"type\":\"string\"},\"value\":{\"type\":\"string\"}}}},\"documentType\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"format\":{\"type\":\"string\"},\"type\":{\"type\":\"string\"},\"value\":{\"type\":\"string\"}}},\"biometricsType\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"format\":{\"type\":\"string\"},\"version\":{\"type\":\"number\",\"minimum\":0},\"value\":{\"type\":\"string\"}}}},\"properties\":{\"identity\":{\"additionalProperties\":false,\"type\":\"object\",\"required\":[\"IDSchemaVersion\",\"UIN\",\"fullName\",\"dateOfBirth\",\"gender\",\"addressLine1\",\"addressLine2\",\"addressLine3\",\"region\",\"province\",\"city\",\"postalCode\",\"phone\",\"email\",\"zone\",\"proofOfIdentity\",\"proofOfRelationship\",\"proofOfDateOfBirth\",\"proofOfException\",\"proofOfException\",\"individualBiometrics\"],\"properties\":{\"proofOfAddress\":{\"fieldCategory\":\"optional\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"gender\":{\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"city\":{\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"postalCode\":{\"validators\":[{\"validator\":\"^[(?i)A-Z0-9]{5}$|^NA$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"individualBiometrics\":{\"bioAttributes\":[\"leftEye\", \"face\"],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/biometricsType\"},\"province\":{\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"zone\":{\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"proofOfDateOfBirth\":{\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"addressLine1\":{\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"addressLine2\":{\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"residenceStatus\":{\"fieldCategory\":\"optional\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"addressLine3\":{\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"email\":{\"validators\":[{\"validator\":\"^[a-zA-Z-\\\\+]+(\\\\.[a-zA-Z]+)*@[a-zA-Z-]+(\\\\.[a-zA-Z]+)*(\\\\.[a-zA-Z]{2,})$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"parentOrGuardianRID\":{\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"parentOrGuardianBiometrics\":{\"bioAttributes\":\"[face]\",\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/biometricsType\"},\"fullName\":{\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"dateOfBirth\":{\"validators\":[{\"validator\":\"^(1869|18[7-9][0-9]|19[0-9][0-9]|20[0-9][0-9])\\/([0][1-9]|1[0-2])\\/([0][1-9]|[1-2][0-9]|3[01])$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"parentOrGuardianUIN\":{\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"proofOfIdentity\":{\"fieldCategory\":\"none\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"IDSchemaVersion\":{\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"number\",\"fieldType\":\"default\",\"minimum\":0},\"proofOfException\":{\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"phone\":{\"validators\":[{\"validator\":\"^([6-9]{1})([0-9]{9})$\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"string\",\"fieldType\":\"default\"},\"parentOrGuardianName\":{\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"},\"proofOfRelationship\":{\"fieldCategory\":\"evidence\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/documentType\"},\"UIN\":{\"fieldCategory\":\"pvt\",\"format\":\"none\",\"type\":\"integer\",\"fieldType\":\"default\",\"minimum\":0},\"region\":{\"validators\":[{\"validator\":\"^(?=.{0,50}$).*\",\"arguments\":[],\"type\":\"regex\"}],\"fieldCategory\":\"pvt\",\"format\":\"none\",\"fieldType\":\"default\",\"$ref\":\"#\\/definitions\\/simpleType\"}}}}}";
	private static Map<String, String> categoryPacketMapping = new HashMap<>();
	
	@BeforeClass
	public static void setup() {		
		categoryPacketMapping.put("pvt", "id");
		categoryPacketMapping.put("kyc", "id");
		categoryPacketMapping.put("none", "id");
		categoryPacketMapping.put("evidence", "evidence");
		categoryPacketMapping.put("optional", "optional");
	}
	
	@Test
	public void createPacketStep0Test() {
		try {
			packetCreatorImpl.createPacket("test", 1.0, schemaJson, categoryPacketMapping, publicKey.getBytes(), null);
		} catch (PacketCreatorException e) {
			assertEquals(ErrorCode.INITIALIZATION_ERROR.getErrorCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void createPacketStep1Test() {
		packetCreatorImpl.initialize();
	}
	
	@Test
	public void createPacketStep2Test() {		
		List<SimpleDto> nameList = new ArrayList<>();
		SimpleDto fullName = new SimpleDto("eng", "testUser");
		nameList.add(fullName);		
		packetCreatorImpl.setField("fullName", nameList);
		List<SimpleDto> generList = new ArrayList<>();
		SimpleDto gender = new SimpleDto("eng", "female");
		generList.add(gender);		
		packetCreatorImpl.setField("gender", generList);
		List<SimpleDto> indiviaulStatusList = new ArrayList<>();
		SimpleDto indiviaulStatus = new SimpleDto("eng", "Non-Forienger");
		indiviaulStatusList.add(indiviaulStatus);		
		packetCreatorImpl.setField("residenceStatus", indiviaulStatusList);
	}
	
	@Test
	public void createPacketStep3Test() {
		packetCreatorImpl.setField("dateOfBirth", "23/05/2000");
		packetCreatorImpl.setField("postalCode", "NA");
		packetCreatorImpl.setField("phone", "9090909090");
		packetCreatorImpl.setField("email", "test@test.com");
		packetCreatorImpl.setField("email", "test@test.com");
	}
	
	@Test
	public void createPacketStep4Test() {
		DocumentDto poa = new DocumentDto();
		poa.setCategory("POA");
		poa.setFormat("pdf");
		poa.setOwner("user");
		poa.setValue("POA_Driving licence of the applicant");
		poa.setType("Driving licence of the applicant");
		poa.setDocument("test agreement content".getBytes());
		packetCreatorImpl.setDocument("proofOfAddress", poa);
		
		DocumentDto poi = new DocumentDto();
		poi.setCategory("POI");
		poi.setFormat("pdf");
		poi.setOwner("user");
		poi.setValue("POI_Service photo ID cards that is issued by a PSU");
		poi.setType("Service photo ID cards that is issued by a PSU");
		poi.setDocument("test passport content".getBytes());
		packetCreatorImpl.setDocument("proofOfIdentity", poi);
		
		DocumentDto poe = new DocumentDto();
		poe.setCategory("POE");
		poe.setFormat("jpg");
		poe.setOwner("user");
		poe.setValue("POE_Certification of Exception");
		poe.setType("Certification of Exception");
		poe.setDocument("test exception photograph".getBytes());
		packetCreatorImpl.setDocument("proofOfException", poe);		
	}
	
	@Test
	public void createPacketStep5Test() {
		List<AuditDto> audits = new ArrayList<AuditDto>();
		AuditDto dto = new AuditDto();
		dto.setEventId("001");
		audits.add(dto);
		AuditDto dto2 = new AuditDto();
		dto2.setEventId("002");
		audits.add(dto2);
		packetCreatorImpl.setAudits(audits);
	}
	
	@Test
	public void createPacketStep6Test() {
		packetCreatorImpl.setAcknowledgement("test-receipt", "test ack content".getBytes());
	}
	
	/*@Test
	public void createPacketStep7Test() throws java.io.IOException {
		List<BiometricsDto> list = new ArrayList<BiometricsDto>();
		BiometricsDto dto = new BiometricsDto();
		dto.setFormatType(7);
		dto.setModalityName("Iris");
		dto.setSubType("Left");
		dto.setModalityISO(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("leftEye.iso")));
		dto.setQualityScore(90.0);
		dto.setNumOfRetries(2);
		dto.setType(SingleType.IRIS);
		dto.setForceCaptured(false);
		list.add(dto);
		packetCreatorImpl.setBiometric("individualBiometrics", list);
	}*/
	
	@Test
	public void createPacketStep8Test() {
		List<ModalityException> list = new ArrayList<>();
		ModalityException exception = new ModalityException();
		exception.setIndividualType("applicant");
		exception.setMissingBiometric("LeftIndex");
		exception.setReason("missing finger");
		exception.setType("Permanent");
		exception.setExceptionType("FIRS");
		list.add(exception);
		packetCreatorImpl.setBiometricException("individualBiometrics", list);
	}
	
	@Test
	public void createPacketStep7Test() {
		packetCreatorImpl.setMetaInfo("consentOfApplicant", "Yes");
		packetCreatorImpl.setMetaInfo("creationDate", 
				LocalDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
		packetCreatorImpl.setOperationsInfo("officerId", "10007");
		packetCreatorImpl.setOperationsInfo("supervisorId", null);
		packetCreatorImpl.setOperationsInfo("supervisorPassword", "false");
		packetCreatorImpl.setOperationsInfo("supervisorPIN", "false");
		packetCreatorImpl.setOperationsInfo("supervisorOTPAuthentication", "false");
		packetCreatorImpl.setOperationsInfo("officerPassword", "true");
		packetCreatorImpl.setOperationsInfo("officerPIN", "false");
		packetCreatorImpl.setOperationsInfo("officerOTPAuthentication", "false");
		packetCreatorImpl.setOperationsInfo("officerBiometricFileName", null);
		packetCreatorImpl.setOperationsInfo("supervisorBiometricFileName",  null);
	}
	
	
	@Test
	public void createPacketStep999Test() throws PacketCreatorException, IOException, java.io.IOException {
		String registrationId = "10007103600000420200510150916";
		byte[] packet = packetCreatorImpl.createPacket(registrationId, 1.0, schemaJson, categoryPacketMapping, publicKey.getBytes(), null);
		assertNotNull(packet);
		
		FileUtils.writeByteArrayToFile(new File("/opt/mosip/packets/"+registrationId+".zip"), packet);
	}

}