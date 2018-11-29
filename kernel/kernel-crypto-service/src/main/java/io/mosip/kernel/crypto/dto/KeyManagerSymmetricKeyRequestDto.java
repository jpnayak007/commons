/*
 * 
 * 
 * 
 * 
 */
package io.mosip.kernel.crypto.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Urvil Joshi
 *
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyManagerSymmetricKeyRequestDto {
/**
 * 
 */
private String applicationId;
/**
 * 
 */
private String referenceId;
/**
 * 
 */
private LocalDateTime timeStamp;
/**
 * 
 */
private byte[] encryptedSymmetricKey;
}
