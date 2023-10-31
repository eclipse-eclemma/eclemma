/*******************************************************************************
 * Copyright (c) 2006, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.ui;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Version;

final class UiTestUtils {

  /**
   * https://github.com/eclipse-platform/eclipse.platform.ui/commit/40b5475e2790b36228537d6446e470b36386b17c
   * https://github.com/eclipse-platform/eclipse.platform.ui/commit/a43240d58eebb2015c48a4957859bb944a432a6e
   */
  public static final boolean TITLE_AREA_DIALOG_USES_LABEL;

  static {
    final Version jfaceVersion = Platform.getBundle("org.eclipse.jface")
        .getVersion();
    TITLE_AREA_DIALOG_USES_LABEL = jfaceVersion.getMajor() == 3
        && jfaceVersion.getMinor() == 22;
  }

  private UiTestUtils() {
  }

}
