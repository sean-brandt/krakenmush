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

package net.muxserver.krakenmush.server.actors.client

import java.net.InetSocketAddress

import akka.actor.{ActorContext, ActorRef}

/**
 * @since 9/1/15
 */
trait ClientHandlerProducer {
  def newClientHandler(
    remoteAddress: InetSocketAddress,
    connection: ActorRef,
    actorName: Option[String] = None
  )(implicit context: ActorContext): ActorRef = {
    context
      .actorOf(ClientHandler.props(remoteAddress, connection), actorName
        .getOrElse(s"ClientHandler-${remoteAddress.getHostName}:${remoteAddress.getPort}"))
  }
}
