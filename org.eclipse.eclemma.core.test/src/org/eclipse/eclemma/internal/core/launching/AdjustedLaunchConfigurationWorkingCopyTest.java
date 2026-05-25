/*******************************************************************************
 * Copyright (c) 2006, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Lukas Pecak - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.internal.core.launching;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.junit.Before;
import org.junit.Test;

public class AdjustedLaunchConfigurationWorkingCopyTest {

  private static final String VM_ARGUMENTS_KEY = IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS;

  private ConfigurationWorkingCopyMock configurationMock;

  private AdjustedLaunchConfigurationWorkingCopy uut;

  @Before
  public void beforeEachTest() {
    configurationMock = new ConfigurationWorkingCopyMock();
    uut = new AdjustedLaunchConfigurationWorkingCopy( //
        "EXTRA", //
        configurationMock.getMock(), //
        configurationMock.getMock());
  }

  @Test
  public void setAttribute_shouldNotModifyTheUnderlyingConfiguration_whenVmArgumentsNotEmpty() {
    // given
    final String originalValue = "ORIGINAL";
    final String extraVMArgument = "EXTRA";

    // when
    uut.setAttribute(VM_ARGUMENTS_KEY, extraVMArgument + " " + originalValue);

    // then
    assertEquals(originalValue, configurationMock.popResult()[1].toString());
  }

  @Test
  public void setAttribute_shouldNotModifyTheUnderlyingConfiguration_whenVmArgumentsEmpty() {
    // given
    final String originalValue = "";
    final String extraVMArgument = "EXTRA";

    // when
    uut.setAttribute(VM_ARGUMENTS_KEY, extraVMArgument + " " + originalValue);

    // then
    assertEquals(originalValue, configurationMock.popResult()[1].toString());
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void setAttributes_shouldNotModifyTheUnderlyingConfiguration_whenVmArgumentsNotEmpty() {
    // given
    final String originalValue = "ORIGINAL";
    final String extraVMArgument = "EXTRA";
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(VM_ARGUMENTS_KEY, extraVMArgument + " " + originalValue);
    attributes.put("OTHER", "OTHER_VALUE");

    // when
    uut.setAttributes(attributes);

    // then
    Map result = (Map) configurationMock.popResult()[0];
    assertEquals(result.get(VM_ARGUMENTS_KEY), originalValue);
    assertEquals(result.get("OTHER"), "OTHER_VALUE");
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void setAttributes_shouldNotModifyTheUnderlyingConfiguration_whenVmArgumentsEmpty() {
    // given
    final String originalValue = "";
    final String extraVMArgument = "EXTRA";
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(VM_ARGUMENTS_KEY, extraVMArgument + " " + originalValue);
    attributes.put("OTHER", "OTHER_VALUE");

    // when
    uut.setAttributes(attributes);

    // then
    Map result = (Map) configurationMock.popResult()[0];
    assertEquals(result.get(VM_ARGUMENTS_KEY), originalValue);
    assertEquals(result.get("OTHER"), "OTHER_VALUE");
  }

  @Test
  public void getSetAttribute_shouldBeSymmetric_always() throws CoreException {
    // given
    final String initialValue = "EXTRA ORIGINAL";
    uut.setAttribute(VM_ARGUMENTS_KEY, initialValue);

    // when
    final String intermediateValue = uut.getAttribute(VM_ARGUMENTS_KEY, "");
    uut.setAttribute(VM_ARGUMENTS_KEY, intermediateValue);
    final String result = uut.getAttribute(VM_ARGUMENTS_KEY, "");

    // then
    assertEquals(result, initialValue);
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void getSetAttributes_shouldBeSymmetric_always() throws CoreException {
    // given
    final Map<String, String> initialValue = new HashMap<String, String>();
    initialValue.put(VM_ARGUMENTS_KEY, "EXTRA ORIGINAL");
    initialValue.put("OTHER", "OTHER_VALUE");
    // push 2 results on the stack - used by the delegates getAttribute method
    configurationMock.pushResult(new Object[] { VM_ARGUMENTS_KEY, "ORIGINAL" });
    configurationMock.pushResult(new Object[] { VM_ARGUMENTS_KEY, "ORIGINAL" });
    uut.setAttributes(initialValue);

    // when
    final Map intermediateValue = uut.getAttributes();
    uut.setAttributes(intermediateValue);
    final Map result = uut.getAttributes();

    // then
    assertEquals(result, initialValue);
  }

}
