/** PRESIDIO CONFIDENTIAL
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
 * Last Modified: Jul 20, 2012 2:50:03 PM
 */

package event_handlers;

import java.io.IOException;


/**
 * 
 * This class dictates what will happen when the 'Wireless' button in the menu
 * is clicked.
 * 
 */
public class WirelessHandler extends GenericEventHandler
{
	/**
	 * Delegates functionality to parent class since no custom functionality is
	 * implemented at this time for Wireless.
	 * @throws IOException 
	 */
	public WirelessHandler() throws IOException
	{
		super(Directories.WIRELESS);
	}
}