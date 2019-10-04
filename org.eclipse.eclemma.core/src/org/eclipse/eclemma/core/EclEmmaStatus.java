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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;

import org.eclipse.eclemma.internal.core.CoreMessages;
import org.eclipse.eclemma.internal.core.EclEmmaCorePlugin;

/**
 * Status objects used by the core plug-in.
 */
public final class EclEmmaStatus {

  final int code;

  final int severity;

  final String message;

  private EclEmmaStatus(int code, int severity, String message) {
    this.code = code;
    this.severity = severity;
    this.message = message;
  }

  public IStatus getStatus() {
    String m = NLS.bind(message, Integer.valueOf(code));
    return new Status(severity, EclEmmaCorePlugin.ID, code, m, null);
  }

  public IStatus getStatus(Throwable t) {
    String m = NLS.bind(message, Integer.valueOf(code));
    return new Status(severity, EclEmmaCorePlugin.ID, code, m, t);
  }

  public IStatus getStatus(Object param1, Throwable t) {
    String m = NLS.bind(message, Integer.valueOf(code), param1);
    return new Status(severity, EclEmmaCorePlugin.ID, code, m, t);
  }

  public IStatus getStatus(Object param1, Object param2, Throwable t) {
    String m = NLS.bind(message, new Object[] { Integer.valueOf(code), param1,
        param2 });
    return new Status(severity, EclEmmaCorePlugin.ID, code, m, t);
  }

  public IStatus getStatus(Object param1) {
    String m = NLS.bind(message, Integer.valueOf(code), param1);
    return new Status(severity, EclEmmaCorePlugin.ID, code, m, null);
  }

  /**
   * Status indicating that it was not possible to obtain a local version of the
   * runtime agent file.
   */
  public static final EclEmmaStatus NO_LOCAL_AGENTJAR_ERROR = new EclEmmaStatus(
      5000, IStatus.ERROR, CoreMessages.StatusNO_LOCAL_AGENTJAR_ERROR_message);

  /**
   * Error while loading a coverage session.
   */
  public static final EclEmmaStatus SESSION_LOAD_ERROR = new EclEmmaStatus(
      5001, IStatus.ERROR, CoreMessages.StatusSESSION_LOAD_ERROR_message);

  /**
   * The requested launch type is not known.
   */
  public static final EclEmmaStatus UNKOWN_LAUNCH_TYPE_ERROR = new EclEmmaStatus(
      5002, IStatus.ERROR, CoreMessages.StatusUNKOWN_LAUNCH_TYPE_ERROR_message);

  /**
   * Error while merging sessions.
   */
  public static final EclEmmaStatus MERGE_SESSIONS_ERROR = new EclEmmaStatus(
      5003, IStatus.ERROR, CoreMessages.StatusMERGE_SESSIONS_ERROR_message);

  /**
   * The execution data file can not be created.
   */
  public static final EclEmmaStatus EXEC_FILE_CREATE_ERROR = new EclEmmaStatus(
      5004, IStatus.ERROR, CoreMessages.StatusEXEC_FILE_CREATE_ERROR_message);

  /**
   * Error while reading coverage data file.
   */
  public static final EclEmmaStatus EXEC_FILE_READ_ERROR = new EclEmmaStatus(
      5005, IStatus.ERROR, CoreMessages.StatusEXEC_FILE_READ_ERROR_message);

  /**
   * Error while reading coverage data file.
   */
  public static final EclEmmaStatus AGENT_CONNECT_ERROR = new EclEmmaStatus(
      5006, IStatus.ERROR, CoreMessages.StatusAGENT_CONNECT_ERROR_message);

  /**
   * Error while analyzing a bundle of class file.
   */
  public static final EclEmmaStatus BUNDLE_ANALYSIS_ERROR = new EclEmmaStatus(
      5007, IStatus.ERROR, CoreMessages.StatusBUNDLE_ANALYSIS_ERROR_message);

  /**
   * Error while extracting coverage session.
   */
  public static final EclEmmaStatus EXPORT_ERROR = new EclEmmaStatus(5008,
      IStatus.ERROR, CoreMessages.StatusEXPORT_ERROR_message);

  /**
   * Error while starting the agent server.
   */
  public static final EclEmmaStatus AGENTSERVER_START_ERROR = new EclEmmaStatus(
      5011, IStatus.ERROR, CoreMessages.StatusAGENTSERVER_START_ERROR_message);

  /**
   * Error while stopping the agent server.
   */
  public static final EclEmmaStatus AGENTSERVER_STOP_ERROR = new EclEmmaStatus(
      5012, IStatus.ERROR, CoreMessages.StatusAGENTSERVER_STOP_ERROR_message);

  /**
   * Error while dumping coverage data.
   */
  public static final EclEmmaStatus EXECDATA_DUMP_ERROR = new EclEmmaStatus(
      5013, IStatus.ERROR, CoreMessages.StatusEXECDATA_DUMP_ERROR_message);

  /**
   * Error while requesting an execution data dump.
   */
  public static final EclEmmaStatus DUMP_REQUEST_ERROR = new EclEmmaStatus(
      5014, IStatus.ERROR, CoreMessages.StatusDUMP_REQUEST_ERROR_message);

  /**
   * No coverage data file has been created during a coverage launch. This
   * status is used to issue an error prompt.
   */
  public static final EclEmmaStatus NO_COVERAGE_DATA_ERROR = new EclEmmaStatus(
      5101, IStatus.ERROR, CoreMessages.StatusNO_COVERAGE_DATA_ERROR_message);

}
