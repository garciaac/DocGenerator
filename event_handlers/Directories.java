/* PRESIDIO CONFIDENTIAL
 * __________________
 * 
 * Copyright (c) [2012] Presidio Networked Solutions 
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Presidio Networked Solutions. The intellectual and 
 * technical concepts contained herein are proprietary to Presidio Networked 
 * Solutions and may be covered by U.S. and Foreign Patents, patents in process, 
 * and are protected by trade secret or copyright law. Dissemination of this 
 * information or reproduction of this material is strictly forbidden unless 
 * prior written permission is obtained from Presidio Networked Solutions.
 * 		
 * Author: 	Andrew Garcia
 * Email:	agarcia@presido.com
 * Last Modified: Jul 20, 2012 2:44:38 PM
 */

package event_handlers;

/**
 * 
 * This class defines constants that define the directory structure where the
 * questionaires are located for each practice.
 * 
 */
public class Directories
{
	// Variables are package private, so they are only visible to the
	// event_handlers package. The directory paths are relative to the
	// current working directory, so in order for the application to work,
	// these folders must be in the same directory as the JAR file.
	final static String DATA_CENTER = "./Data Center/";
	final static String NETWORK_INFRASTRUCTURE = "./Network Infrastructure/";
	final static String WIRELESS = "./Wireless/";
	final static String UNIFIED_COMMUNICATIONS = "./Unified Communications/";
	final static String SECURITY = "./Security/";
}
