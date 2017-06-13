/*******************************************************************************
 * Copyright (c) 2006, 2017 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package methodlocator;

import java.util.Date;

/**
 * Collections of methods with different Signatures.
 */
public class Samples {

  /* <init>()V */
  Samples() {
  }

  /* <init>(Ljava/lang/String;)V */
  Samples(String param) {
  }

  /* <init>(I)V */
  Samples(int param) {
  }

  /* (Ljava/lang/String;)V */
  void m1(String s) {
  }

  /* (Ljava/lang/Integer;)V */
  void m2(Integer i) {
  }

  /* (Ljava/lang/Number;)V */
  void m2(Number n) {
  }

}
