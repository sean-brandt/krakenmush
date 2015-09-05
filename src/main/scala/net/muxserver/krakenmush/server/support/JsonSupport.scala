/*
 * Copyright 2015 Sean Brandt
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

package net.muxserver.krakenmush.server.support

import org.json4s.DefaultFormats

/**
 * @since 9/4/15
 */
object JsonSupport {
  val formats = DefaultFormats

}

trait JsonToString {
  implicit val formats = JsonSupport.formats

  override def toString: String = {
    import org.json4s.native.Serialization.write
    write(this)
  }
}
