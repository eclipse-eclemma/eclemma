/*******************************************************************************
 * Copyright (c) 2006, 2017 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.ui;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.After;
import org.junit.Test;

public class MenuTest {

	private static final SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@After
	public void resetWorkbench() {
		bot.resetWorkbench();
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=517712
	 */
	@Test
	public void labels_should_be_consistent() {
		bot.perspectiveByLabel("Java").activate();
		final List<String> items = bot.menu("Run").menuItems();

		assertTrue(items.contains("Run"));
		assertTrue(items.contains("Coverage"));

		assertTrue(items.contains("Run History"));
		assertTrue(items.contains("Coverage History"));

		assertTrue(items.contains("Run Configurations..."));
		assertTrue(items.contains("Coverage Configurations..."));
	}

}
