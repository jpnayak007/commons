/*
 * 
 * 
 * 
 */
package org.mosip.kernel.logger.appenders;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.mosip.kernel.logger.constants.MosipConfigurationDefaults;

/**RollingFile appender for Mosip 
 * 
 * @author Urvil Joshi
 * @since 1.0.0
 */
@XmlRootElement
public class MosipRollingFileAppender extends MosipFileAppender {

	/**
	 * RollingFileAppender for mosip The name of the rolled-over (archived) log
	 * files.Its value should consist of the name of the file,<b> plus a suitably
	 * placed %d</b> conversion specifier. The %d conversion specifier may contain a
	 * date-and-time pattern as specified by the java.text.SimpleDateFormat
	 * class;<b>Mandatory field to pass</b>
	 */
	private String fileNamePattern;
	/**
	 * Controls the maximum number of archive files to keep, asynchronously deleting
	 * older files;default this restriction will not apply
	 */
	private int maxHistory = MosipConfigurationDefaults.DEFAULMAXFILEHISTORY;
	/**
	 * Controls the total size of all archive files. Oldest archives are deleted
	 * asynchronously when the total size cap is exceeded;default this restriction
	 * will not apply
	 */
	private String totalCap = MosipConfigurationDefaults.DEFAULTTOTALCAP;
	/**
	 * Limit the size of each log file;default this restriction will not apply
	 */
	private String maxFileSize = MosipConfigurationDefaults.DEFAULTFILESIZE;

	/**
	 * Getter for fileNamePattern
	 * 
	 * @return current value of fileNamepattern
	 */
	public String getFileNamePattern() {
		return fileNamePattern;
	}

	/**
	 * Setter for fileNamepattern
	 * 
	 * @param fileNamePattern
	 *            The name of the rolled-over (archived) log files.Its value should
	 *            consist of the name of the file,<b> plus a suitably placed %d</b>
	 *            conversion specifier. The %d conversion specifier may contain a
	 *            date-and-time pattern as specified by the
	 *            java.text.SimpleDateFormat class;<b>Mandatory field to pass</b>
	 */
	@XmlElement
	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	/**
	 * Getter for maxHistory
	 * 
	 * @return current value of maxHistory
	 */
	public int getMaxHistory() {
		return maxHistory;
	}

	/**
	 * Setter for maxHistory
	 * 
	 * @param maxHistory
	 *            Controls the maximum number of archive files to keep,
	 *            asynchronously deleting older files;default this restriction will
	 *            not apply
	 */
	@XmlElement
	public void setMaxHistory(int maxHistory) {
		this.maxHistory = maxHistory;
	}

	/**
	 * Getter for totalCap
	 * 
	 * @return current value of totalCap
	 */
	public String getTotalCap() {
		return totalCap;
	}

	/**
	 * Setter for totalCap
	 * 
	 * @param totalCap
	 *            Controls the total size of all archive files. Oldest archives are
	 *            deleted asynchronously when the total size cap is exceeded;default
	 *            this restriction will not apply
	 */
	@XmlElement
	public void setTotalCap(String totalCap) {
		this.totalCap = totalCap;
	}

	/**
	 * Getter for maxFileSize
	 * 
	 * @return current value of maxFileSize
	 */
	public String getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * Setter for maxFileSize
	 * 
	 * @param maxFileSize
	 *            Limit the size of each log file;default this restriction will not
	 *            apply
	 */
	@XmlElement
	public void setMaxFileSize(String maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

}
