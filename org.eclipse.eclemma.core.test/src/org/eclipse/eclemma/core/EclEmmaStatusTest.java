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
package org.eclipse.eclemma.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.eclipse.core.runtime.IStatus;
import org.junit.Test;

/**
 * Tests for {@link EclEmmaStatus}.
 */
public class EclEmmaStatusTest {

  @Test
  public void testCode1() {
    EclEmmaStatus estatus = EclEmmaStatus.NO_LOCAL_AGENTJAR_ERROR;
    IStatus status = estatus.getStatus();
    assertEquals(estatus.code, status.getCode());
  }

  @Test
  public void testSeverity1() {
    EclEmmaStatus estatus = EclEmmaStatus.NO_LOCAL_AGENTJAR_ERROR;
    IStatus status = estatus.getStatus();
    assertEquals(estatus.severity, status.getSeverity());
  }

  @Test
  public void testMessage1() {
    EclEmmaStatus estatus = EclEmmaStatus.NO_LOCAL_AGENTJAR_ERROR;
    IStatus status = estatus.getStatus();
    assertEquals("Local agent jar can not be obtained (code 5000).",
        status.getMessage());
  }

  @Test
  public void testMessage2() {
    EclEmmaStatus estatus = EclEmmaStatus.UNKOWN_LAUNCH_TYPE_ERROR;
    IStatus status = estatus.getStatus("abcdef");
    assertEquals("Unknown launch type abcdef (code 5002).", status.getMessage());
  }

  @Test
  public void testThrowable1() {
    EclEmmaStatus estatus = EclEmmaStatus.NO_LOCAL_AGENTJAR_ERROR;
    Throwable t = new Exception();
    IStatus status = estatus.getStatus(t);
    assertSame(t, status.getException());
  }

}
