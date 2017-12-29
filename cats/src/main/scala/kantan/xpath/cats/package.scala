/*
 * Copyright 2016 Nicolas Rinaudo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kantan.xpath

import _root_.cats._
import kantan.codecs.cats.CatsInstances

package object cats extends CatsInstances {
  // - Eq instances for errors -----------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val compileErrorEq: Eq[CompileError] = Eq.fromUniversalEquals[CompileError]
  implicit val readErrorEq: Eq[ReadError]       = Eq.fromUniversalEquals[ReadError]
  implicit val decodeErrorEq: Eq[DecodeError]   = Eq.fromUniversalEquals[DecodeError]
  implicit val parseErrorEq: Eq[ParseError]     = Eq.fromUniversalEquals[ParseError]

  // - Misc. instances -------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource: Contravariant[XmlSource] = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B ⇒ A) = fa.contramap(f)
  }
}
