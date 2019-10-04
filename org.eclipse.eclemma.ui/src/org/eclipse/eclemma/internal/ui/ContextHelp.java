/*******************************************************************************
 * Copyright (c) 2006, 2019 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.internal.ui;

import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * Constants and utility methods for context help.
 */
public final class ContextHelp {

  private static final String PREFIX = EclEmmaUIPlugin.ID + "."; //$NON-NLS-1$

  public static final String COVERAGE_VIEW = PREFIX + "coverage_view_context"; //$NON-NLS-1$

  public static final String COVERAGE_PROPERTIES = PREFIX
      + "coverage_properties_context"; //$NON-NLS-1$

  public static final String COVERAGE_PREFERENCES = PREFIX
      + "coverage_preferences_context"; //$NON-NLS-1$

  public static final String COVERAGE_LAUNCH = PREFIX
      + "coverage_launch_context"; //$NON-NLS-1$

  public static final String COVERAGE_LAUNCH_TAB = PREFIX
      + "coverage_launch_tab_context"; //$NON-NLS-1$

  public static final String DUMP_EXECUTION_DATA = PREFIX
      + "dump_execution_data"; //$NON-NLS-1$

  public static final String SELECT_ACTIVE_SESSION = PREFIX
      + "select_active_session_context"; //$NON-NLS-1$

  public static final String MERGE_SESSIONS = PREFIX + "merge_sessions_context"; //$NON-NLS-1$

  public static final String SESSION_EXPORT = PREFIX + "session_export_context"; //$NON-NLS-1$

  public static final String SESSION_IMPORT = PREFIX + "session_import_context"; //$NON-NLS-1$

  /**
   * Assigns the given context help id to a SWT control.
   *
   * @param control
   *          control for this help context
   * @param id
   *          context help id
   */
  public static void setHelp(Control control, String id) {
    PlatformUI.getWorkbench().getHelpSystem().setHelp(control, id);
  }

  private ContextHelp() {
    // no instances
  }

}
