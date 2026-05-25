/*******************************************************************************
 * Copyright (c) 2006, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.internal.ui.actions;

import org.eclipse.debug.ui.actions.RelaunchLastAction;

import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.internal.ui.EclEmmaUIPlugin;
import org.eclipse.eclemma.internal.ui.UIMessages;

/**
 * Action to re-launch the last launch in coverage mode.
 */
public class CoverageLastAction extends RelaunchLastAction {

  @Override
  public String getMode() {
    return CoverageTools.LAUNCH_MODE;
  }

  @Override
  public String getLaunchGroupId() {
    return EclEmmaUIPlugin.ID_COVERAGE_LAUNCH_GROUP;
  }

  @Override
  protected String getText() {
    return UIMessages.CoverageLastAction_label;
  }

  @Override
  protected String getTooltipText() {
    return UIMessages.CoverageLastAction_label;
  }

  @Override
  protected String getDescription() {
    return UIMessages.CoverageLastAction_label;
  }

  @Override
  protected String getCommandId() {
    return "org.eclipse.eclemma.ui.commands.CoverageLast"; //$NON-NLS-1$
  }

}
