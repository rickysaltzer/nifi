/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.cluster.protocol.jaxb.message;

import javax.xml.bind.annotation.XmlRegistry;

import org.apache.nifi.cluster.protocol.message.ConnectionRequestMessage;
import org.apache.nifi.cluster.protocol.message.ConnectionResponseMessage;
import org.apache.nifi.cluster.protocol.message.ControllerStartupFailureMessage;
import org.apache.nifi.cluster.protocol.message.DisconnectMessage;
import org.apache.nifi.cluster.protocol.message.FlowRequestMessage;
import org.apache.nifi.cluster.protocol.message.FlowResponseMessage;
import org.apache.nifi.cluster.protocol.message.HeartbeatMessage;
import org.apache.nifi.cluster.protocol.message.MulticastProtocolMessage;
import org.apache.nifi.cluster.protocol.message.NodeBulletinsMessage;
import org.apache.nifi.cluster.protocol.message.PingMessage;
import org.apache.nifi.cluster.protocol.message.PrimaryRoleAssignmentMessage;
import org.apache.nifi.cluster.protocol.message.ReconnectionFailureMessage;
import org.apache.nifi.cluster.protocol.message.ReconnectionRequestMessage;
import org.apache.nifi.cluster.protocol.message.ReconnectionResponseMessage;
import org.apache.nifi.cluster.protocol.message.ServiceBroadcastMessage;

/**
 */
@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public ReconnectionRequestMessage createReconnectionRequestMessage() {
        return new ReconnectionRequestMessage();
    }

    public ReconnectionFailureMessage createReconnectionFailureMessage() {
        return new ReconnectionFailureMessage();
    }

    public ReconnectionResponseMessage createReconnectionResponseMessage() {
        return new ReconnectionResponseMessage();
    }

    public DisconnectMessage createDisconnectionMessage() {
        return new DisconnectMessage();
    }

    public ConnectionRequestMessage createConnectionRequestMessage() {
        return new ConnectionRequestMessage();
    }

    public ConnectionResponseMessage createConnectionResponseMessage() {
        return new ConnectionResponseMessage();
    }

    public ServiceBroadcastMessage createServiceBroadcastMessage() {
        return new ServiceBroadcastMessage();
    }

    public HeartbeatMessage createHeartbeatMessage() {
        return new HeartbeatMessage();
    }

    public FlowRequestMessage createFlowRequestMessage() {
        return new FlowRequestMessage();
    }

    public FlowResponseMessage createFlowResponseMessage() {
        return new FlowResponseMessage();
    }

    public PingMessage createPingMessage() {
        return new PingMessage();
    }

    public MulticastProtocolMessage createMulticastProtocolMessage() {
        return new MulticastProtocolMessage();
    }

    public ControllerStartupFailureMessage createControllerStartupFailureMessage() {
        return new ControllerStartupFailureMessage();
    }

    public PrimaryRoleAssignmentMessage createPrimaryRoleAssignmentMessage() {
        return new PrimaryRoleAssignmentMessage();
    }

    public NodeBulletinsMessage createBulletinsMessage() {
        return new NodeBulletinsMessage();
    }
}
