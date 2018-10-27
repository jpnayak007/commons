package io.mosip.kernel.smsnotification.msg91.test.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.mosip.kernel.smsnotification.msg91.SmsNotifierBootApplication;
import io.mosip.kernel.smsnotification.msg91.constant.SmsPropertyConstants;
import io.mosip.kernel.smsnotification.msg91.dto.SmsResponseDto;
import io.mosip.kernel.smsnotification.msg91.dto.SmsServerResponseDto;
import io.mosip.kernel.smsnotification.msg91.exception.MosipInvalidNumberException;
import io.mosip.kernel.smsnotification.msg91.service.impl.SmsNotifierServiceImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { SmsNotifierBootApplication.class })
@TestPropertySource("classpath:application.properties")
public class SmsNotifierServiceTest {

	@Autowired
	SmsNotifierServiceImpl service;

	@MockBean
	RestTemplateBuilder restTemplateBuilder;

	@Value("${mosip.kernel.sms.api}")
	String api;

	@Value("${mosip.kernel.sms.authkey}")
	String authkey;

	@Value("${mosip.kernel.sms.country.code}")
	String countryCode;

	@Value("${mosip.kernel.sms.sender}")
	String senderId;

	@Value("${mosip.kernel.sms.route}")
	String route;

	@Value("${mosip.kernel.sms.number.length}")
	String length;

	@Test
	public void sendSmsNotificationTest() {

		UriComponentsBuilder sms = UriComponentsBuilder.fromHttpUrl(api)
				.queryParam(SmsPropertyConstants.AUTH_KEY.getProperty(), authkey)
				.queryParam(SmsPropertyConstants.SMS_MESSAGE.getProperty(), "your otp is 4646")
				.queryParam(SmsPropertyConstants.ROUTE.getProperty(), route)
				.queryParam(SmsPropertyConstants.SENDER_ID.getProperty(), senderId)
				.queryParam(SmsPropertyConstants.RECIPIENT_NUMBER.getProperty(), "8987876473")
				.queryParam(SmsPropertyConstants.COUNTRY_CODE.getProperty(), countryCode);

		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

		SmsServerResponseDto serverResponse = new SmsServerResponseDto();
		serverResponse.setType("success");
		SmsResponseDto dto = new SmsResponseDto();
		dto.setStatus(serverResponse.getType());
		dto.setMessage("Sms Request Sent");
		when(restTemplateBuilder.build()).thenReturn(restTemplate);

		when(restTemplate.getForEntity(sms.toUriString(), SmsServerResponseDto.class))
				.thenReturn(new ResponseEntity<>(serverResponse, HttpStatus.OK));

		assertThat(service.sendSmsNotification("8987876473", "your otp is 4646"), is(dto));

	}

	@Test(expected = MosipInvalidNumberException.class)
	public void invalidContactNumberTest() {
		service.sendSmsNotification("jsbchb", "hello your otp is 45373");
	}

	@Test(expected = MosipInvalidNumberException.class)
	public void contactNumberMinimumThresholdTest() {
		service.sendSmsNotification("78978976", "hello your otp is 45373");
	}

	@Test(expected = MosipInvalidNumberException.class)
	public void contactNumberMaximumThresholdTest() {
		service.sendSmsNotification("7897897458673484376", "hello your otp is 45373");
	}

}
