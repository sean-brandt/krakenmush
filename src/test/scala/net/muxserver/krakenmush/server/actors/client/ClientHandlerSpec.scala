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

import akka.actor.{Actor, ActorRef}
import akka.cluster.pubsub.DistributedPubSubMediator.Send
import akka.io.Tcp.{PeerClosed, Received}
import akka.testkit._
import akka.util.ByteString
import net.muxserver.krakenmush.server.actors.BaseActorSpec
import net.muxserver.krakenmush.server.actors.commands.CommandExecutorProtocol.ExecuteRawCommand
import net.muxserver.krakenmush.server.{ClusterComms, CoreClusterAddresses}

/**
 * @since 9/1/15
 */
class ClientHandlerSpec extends BaseActorSpec {

  trait TestClusterComms extends ClusterComms {
    self: Actor =>
    lazy     val mediatorProbe      = TestProbe("clusterMediator")
    override val mediator: ActorRef = mediatorProbe.ref
  }

  var clientConnectionProbe: TestProbe                                         = _
  var clientHandler        : TestActorRef[ClientHandler with TestClusterComms] = _
  var commandExecutorProbe : TestProbe                                         = _
  var mediatorProbe        : TestProbe                                         = _

  "A ClientHandler " must {
    "stop when connection terminated" in {
      EventFilter.info(message = "Stopping due to terminated connection.", occurrences = 1) intercept {
        system.stop(clientConnectionProbe.ref)
      }
    }

    "stop when client closes the connection" in {
      EventFilter.info(start = "Client closed, shutting down:", occurrences = 1) intercept {
        clientHandler ! PeerClosed
      }
    }

    "does nothing when a no-op entry is sent" in {
      EventFilter.debug(message = "Client sent no data or all whitespace: no-op or keep alive.", occurrences = 1) intercept {
        clientHandler ! Received(ByteString(""))
      }
    }

    "executes command when received" in {
      clientHandler ! Received(ByteString("connect Foo bar"))
      mediatorProbe.expectMsgPF() {
        case Send(address, ExecuteRawCommand(_, msg), localAffinity) =>
          CoreClusterAddresses.COMMAND_EXECUTOR == address && !localAffinity && msg == "connect Foo bar"
      }
    }

    "sends command execution result to connection when received" in {

    }
  }

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    clientConnectionProbe = TestProbe()
    commandExecutorProbe = TestProbe("commandExecutor")
    clientHandler = TestActorRef(new
        ClientHandler(new InetSocketAddress("127.1.1.1", 63333), clientConnectionProbe.ref) with TestClusterComms)
    mediatorProbe = clientHandler.underlyingActor.mediatorProbe

  }
}
