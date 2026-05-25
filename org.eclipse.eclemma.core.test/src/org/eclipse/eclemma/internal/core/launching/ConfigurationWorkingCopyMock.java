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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Stack;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

/**
 * Mock support for {@link ILaunchConfigurationWorkingCopy}.
 */
class ConfigurationWorkingCopyMock implements InvocationHandler {

  private ILaunchConfigurationWorkingCopy mock;

  private Stack<Object> mockResult;

  ConfigurationWorkingCopyMock() {
    mockResult = new Stack<Object>();
    mock = (ILaunchConfigurationWorkingCopy) Proxy.newProxyInstance(
        getClass().getClassLoader(),
        new Class<?>[] { ILaunchConfigurationWorkingCopy.class }, this);
  }

  ILaunchConfigurationWorkingCopy getMock() {
    return mock;
  }

  Object[] popResult() {
    return (Object[]) mockResult.pop();
  }

  void pushResult(Object value) {
    mockResult.push(value);
  }

  // InvocationHandler implementation

  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {

    if (method.getName().contains("getAttribute")) {
      Object[] returnValue = (Object[]) mockResult.pop();
      // get second parameter for getAttrigute method - type String
      // get first parameter for getAttributes method - type Map
      return returnValue.length == 2 ? returnValue[1] : returnValue[0];
    }
    return mockResult.push(args);
  }

}
