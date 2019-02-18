package io.mosip.kernel.idrepo.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.FieldComparisonFailure;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import io.mosip.kernel.core.cbeffutil.entity.BDBInfo;
import io.mosip.kernel.core.cbeffutil.entity.BIR;
import io.mosip.kernel.core.cbeffutil.entity.BIRInfo;
import io.mosip.kernel.core.cbeffutil.entity.BIRVersion;
import io.mosip.kernel.core.cbeffutil.entity.SBInfo;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.BIRType;
import io.mosip.kernel.core.cbeffutil.spi.CbeffUtil;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ParseException;
import io.mosip.kernel.core.idrepo.constant.IdRepoErrorConstants;
import io.mosip.kernel.core.idrepo.constant.RestServicesConstants;
import io.mosip.kernel.core.idrepo.exception.IdRepoAppException;
import io.mosip.kernel.core.idrepo.exception.IdRepoAppUncheckedException;
import io.mosip.kernel.core.idrepo.exception.RestServiceException;
import io.mosip.kernel.core.idrepo.spi.IdRepoService;
import io.mosip.kernel.core.idrepo.spi.MosipDFSProvider;
import io.mosip.kernel.core.idrepo.spi.MosipFingerprintProvider;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.util.HMACUtils;
import io.mosip.kernel.core.util.UUIDUtils;
import io.mosip.kernel.idrepo.config.IdRepoLogger;
import io.mosip.kernel.idrepo.dto.Documents;
import io.mosip.kernel.idrepo.dto.IdRequestDTO;
import io.mosip.kernel.idrepo.dto.RequestDTO;
import io.mosip.kernel.idrepo.dto.RestRequestDTO;
import io.mosip.kernel.idrepo.entity.Uin;
import io.mosip.kernel.idrepo.entity.UinBiometric;
import io.mosip.kernel.idrepo.entity.UinBiometricHistory;
import io.mosip.kernel.idrepo.entity.UinDocument;
import io.mosip.kernel.idrepo.entity.UinDocumentHistory;
import io.mosip.kernel.idrepo.entity.UinHistory;
import io.mosip.kernel.idrepo.factory.RestRequestFactory;
import io.mosip.kernel.idrepo.helper.RestHelper;
import io.mosip.kernel.idrepo.repository.UinBiometricHistoryRepo;
import io.mosip.kernel.idrepo.repository.UinDocumentHistoryRepo;
import io.mosip.kernel.idrepo.repository.UinHistoryRepo;
import io.mosip.kernel.idrepo.repository.UinRepo;

@Component
public class IdRepoServiceImpl implements IdRepoService<IdRequestDTO, Uin> {
	
	private static final String GET_FILES = "getFiles";

	private static final String DECRYPT = "decrypt";

	private static final String ENCRYPT = "encrypt";

	private static final String DATETIME_TIMEZONE = "mosip.kernel.idrepo.datetime.timezone";
	
	private static final String UPDATE_IDENTITY = "updateIdentity";

	/** The Constant ROOT. */
	private static final String ROOT = "$";

	/** The Constant OPEN_SQUARE_BRACE. */
	private static final String OPEN_SQUARE_BRACE = "[";

	/** The Constant LANGUAGE. */
	private static final String LANGUAGE = "language";

	/** The Constant ADD_IDENTITY. */
	private static final String ADD_IDENTITY = "addIdentity";

	/** The mosip logger. */
	Logger mosipLogger = IdRepoLogger.getLogger(IdRepoProxyServiceImpl.class);

	/** The Constant ID_REPO_SERVICE. */
	private static final String ID_REPO_SERVICE = "IdRepoService";

	/** The Constant DOCUMENTS. */
	private static final String DOCUMENTS = "documents";

	/** The Constant TYPE. */
	private static final String TYPE = "type";

	/** The Constant DOT. */
	private static final String DOT = ".";

	/** The Constant SLASH. */
	private static final String SLASH = "/";

	/** The Constant BIOMETRICS. */
	private static final String BIOMETRICS = "Biometrics";

	/** The Constant ID_REPO_SERVICE_IMPL. */
	private static final String ID_REPO_SERVICE_IMPL = "IdRepoServiceImpl";

	/** The Constant CBEFF. */
	private static final String CBEFF = "cbeff";

	/** The Constant FORMAT. */
	private static final String FORMAT = "format";

	/** The Constant VALUE. */
	private static final String VALUE = "value";

	/** The Constant LANG_CODE. */
	private static final String LANG_CODE = "AR";

	/** The Constant DATETIME_PATTERN. */
	private static final String DATETIME_PATTERN = "mosip.kernel.idrepo.datetime.pattern";

	/** The Constant CREATED_BY. */
	private static final String CREATED_BY = "createdBy";

	/** The Constant MOSIP_IDREPO_STATUS_REGISTERED. */
	private static final String MOSIP_IDREPO_STATUS_REGISTERED = "mosip.kernel.idrepo.status.registered";

	/** The Constant UPDATED_BY. */
	private static final String UPDATED_BY = "updatedBy";

	/** The Constant DEMOGRAPHICS. */
	private static final String DEMOGRAPHICS = "Demographics";
	
	@Autowired
	private RestRequestFactory restFactory;
	
	@Autowired
	private RestHelper restHelper;

	/** The env. */
	@Autowired
	private Environment env;

	/** The mapper. */
	@Autowired
	private ObjectMapper mapper;

	/** The uin repo. */
	@Autowired
	private UinRepo uinRepo;

	/** The uin detail repo. */
	@Autowired
	private UinDocumentHistoryRepo uinDocHRepo;

	/** The uin bio H repo. */
	@Autowired
	private UinBiometricHistoryRepo uinBioHRepo;

	/** The uin history repo. */
	@Autowired
	private UinHistoryRepo uinHistoryRepo;

	/** The fp provider. */
	@Autowired
	private MosipFingerprintProvider<BIRType, BIR> fpProvider;

	/** The cbeff util. */
	@Autowired
	private CbeffUtil cbeffUtil;

	/** The dfs provider. */
	@Autowired
	private MosipDFSProvider dfsProvider;

	/**
	 * Adds the identity to DB.
	 *
	 * @param uin
	 *            the uin
	 * @param regId
	 *            the uin ref id
	 * @param identityInfo
	 *            the identity info
	 * @param documents
	 *            the documents
	 * @return the uin
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	@Transactional
	public Uin addIdentity(IdRequestDTO request, String uin) throws IdRepoAppException {
		String uinRefId = UUIDUtils
				.getUUID(UUIDUtils.NAMESPACE_OID,
						uin + "_" + DateUtils.getUTCCurrentDateTime()
								.atZone(ZoneId.of(env.getProperty(DATETIME_TIMEZONE))).toInstant().toEpochMilli())
				.toString();
		byte[] identityInfo = convertToBytes(request.getRequest().getIdentity());

		if (!uinRepo.existsByRegId(request.getRegistrationId()) && !uinRepo.existsByUin(uin)) {
			List<UinDocument> docList = new ArrayList<>();
			List<UinBiometric> bioList = new ArrayList<>();
			if (Objects.nonNull(request.getRequest().getDocuments())
					&& !request.getRequest().getDocuments().isEmpty()) {
				addDocuments(uin, identityInfo, request.getRequest().getDocuments(), uinRefId, docList, bioList);

				uinRepo.save(new Uin(uinRefId, uin, identityInfo, hash(identityInfo), request.getRegistrationId(),
						env.getProperty(MOSIP_IDREPO_STATUS_REGISTERED), LANG_CODE, CREATED_BY, now(), UPDATED_BY,
						now(), false, now(), bioList, docList));
				mosipLogger.debug(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, ADD_IDENTITY,
						"Record successfully saved in db with documents");
			} else {
				uinRepo.save(new Uin(uinRefId, uin, identityInfo, hash(identityInfo), request.getRegistrationId(),
						env.getProperty(MOSIP_IDREPO_STATUS_REGISTERED), LANG_CODE, CREATED_BY, now(), UPDATED_BY,
						now(), false, now(), null, null));
				mosipLogger.debug(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, ADD_IDENTITY,
						"Record successfully saved in db without documents");
			}

			uinHistoryRepo.save(new UinHistory(uinRefId, now(), uin, identityInfo, hash(identityInfo),
					request.getRegistrationId(), env.getProperty(MOSIP_IDREPO_STATUS_REGISTERED), LANG_CODE, CREATED_BY,
					now(), UPDATED_BY, now(), false, now()));

			return retrieveIdentity(uin, null);
		} else {
			mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, ADD_IDENTITY,
					IdRepoErrorConstants.RECORD_EXISTS.getErrorMessage());
			throw new IdRepoAppException(IdRepoErrorConstants.RECORD_EXISTS);
		}
	}

	/**
	 * Adds the documents.
	 *
	 * @param uin
	 *            the uin
	 * @param identityInfo
	 *            the identity info
	 * @param documents
	 *            the documents
	 * @param uinRefId
	 *            the uin ref id
	 * @param docList
	 *            the doc list
	 * @param bioList
	 *            the bio list
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	public void addDocuments(String uin, byte[] identityInfo, List<Documents> documents, String uinRefId,
			List<UinDocument> docList, List<UinBiometric> bioList) throws IdRepoAppException {
		ObjectNode identityObject = (ObjectNode) convertToObject(identityInfo, ObjectNode.class);
		documents.stream().filter(doc -> identityObject.has(doc.getCategory())).forEach(doc -> {
			JsonNode docType = identityObject.get(doc.getCategory());
			try {
				if (StringUtils.equalsIgnoreCase(docType.get(FORMAT).asText(), CBEFF)) {
					String fileRefId = UUIDUtils.getUUID(UUIDUtils.NAMESPACE_OID,
							docType.get(VALUE).asText() + "_" + DateUtils.getUTCCurrentDateTime()
									.atZone(ZoneId.of(env.getProperty(DATETIME_TIMEZONE))).toInstant().toEpochMilli())
							.toString();
					byte[] cbeffDoc = convertToFMR(doc.getCategory(), doc.getValue());

					dfsProvider.storeFile(uin, BIOMETRICS + SLASH + fileRefId + DOT + docType.get(FORMAT).asText(),
							CryptoUtil.decodeBase64(
									new String(encryptDecryptDocuments(CryptoUtil.encodeBase64(cbeffDoc), ENCRYPT))));

					bioList.add(new UinBiometric(uinRefId, fileRefId, doc.getCategory(), docType.get(VALUE).asText(),
							hash(cbeffDoc), LANG_CODE, CREATED_BY, now(), UPDATED_BY, now(), false, now()));

					uinBioHRepo.save(new UinBiometricHistory(uinRefId, now(), fileRefId, doc.getCategory(),
							docType.get(VALUE).asText(), hash(CryptoUtil.decodeBase64(doc.getValue())), LANG_CODE,
							CREATED_BY, now(), UPDATED_BY, now(), false, now()));

				} else {
					String fileRefId = docType.get(VALUE).asText() + "_" + DateUtils.getUTCCurrentDateTime()
							.atZone(ZoneId.of(env.getProperty(DATETIME_TIMEZONE))).toInstant().toEpochMilli();

					dfsProvider.storeFile(uin, DEMOGRAPHICS + SLASH + fileRefId + DOT + docType.get(FORMAT).asText(),
							CryptoUtil.decodeBase64(
									new String(encryptDecryptDocuments(doc.getValue(), ENCRYPT))));

					docList.add(new UinDocument(uinRefId, doc.getCategory(), docType.get(TYPE).asText(), fileRefId,
							docType.get(VALUE).asText(), docType.get(FORMAT).asText(),
							hash(CryptoUtil.decodeBase64(doc.getValue())), LANG_CODE, CREATED_BY, now(), UPDATED_BY,
							now(), false, now()));

					uinDocHRepo.save(new UinDocumentHistory(uinRefId, now(), doc.getCategory(),
							docType.get(TYPE).asText(), fileRefId, docType.get(VALUE).asText(),
							docType.get(FORMAT).asText(), hash(CryptoUtil.decodeBase64(doc.getValue())), LANG_CODE,
							CREATED_BY, now(), UPDATED_BY, now(), false, now()));
				}
			} catch (DataAccessException e) {
				mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, ADD_IDENTITY,
						"\n" + ExceptionUtils.getStackTrace(e));
				throw new IdRepoAppUncheckedException(IdRepoErrorConstants.FILE_STORAGE_ACCESS_ERROR, e);
			} catch (IdRepoAppException e) {
				mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, ADD_IDENTITY,
						"\n" + ExceptionUtils.getStackTrace(e));
				throw new IdRepoAppUncheckedException(e.getErrorCode(), e.getErrorText(), e);
			} catch (JDBCConnectionException e) {
				mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, ADD_IDENTITY,
						"\n" + ExceptionUtils.getStackTrace(e));
				throw new IdRepoAppUncheckedException(IdRepoErrorConstants.DATABASE_ACCESS_ERROR, e);
			}
		});
	}

	/**
	 * Convert to FMR.
	 *
	 * @param category
	 *            the category
	 * @param encodedCbeffFile
	 *            the encoded cbeff file
	 * @return the byte[]
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	private byte[] convertToFMR(String category, String encodedCbeffFile) throws IdRepoAppException {
		try {
			byte[] cbeffFileData = CryptoUtil.decodeBase64(encodedCbeffFile);
			return cbeffUtil.updateXML(fpProvider.convertFIRtoFMR(cbeffUtil.getBIRDataFromXML(cbeffFileData)),
					cbeffFileData);
		} catch (Exception e) {
			mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, ADD_IDENTITY,
					"\n" + ExceptionUtils.getStackTrace(e));
			throw new IdRepoAppException(IdRepoErrorConstants.INVALID_INPUT_PARAMETER.getErrorCode(), String.format(
					IdRepoErrorConstants.INVALID_INPUT_PARAMETER.getErrorMessage(), DOCUMENTS + " - " + category));
		}
	}

	/**
	 * Retrieve identity by uin from DB.
	 *
	 * @param uin
	 *            the uin
	 * @return the uin
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	@Transactional
	public Uin retrieveIdentity(String uin, String type) throws IdRepoAppException {
		return uinRepo.findByUin(uin);
	}
	
	@Transactional
	public Uin updateIdentity(IdRequestDTO request, String uin) throws IdRepoAppException {
		try {
			Uin uinObject = retrieveIdentity(uin, null);
			uinObject.setRegId(request.getRegistrationId());
			if (Objects.nonNull(request.getStatus())
					&& !StringUtils.equals(uinObject.getStatusCode(), request.getStatus())) {
				uinObject.setStatusCode(request.getStatus());
				uinObject.setUpdatedDateTime(now());
			}
			if (Objects.nonNull(request.getRequest()) && Objects.nonNull(request.getRequest().getIdentity())) {
				RequestDTO requestDTO = request.getRequest();
				Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonProvider())
						.mappingProvider(new JacksonMappingProvider()).build();
				DocumentContext inputData = JsonPath.using(configuration).parse(requestDTO.getIdentity());
				DocumentContext dbData = JsonPath.using(configuration).parse(new String(uinObject.getUinData()));
				JSONCompareResult comparisonResult = JSONCompare.compareJSON(inputData.jsonString(),
						dbData.jsonString(), JSONCompareMode.LENIENT);

				if (comparisonResult.failed()) {
					updateIdentityObject(inputData, dbData, comparisonResult);
					uinObject.setUinData(convertToBytes(convertToObject(dbData.jsonString().getBytes(), Map.class)));
					uinObject.setUinDataHash(hash(uinObject.getUinData()));
					uinObject.setUpdatedDateTime(now());
				}

				if (Objects.nonNull(requestDTO.getDocuments()) && !requestDTO.getDocuments().isEmpty()) {
					updateDocuments(uin, uinObject, requestDTO);
					uinObject.setUpdatedDateTime(now());
				}

			}
			uinHistoryRepo.save(new UinHistory(uinObject.getUinRefId(), now(), uin, uinObject.getUinData(),
					uinObject.getUinDataHash(), uinObject.getRegId(), uinObject.getStatusCode(), LANG_CODE, CREATED_BY,
					now(), UPDATED_BY, now(), false, now()));

			return uinRepo.save(uinObject);
		} catch (JSONException | InvalidJsonException e) {
			mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, UPDATE_IDENTITY,
					"\n" + ExceptionUtils.getStackTrace(e));
			throw new IdRepoAppException(IdRepoErrorConstants.JSON_PROCESSING_FAILED, e);
		}
	}
	
	/**
	 * Update identity.
	 *
	 * @param inputData
	 *            the input data
	 * @param dbData
	 *            the db data
	 * @param comparisonResult
	 *            the comparison result
	 * @throws JSONException
	 *             the JSON exception
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	private void updateIdentityObject(DocumentContext inputData, DocumentContext dbData, JSONCompareResult comparisonResult)
			throws JSONException, IdRepoAppException {
		if (comparisonResult.isMissingOnField()) {
			updateMissingFields(dbData, comparisonResult);
		}

		comparisonResult = JSONCompare.compareJSON(inputData.jsonString(), dbData.jsonString(),
				JSONCompareMode.LENIENT);
		if (comparisonResult.isFailureOnField()) {
			updateFailingFields(inputData, dbData, comparisonResult);
		}

		comparisonResult = JSONCompare.compareJSON(inputData.jsonString(), dbData.jsonString(),
				JSONCompareMode.LENIENT);
		if (!comparisonResult.getMessage().isEmpty()) {
			updateMissingValues(inputData, dbData, comparisonResult);
		}

		comparisonResult = JSONCompare.compareJSON(inputData.jsonString(), dbData.jsonString(),
				JSONCompareMode.LENIENT);
		if (comparisonResult.failed()) {
			updateIdentityObject(inputData, dbData, comparisonResult);
		}
	}

	/**
	 * Update missing fields.
	 *
	 * @param dbData
	 *            the db data
	 * @param comparisonResult
	 *            the comparison result
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateMissingFields(DocumentContext dbData, JSONCompareResult comparisonResult)
			throws IdRepoAppException {
		for (FieldComparisonFailure failure : comparisonResult.getFieldMissing()) {
			if (StringUtils.contains(failure.getField(), OPEN_SQUARE_BRACE)) {
				String path = StringUtils.substringBefore(failure.getField(), OPEN_SQUARE_BRACE);
				String key = StringUtils.substringAfterLast(path, DOT);
				path = StringUtils.substringBeforeLast(path, DOT);

				if (StringUtils.isEmpty(key)) {
					key = path;
					path = ROOT;
				}

				List value = dbData.read(path + DOT + key, List.class);
				value.addAll((Collection) Collections
						.singletonList(convertToObject(failure.getExpected().toString().getBytes(), Map.class)));

				dbData.put(path, key, value);
			} else {
				String path = StringUtils.substringBeforeLast(failure.getField(), DOT);
				if (StringUtils.isEmpty(path)) {
					path = ROOT;
				}
				String key = StringUtils.substringAfterLast(failure.getField(), DOT);
				dbData.put(path, (String) failure.getExpected(), key);
			}

		}
	}

	/**
	 * Update failing fields.
	 *
	 * @param inputData
	 *            the input data
	 * @param dbData
	 *            the db data
	 * @param comparisonResult
	 *            the comparison result
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	private void updateFailingFields(DocumentContext inputData, DocumentContext dbData,
			JSONCompareResult comparisonResult) throws IdRepoAppException {
		for (FieldComparisonFailure failure : comparisonResult.getFieldFailures()) {

			String path = StringUtils.substringBeforeLast(failure.getField(), DOT);
			if (StringUtils.contains(path, OPEN_SQUARE_BRACE)) {
				path = StringUtils.replaceAll(path, "\\[", "\\[\\?\\(\\@\\.");
				path = StringUtils.replaceAll(path, "=", "=='");
				path = StringUtils.replaceAll(path, "\\]", "'\\)\\]");
			}
			String key = StringUtils.substringAfterLast(failure.getField(), DOT);
			if (StringUtils.isEmpty(key)) {
				key = failure.getField();
				path = ROOT;
			}

			if (failure.getExpected() instanceof JSONArray) {
				dbData.put(path, key, convertToObject(failure.getExpected().toString().getBytes(), List.class));
				inputData.put(path, key, convertToObject(failure.getExpected().toString().getBytes(), List.class));
			} else if (failure.getExpected() instanceof JSONObject) {
				Object object = convertToObject(failure.getExpected().toString().getBytes(), ObjectNode.class);
				dbData.put(path, key, object);
				inputData.put(path, key, object);
			} else {
				dbData.put(path, key, failure.getExpected());
				inputData.put(path, key, failure.getExpected());
			}
		}
	}

	/**
	 * Update missing values.
	 *
	 * @param inputData
	 *            the input data
	 * @param dbData
	 *            the db data
	 * @param comparisonResult
	 *            the comparison result
	 */
	@SuppressWarnings("unchecked")
	private void updateMissingValues(DocumentContext inputData, DocumentContext dbData,
			JSONCompareResult comparisonResult) {
		String path = StringUtils.substringBefore(comparisonResult.getMessage(), OPEN_SQUARE_BRACE);
		String key = StringUtils.substringAfterLast(path, DOT);
		path = StringUtils.substringBeforeLast(path, DOT);

		if (StringUtils.isEmpty(key)) {
			key = path;
			path = ROOT;
		}

		List<Map<String, String>> dbDataList = dbData.read(path + DOT + key, List.class);
		List<Map<String, String>> inputDataList = inputData.read(path + DOT + key, List.class);
		inputDataList.stream().filter(
				map -> map.containsKey(LANGUAGE) && dbDataList.stream().filter(dbMap -> dbMap.containsKey(LANGUAGE))
						.allMatch(dbMap -> !StringUtils.equalsIgnoreCase(dbMap.get(LANGUAGE), map.get(LANGUAGE))))
				.forEach(dbDataList::add);
		dbDataList
				.stream().filter(
						map -> map.containsKey(LANGUAGE)
								&& inputDataList.stream().filter(inputDataMap -> inputDataMap.containsKey(LANGUAGE))
										.allMatch(inputDataMap -> !StringUtils
												.equalsIgnoreCase(inputDataMap.get(LANGUAGE), map.get(LANGUAGE))))
				.forEach(inputDataList::add);
	}

	/**
	 * Update documents.
	 *
	 * @param uin
	 *            the uin
	 * @param uinObject
	 *            the uin object
	 * @param requestDTO
	 *            the request DTO
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	private void updateDocuments(String uin, Uin uinObject, RequestDTO requestDTO) throws IdRepoAppException {
		List<UinDocument> docList = new ArrayList<>();
		List<UinBiometric> bioList = new ArrayList<>();

		if (Objects.nonNull(uinObject.getBiometrics())) {
			updateCbeff(uinObject, requestDTO);
		}

		addDocuments(uin, convertToBytes(requestDTO.getIdentity()), requestDTO.getDocuments(), uinObject.getUinRefId(),
				docList, bioList);

		docList.stream().forEach(doc -> uinObject.getDocuments().stream()
				.filter(docObj -> StringUtils.equals(doc.getDoccatCode(), docObj.getDoccatCode())).forEach(docObj -> {
					docObj.setDocId(doc.getDocId());
					docObj.setDocName(doc.getDocName());
					docObj.setDocfmtCode(doc.getDocfmtCode());
					docObj.setDocHash(doc.getDocHash());
					docObj.setUpdatedDateTime(doc.getUpdatedDateTime());
				}));
		docList.stream()
				.filter(doc -> uinObject.getDocuments().stream()
						.allMatch(docObj -> !StringUtils.equals(doc.getDoccatCode(), docObj.getDoccatCode())))
				.forEach(doc -> uinObject.getDocuments().add(doc));
		bioList.stream()
				.forEach(bio -> uinObject.getBiometrics().stream()
						.filter(bioObj -> StringUtils.equals(bio.getBiometricFileType(), bioObj.getBiometricFileType()))
						.forEach(bioObj -> {
							bioObj.setBioFileId(bio.getBioFileId());
							bioObj.setBiometricFileName(bio.getBiometricFileName());
							bioObj.setBiometricFileHash(bio.getBiometricFileHash());
							bioObj.setUpdatedDateTime(bio.getUpdatedDateTime());
						}));
		bioList.stream()
				.filter(bio -> uinObject.getBiometrics().stream()
						.allMatch(bioObj -> !StringUtils.equals(bio.getBioFileId(), bioObj.getBioFileId())))
				.forEach(bio -> uinObject.getBiometrics().add(bio));
	}

	/**
	 * Update cbeff.
	 *
	 * @param uinObject
	 *            the uin object
	 * @param requestDTO
	 *            the request DTO
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	private void updateCbeff(Uin uinObject, RequestDTO requestDTO) throws IdRepoAppException {
		ObjectNode identityMap = (ObjectNode) convertToObject(uinObject.getUinData(), ObjectNode.class);

		uinObject.getBiometrics().stream().forEach(bio -> requestDTO.getDocuments().stream()
				.filter(doc -> 
				StringUtils.equals(bio.getBiometricFileType(), doc.getCategory()))
				.forEach(doc -> {
					try {
						String fileName = BIOMETRICS + SLASH + bio.getBioFileId() + DOT
								+ identityMap.get(bio.getBiometricFileType()).get(FORMAT).asText();
						String data = new String(encryptDecryptDocuments(
								CryptoUtil.encodeBase64(dfsProvider.getFile(uinObject.getUin(), fileName)), DECRYPT));
						doc.setValue(CryptoUtil.encodeBase64(cbeffUtil.updateXML(
								convertToBIR(cbeffUtil.getBIRDataFromXML(CryptoUtil.decodeBase64(doc.getValue()))),
								CryptoUtil.decodeBase64(data))));
					} catch (IdRepoAppException e) {
						mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, GET_FILES,
								"\n" + ExceptionUtils.getStackTrace(e));
						throw new IdRepoAppUncheckedException(e.getErrorCode(), e.getErrorText(), e);
					} catch (Exception e) {
						mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, ADD_IDENTITY,
								"\n" + ExceptionUtils.getStackTrace(e));
						throw new IdRepoAppUncheckedException(
								IdRepoErrorConstants.INVALID_INPUT_PARAMETER.getErrorCode(),
								String.format(IdRepoErrorConstants.INVALID_INPUT_PARAMETER.getErrorMessage(),
										DOCUMENTS + " - " + doc.getCategory()));
					}
				}));
	}

	/**
	 * Converts all BIRType to BIR.
	 *
	 * @param birTypeList
	 *            the bir type list
	 * @return the list of BIR
	 */
	private List<BIR> convertToBIR(List<BIRType> birTypeList) {
		return birTypeList.stream()
				.filter(birType -> Objects.nonNull(birType.getBDBInfo()) && 
						!birType.getBDBInfo().getFormatType().equals(2l))
				.map(birType -> new BIR.BIRBuilder()
						.withVersion(Optional.ofNullable(birType.getVersion())
								.map(birVersion -> new BIRVersion.BIRVersionBuilder()
										.withMajor(birVersion.getMajor())
										.withMinor(birVersion.getMinor())
										.build())
								.orElseGet(() -> null))
						.withCbeffversion(Optional.ofNullable(birType.getCBEFFVersion())
								.map(cbeffVersion -> new BIRVersion.BIRVersionBuilder()
										.withMajor(cbeffVersion.getMajor())
										.withMinor(cbeffVersion.getMinor())
										.build())
								.orElseGet(() -> null))
						.withBirInfo(Optional.ofNullable(birType.getBIRInfo())
								.map(birInfo -> new BIRInfo.BIRInfoBuilder()
										.withCreator(birInfo.getCreator())
										.withIndex(birInfo.getIndex())
										.withPayload(birInfo.getPayload())
										.withIntegrity(birInfo.isIntegrity())
										.withCreationDate(birInfo.getCreationDate())
										.withNotValidBefore(birInfo.getNotValidBefore())
										.withNotValidAfter(birInfo.getNotValidAfter())
										.build())
								.orElseGet(() -> null))
						.withBdbInfo(Optional.ofNullable(birType.getBDBInfo())
								.map(bdbInfo -> new BDBInfo.BDBInfoBuilder()
										.withChallengeResponse(bdbInfo.getChallengeResponse())
										.withIndex(bdbInfo.getIndex())
										.withFormatOwner(bdbInfo.getFormatOwner())
										.withFormatType(bdbInfo.getFormatType())
										.withEncryption(bdbInfo.getEncryption())
										.withCreationDate(bdbInfo.getCreationDate())
										.withNotValidBefore(bdbInfo.getNotValidBefore())
										.withNotValidAfter(bdbInfo.getNotValidAfter())
										.withType(bdbInfo.getType())
										.withSubtype(bdbInfo.getSubtype())
										.withLevel(bdbInfo.getLevel())
										.withProductOwner(bdbInfo.getProductOwner())
										.withProductType(bdbInfo.getProductType())
										.withPurpose(bdbInfo.getPurpose())
										.withQuality(bdbInfo.getQuality())
										.build())
								.orElseGet(() -> null))
						.withBdb(birType.getBDB())
						.withSb(birType.getSB())
						.withSbInfo(Optional.ofNullable(birType.getSBInfo())
								.map(sbInfo -> new SBInfo.SBInfoBuilder()
										.setFormatOwner(sbInfo.getFormatOwner())
										.setFormatType(sbInfo.getFormatType())
										.build())
								.orElseGet(() -> null))
						.build())
				.collect(Collectors.toList());
	}
	
	/**
	 * Hash.
	 *
	 * @param identityInfo
	 *            the identity info
	 * @return the string
	 */
	private String hash(byte[] identityInfo) {
		return HMACUtils.digestAsPlainText(HMACUtils.generateHash(identityInfo));
	}
	
	/**
	 * Get the current time.
	 *
	 * @return the date
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	private LocalDateTime now() throws IdRepoAppException {
		try {
			return DateUtils.parseUTCToLocalDateTime(
					DateUtils.formatDate(new Date(), env.getProperty(DATETIME_PATTERN)),
					env.getProperty(DATETIME_PATTERN));
		} catch (ParseException e) {
			mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, "now()", "\n" + ExceptionUtils.getStackTrace(e));
			throw new IdRepoAppException(IdRepoErrorConstants.INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(IdRepoErrorConstants.INVALID_INPUT_PARAMETER.getErrorCode(), "DATETIME_PATTERN"), e);
		}
	}
	
	/**
	 * Convert to object.
	 *
	 * @param identity
	 *            the identity
	 * @param clazz
	 *            the clazz
	 * @return the object
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	private Object convertToObject(byte[] identity, Class<?> clazz) throws IdRepoAppException {
		try {
			return mapper.readValue(identity, clazz);
		} catch (IOException e) {
			mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, "convertToObject",
					"\n" + ExceptionUtils.getStackTrace(e));
			throw new IdRepoAppException(IdRepoErrorConstants.JSON_PROCESSING_FAILED, e);
		}
	}
	
	/**
	 * Convert to bytes.
	 *
	 * @param identity
	 *            the identity
	 * @return the byte[]
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	private byte[] convertToBytes(Object identity) throws IdRepoAppException {
		try {
			return mapper.writeValueAsBytes(identity);
		} catch (JsonProcessingException e) {
			mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, "convertToBytes",
					"\n" + ExceptionUtils.getStackTrace(e));
			throw new IdRepoAppException(IdRepoErrorConstants.JSON_PROCESSING_FAILED, e);
		}
	}
	
	/**
	 * Encrypt identity.
	 *
	 * @param identity
	 *            the identity
	 * @param method
	 *            the method
	 * @return the byte[]
	 * @throws IdRepoAppException
	 *             the id repo app exception
	 */
	private byte[] encryptDecryptDocuments(String document, String method) throws IdRepoAppException {
		try {
			RestRequestDTO restRequest = null;
			ObjectNode request = new ObjectNode(mapper.getNodeFactory());
			request.put("applicationId", env.getProperty("mosip.kernel.idrepo.application.id"));
			request.put("timeStamp", DateUtils.formatDate(new Date(), env.getProperty(DATETIME_PATTERN)));
			request.put("data", document);

			if (method.equals(ENCRYPT)) {
				restRequest = restFactory.buildRequest(RestServicesConstants.CRYPTO_MANAGER_ENCRYPT, request, ObjectNode.class);
			} else {
				restRequest = restFactory.buildRequest(RestServicesConstants.CRYPTO_MANAGER_DECRYPT, request, ObjectNode.class);
			}
			
			ObjectNode response = restHelper.requestSync(restRequest);

			if (response.has("data")) {
				return response.get("data").asText().getBytes();
			} else {
				mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, "encryptDecryptIdentity",
						"No data block found in response");
				throw new IdRepoAppException(IdRepoErrorConstants.ENCRYPTION_DECRYPTION_FAILED);
			}
		} catch (RestServiceException e) {
			mosipLogger.error(ID_REPO_SERVICE, ID_REPO_SERVICE_IMPL, "encryptDecryptIdentity",
					"\n" + ExceptionUtils.getStackTrace(e));
			throw new IdRepoAppException(IdRepoErrorConstants.ENCRYPTION_DECRYPTION_FAILED, e);
		}
	}
}
