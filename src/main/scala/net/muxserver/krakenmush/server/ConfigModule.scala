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

package net.muxserver.krakenmush.server

import com.google.inject.{AbstractModule, Provider}
import com.typesafe.config.ConfigFactory
import kadai.config.Configuration
import net.codingwell.scalaguice.ScalaModule
import net.muxserver.krakenmush.server.ConfigModule.ConfigProvider

/**
 * @since 9/5/15
 */
class ConfigModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Configuration].toProvider[ConfigProvider].asEagerSingleton()
  }
}

object ConfigModule {

  class ConfigProvider extends Provider[Configuration] {
    override def get() = Configuration(ConfigFactory.load())
  }

}

