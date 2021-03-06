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
package org.apache.nifi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NiFiProperties extends Properties {

    private static final long serialVersionUID = 2119177359005492702L;

    private static final Logger LOG = LoggerFactory.getLogger(NiFiProperties.class);
    private static NiFiProperties instance = null;

    // core properties
    public static final String PROPERTIES_FILE_PATH = "nifi.properties.file.path";
    public static final String FLOW_CONFIGURATION_FILE = "nifi.flow.configuration.file";
    public static final String FLOW_CONFIGURATION_ARCHIVE_FILE = "nifi.flow.configuration.archive.file";
    public static final String AUTHORITY_PROVIDER_CONFIGURATION_FILE = "nifi.authority.provider.configuration.file";
    public static final String REPOSITORY_DATABASE_DIRECTORY = "nifi.database.directory";
    public static final String RESTORE_DIRECTORY = "nifi.restore.directory";
    public static final String VERSION = "nifi.version";
    public static final String WRITE_DELAY_INTERVAL = "nifi.flowservice.writedelay.interval";
    public static final String AUTO_RESUME_STATE = "nifi.flowcontroller.autoResumeState";
    public static final String FLOW_CONTROLLER_GRACEFUL_SHUTDOWN_PERIOD = "nifi.flowcontroller.graceful.shutdown.period";
    public static final String NAR_LIBRARY_DIRECTORY = "nifi.nar.library.directory";
    public static final String NAR_LIBRARY_DIRECTORY_PREFIX = "nifi.nar.library.directory.";
    public static final String NAR_WORKING_DIRECTORY = "nifi.nar.working.directory";
    public static final String COMPONENT_DOCS_DIRECTORY = "nifi.documentation.working.directory";
    public static final String SENSITIVE_PROPS_KEY = "nifi.sensitive.props.key";
    public static final String SENSITIVE_PROPS_ALGORITHM = "nifi.sensitive.props.algorithm";
    public static final String SENSITIVE_PROPS_PROVIDER = "nifi.sensitive.props.provider";
    public static final String H2_URL_APPEND = "nifi.h2.url.append";
    public static final String REMOTE_INPUT_PORT = "nifi.remote.input.socket.port";
    public static final String SITE_TO_SITE_SECURE = "nifi.remote.input.secure";
    public static final String TEMPLATE_DIRECTORY = "nifi.templates.directory";
    public static final String ADMINISTRATIVE_YIELD_DURATION = "nifi.administrative.yield.duration";
    public static final String PERSISTENT_STATE_DIRECTORY = "nifi.persistent.state.directory";
    public static final String BORED_YIELD_DURATION = "nifi.bored.yield.duration";

    // content repository properties
    public static final String REPOSITORY_CONTENT_PREFIX = "nifi.content.repository.directory.";
    public static final String CONTENT_REPOSITORY_IMPLEMENTATION = "nifi.content.repository.implementation";
    public static final String MAX_APPENDABLE_CLAIM_SIZE = "nifi.content.claim.max.appendable.size";
    public static final String MAX_FLOWFILES_PER_CLAIM = "nifi.content.claim.max.flow.files";
    public static final String CONTENT_ARCHIVE_MAX_RETENTION_PERIOD = "nifi.content.repository.archive.max.retention.period";
    public static final String CONTENT_ARCHIVE_MAX_USAGE_PERCENTAGE = "nifi.content.repository.archive.max.usage.percentage";
    public static final String CONTENT_ARCHIVE_BACK_PRESSURE_PERCENTAGE = "nifi.content.repository.archive.backpressure.percentage";
    public static final String CONTENT_ARCHIVE_ENABLED = "nifi.content.repository.archive.enabled";
    public static final String CONTENT_ARCHIVE_CLEANUP_FREQUENCY = "nifi.content.repository.archive.cleanup.frequency";
    public static final String CONTENT_VIEWER_URL = "nifi.content.viewer.url";

    // flowfile repository properties
    public static final String FLOWFILE_REPOSITORY_IMPLEMENTATION = "nifi.flowfile.repository.implementation";
    public static final String FLOWFILE_REPOSITORY_ALWAYS_SYNC = "nifi.flowfile.repository.always.sync";
    public static final String FLOWFILE_REPOSITORY_DIRECTORY = "nifi.flowfile.repository.directory";
    public static final String FLOWFILE_REPOSITORY_PARTITIONS = "nifi.flowfile.repository.partitions";
    public static final String FLOWFILE_REPOSITORY_CHECKPOINT_INTERVAL = "nifi.flowfile.repository.checkpoint.interval";
    public static final String FLOWFILE_SWAP_MANAGER_IMPLEMENTATION = "nifi.swap.manager.implementation";
    public static final String QUEUE_SWAP_THRESHOLD = "nifi.queue.swap.threshold";
    public static final String SWAP_IN_THREADS = "nifi.swap.in.threads";
    public static final String SWAP_IN_PERIOD = "nifi.swap.in.period";
    public static final String SWAP_OUT_THREADS = "nifi.swap.out.threads";
    public static final String SWAP_OUT_PERIOD = "nifi.swap.out.period";

    // provenance properties
    public static final String PROVENANCE_REPO_IMPLEMENTATION_CLASS = "nifi.provenance.repository.implementation";
    public static final String PROVENANCE_REPO_DIRECTORY_PREFIX = "nifi.provenance.repository.directory.";
    public static final String PROVENANCE_MAX_STORAGE_TIME = "nifi.provenance.repository.max.storage.time";
    public static final String PROVENANCE_MAX_STORAGE_SIZE = "nifi.provenance.repository.max.storage.size";
    public static final String PROVENANCE_ROLLOVER_TIME = "nifi.provenance.repository.rollover.time";
    public static final String PROVENANCE_ROLLOVER_SIZE = "nifi.provenance.repository.rollover.size";
    public static final String PROVENANCE_QUERY_THREAD_POOL_SIZE = "nifi.provenance.repository.query.threads";
    public static final String PROVENANCE_COMPRESS_ON_ROLLOVER = "nifi.provenance.repository.compress.on.rollover";
    public static final String PROVENANCE_INDEXED_FIELDS = "nifi.provenance.repository.indexed.fields";
    public static final String PROVENANCE_INDEXED_ATTRIBUTES = "nifi.provenance.repository.indexed.attributes";
    public static final String PROVENANCE_INDEX_SHARD_SIZE = "nifi.provenance.repository.index.shard.size";
    public static final String PROVENANCE_JOURNAL_COUNT = "nifi.provenance.repository.journal.count";

    // component status repository properties
    public static final String COMPONENT_STATUS_REPOSITORY_IMPLEMENTATION = "nifi.components.status.repository.implementation";
    public static final String COMPONENT_STATUS_SNAPSHOT_FREQUENCY = "nifi.components.status.snapshot.frequency";

    // encryptor properties
    public static final String NF_SENSITIVE_PROPS_KEY = "nifi.sensitive.props.key";
    public static final String NF_SENSITIVE_PROPS_ALGORITHM = "nifi.sensitive.props.algorithm";
    public static final String NF_SENSITIVE_PROPS_PROVIDER = "nifi.sensitive.props.provider";

    // security properties
    public static final String SECURITY_KEYSTORE = "nifi.security.keystore";
    public static final String SECURITY_KEYSTORE_TYPE = "nifi.security.keystoreType";
    public static final String SECURITY_KEYSTORE_PASSWD = "nifi.security.keystorePasswd";
    public static final String SECURITY_KEY_PASSWD = "nifi.security.keyPasswd";
    public static final String SECURITY_TRUSTSTORE = "nifi.security.truststore";
    public static final String SECURITY_TRUSTSTORE_TYPE = "nifi.security.truststoreType";
    public static final String SECURITY_TRUSTSTORE_PASSWD = "nifi.security.truststorePasswd";
    public static final String SECURITY_NEED_CLIENT_AUTH = "nifi.security.needClientAuth";
    public static final String SECURITY_USER_AUTHORITY_PROVIDER = "nifi.security.user.authority.provider";
    public static final String SECURITY_CLUSTER_AUTHORITY_PROVIDER_PORT = "nifi.security.cluster.authority.provider.port";
    public static final String SECURITY_CLUSTER_AUTHORITY_PROVIDER_THREADS = "nifi.security.cluster.authority.provider.threads";
    public static final String SECURITY_USER_CREDENTIAL_CACHE_DURATION = "nifi.security.user.credential.cache.duration";
    public static final String SECURITY_SUPPORT_NEW_ACCOUNT_REQUESTS = "nifi.security.support.new.account.requests";
    public static final String SECURITY_DEFAULT_USER_ROLES = "nifi.security.default.user.roles";
    public static final String SECURITY_OCSP_RESPONDER_URL = "nifi.security.ocsp.responder.url";
    public static final String SECURITY_OCSP_RESPONDER_CERTIFICATE = "nifi.security.ocsp.responder.certificate";

    // web properties
    public static final String WEB_WAR_DIR = "nifi.web.war.directory";
    public static final String WEB_HTTP_PORT = "nifi.web.http.port";
    public static final String WEB_HTTP_HOST = "nifi.web.http.host";
    public static final String WEB_HTTPS_PORT = "nifi.web.https.port";
    public static final String WEB_HTTPS_HOST = "nifi.web.https.host";
    public static final String WEB_WORKING_DIR = "nifi.web.jetty.working.directory";
    public static final String WEB_THREADS = "nifi.web.jetty.threads";

    // ui properties
    public static final String UI_BANNER_TEXT = "nifi.ui.banner.text";
    public static final String UI_AUTO_REFRESH_INTERVAL = "nifi.ui.autorefresh.interval";

    // cluster common properties
    public static final String CLUSTER_PROTOCOL_HEARTBEAT_INTERVAL = "nifi.cluster.protocol.heartbeat.interval";
    public static final String CLUSTER_PROTOCOL_IS_SECURE = "nifi.cluster.protocol.is.secure";
    public static final String CLUSTER_PROTOCOL_SOCKET_TIMEOUT = "nifi.cluster.protocol.socket.timeout";
    public static final String CLUSTER_PROTOCOL_CONNECTION_HANDSHAKE_TIMEOUT = "nifi.cluster.protocol.connection.handshake.timeout";
    public static final String CLUSTER_PROTOCOL_USE_MULTICAST = "nifi.cluster.protocol.use.multicast";
    public static final String CLUSTER_PROTOCOL_MULTICAST_ADDRESS = "nifi.cluster.protocol.multicast.address";
    public static final String CLUSTER_PROTOCOL_MULTICAST_PORT = "nifi.cluster.protocol.multicast.port";
    public static final String CLUSTER_PROTOCOL_MULTICAST_SERVICE_BROADCAST_DELAY = "nifi.cluster.protocol.multicast.service.broadcast.delay";
    public static final String CLUSTER_PROTOCOL_MULTICAST_SERVICE_LOCATOR_ATTEMPTS = "nifi.cluster.protocol.multicast.service.locator.attempts";
    public static final String CLUSTER_PROTOCOL_MULTICAST_SERVICE_LOCATOR_ATTEMPTS_DELAY = "nifi.cluster.protocol.multicast.service.locator.attempts.delay";

    // cluster node properties
    public static final String CLUSTER_IS_NODE = "nifi.cluster.is.node";
    public static final String CLUSTER_NODE_ADDRESS = "nifi.cluster.node.address";
    public static final String CLUSTER_NODE_PROTOCOL_PORT = "nifi.cluster.node.protocol.port";
    public static final String CLUSTER_NODE_PROTOCOL_THREADS = "nifi.cluster.node.protocol.threads";
    public static final String CLUSTER_NODE_UNICAST_MANAGER_ADDRESS = "nifi.cluster.node.unicast.manager.address";
    public static final String CLUSTER_NODE_UNICAST_MANAGER_PROTOCOL_PORT = "nifi.cluster.node.unicast.manager.protocol.port";

    // cluster manager properties
    public static final String CLUSTER_IS_MANAGER = "nifi.cluster.is.manager";
    public static final String CLUSTER_MANAGER_ADDRESS = "nifi.cluster.manager.address";
    public static final String CLUSTER_MANAGER_PROTOCOL_PORT = "nifi.cluster.manager.protocol.port";
    public static final String CLUSTER_MANAGER_NODE_FIREWALL_FILE = "nifi.cluster.manager.node.firewall.file";
    public static final String CLUSTER_MANAGER_NODE_EVENT_HISTORY_SIZE = "nifi.cluster.manager.node.event.history.size";
    public static final String CLUSTER_MANAGER_NODE_API_CONNECTION_TIMEOUT = "nifi.cluster.manager.node.api.connection.timeout";
    public static final String CLUSTER_MANAGER_NODE_API_READ_TIMEOUT = "nifi.cluster.manager.node.api.read.timeout";
    public static final String CLUSTER_MANAGER_NODE_API_REQUEST_THREADS = "nifi.cluster.manager.node.api.request.threads";
    public static final String CLUSTER_MANAGER_FLOW_RETRIEVAL_DELAY = "nifi.cluster.manager.flow.retrieval.delay";
    public static final String CLUSTER_MANAGER_PROTOCOL_THREADS = "nifi.cluster.manager.protocol.threads";
    public static final String CLUSTER_MANAGER_SAFEMODE_DURATION = "nifi.cluster.manager.safemode.duration";

    // defaults
    public static final String DEFAULT_TITLE = "NiFi";
    public static final Boolean DEFAULT_AUTO_RESUME_STATE = true;
    public static final String DEFAULT_AUTHORITY_PROVIDER_CONFIGURATION_FILE = "conf/authority-providers.xml";
    public static final String DEFAULT_USER_CREDENTIAL_CACHE_DURATION = "24 hours";
    public static final Integer DEFAULT_REMOTE_INPUT_PORT = null;
    public static final Path DEFAULT_TEMPLATE_DIRECTORY = Paths.get("conf", "templates");
    public static final int DEFAULT_WEB_THREADS = 200;
    public static final String DEFAULT_WEB_WORKING_DIR = "./work/jetty";
    public static final String DEFAULT_NAR_WORKING_DIR = "./work/nar";
    public static final String DEFAULT_COMPONENT_DOCS_DIRECTORY = "./work/docs/components";
    public static final String DEFAULT_NAR_LIBRARY_DIR = "./lib";
    public static final String DEFAULT_FLOWFILE_REPO_PARTITIONS = "256";
    public static final String DEFAULT_FLOWFILE_CHECKPOINT_INTERVAL = "2 min";
    public static final int DEFAULT_MAX_FLOWFILES_PER_CLAIM = 100;
    public static final int DEFAULT_QUEUE_SWAP_THRESHOLD = 20000;
    public static final String DEFAULT_SWAP_STORAGE_LOCATION = "./flowfile_repository/swap";
    public static final String DEFAULT_SWAP_IN_PERIOD = "1 sec";
    public static final String DEFAULT_SWAP_OUT_PERIOD = "5 sec";
    public static final int DEFAULT_SWAP_IN_THREADS = 4;
    public static final int DEFAULT_SWAP_OUT_THREADS = 4;
    public static final String DEFAULT_ADMINISTRATIVE_YIELD_DURATION = "30 sec";
    public static final String DEFAULT_PERSISTENT_STATE_DIRECTORY = "./conf/state";
    public static final String DEFAULT_COMPONENT_STATUS_SNAPSHOT_FREQUENCY = "5 mins";
    public static final String DEFAULT_BORED_YIELD_DURATION = "10 millis";

    // cluster common defaults
    public static final String DEFAULT_CLUSTER_PROTOCOL_HEARTBEAT_INTERVAL = "5 sec";
    public static final String DEFAULT_CLUSTER_PROTOCOL_MULTICAST_SERVICE_BROADCAST_DELAY = "500 ms";
    public static final int DEFAULT_CLUSTER_PROTOCOL_MULTICAST_SERVICE_LOCATOR_ATTEMPTS = 3;
    public static final String DEFAULT_CLUSTER_PROTOCOL_MULTICAST_SERVICE_LOCATOR_ATTEMPTS_DELAY = "1 sec";
    public static final String DEFAULT_CLUSTER_PROTOCOL_SOCKET_TIMEOUT = "30 sec";
    public static final String DEFAULT_CLUSTER_PROTOCOL_CONNECTION_HANDSHAKE_TIMEOUT = "45 sec";

    // cluster node defaults
    public static final int DEFAULT_CLUSTER_NODE_PROTOCOL_THREADS = 2;

    // cluster manager defaults
    public static final int DEFAULT_CLUSTER_MANAGER_NODE_EVENT_HISTORY_SIZE = 10;
    public static final String DEFAULT_CLUSTER_MANAGER_NODE_API_CONNECTION_TIMEOUT = "30 sec";
    public static final String DEFAULT_CLUSTER_MANAGER_NODE_API_READ_TIMEOUT = "30 sec";
    public static final int DEFAULT_CLUSTER_MANAGER_NODE_API_NUM_REQUEST_THREADS = 10;
    public static final String DEFAULT_CLUSTER_MANAGER_FLOW_RETRIEVAL_DELAY = "5 sec";
    public static final int DEFAULT_CLUSTER_MANAGER_PROTOCOL_THREADS = 10;
    public static final String DEFAULT_CLUSTER_MANAGER_SAFEMODE_DURATION = "0 sec";

    private NiFiProperties() {
        super();
    }

    /**
     * This is the method through which the NiFiProperties object should be
     * obtained.
     *
     * @return the NiFiProperties object to use
     * @throws RuntimeException if unable to load properties file
     */
    public static synchronized NiFiProperties getInstance() {
        if (null == instance) {
            final NiFiProperties suspectInstance = new NiFiProperties();
            final String nfPropertiesFilePath = System
                    .getProperty(NiFiProperties.PROPERTIES_FILE_PATH);
            if (null == nfPropertiesFilePath || nfPropertiesFilePath.trim().length() == 0) {
                throw new RuntimeException("Requires a system property called \'"
                        + NiFiProperties.PROPERTIES_FILE_PATH
                        + "\' and this is not set or has no value");
            }
            final File propertiesFile = new File(nfPropertiesFilePath);
            if (!propertiesFile.exists()) {
                throw new RuntimeException("Properties file doesn't exist \'"
                        + propertiesFile.getAbsolutePath() + "\'");
            }
            if (!propertiesFile.canRead()) {
                throw new RuntimeException("Properties file exists but cannot be read \'"
                        + propertiesFile.getAbsolutePath() + "\'");
            }
            InputStream inStream = null;
            try {
                inStream = new BufferedInputStream(new FileInputStream(propertiesFile));
                suspectInstance.load(inStream);
            } catch (final Exception ex) {
                LOG.error("Cannot load properties file due to " + ex.getLocalizedMessage());
                throw new RuntimeException("Cannot load properties file due to "
                        + ex.getLocalizedMessage(), ex);
            } finally {
                if (null != inStream) {
                    try {
                        inStream.close();
                    } catch (final Exception ex) {
                        /**
                         * do nothing *
                         */
                    }
                }
            }
            instance = suspectInstance;
        }
        return instance;
    }

    // getters for core properties //
    public File getFlowConfigurationFile() {
        try {
            return new File(getProperty(FLOW_CONFIGURATION_FILE));
        } catch (Exception ex) {
            return null;
        }
    }

    public File getFlowConfigurationFileDir() {
        try {
            return getFlowConfigurationFile().getParentFile();
        } catch (Exception ex) {
            return null;
        }
    }

    private Integer getPropertyAsPort(final String propertyName, final Integer defaultValue) {
        final String port = getProperty(propertyName);
        if (StringUtils.isEmpty(port)) {
            return defaultValue;
        }
        try {
            final int val = Integer.parseInt(port);
            if (val <= 0 || val > 65535) {
                throw new RuntimeException("Valid port range is 0 - 65535 but got " + val);
            }
            return val;
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getQueueSwapThreshold() {
        final String thresholdValue = getProperty(QUEUE_SWAP_THRESHOLD);
        if (thresholdValue == null) {
            return DEFAULT_QUEUE_SWAP_THRESHOLD;
        }

        try {
            return Integer.parseInt(thresholdValue);
        } catch (final NumberFormatException e) {
            return DEFAULT_QUEUE_SWAP_THRESHOLD;
        }
    }

    public Integer getIntegerProperty(final String propertyName, final Integer defaultValue) {
        final String value = getProperty(propertyName);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(getProperty(propertyName));
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    public int getSwapInThreads() {
        return getIntegerProperty(SWAP_IN_THREADS, DEFAULT_SWAP_IN_THREADS);
    }

    public int getSwapOutThreads() {
        final String value = getProperty(SWAP_OUT_THREADS);
        if (value == null) {
            return DEFAULT_SWAP_OUT_THREADS;
        }

        try {
            return Integer.parseInt(getProperty(SWAP_OUT_THREADS));
        } catch (final Exception e) {
            return DEFAULT_SWAP_OUT_THREADS;
        }
    }

    public String getSwapInPeriod() {
        return getProperty(SWAP_IN_PERIOD, DEFAULT_SWAP_IN_PERIOD);
    }

    public String getSwapOutPeriod() {
        return getProperty(SWAP_OUT_PERIOD, DEFAULT_SWAP_OUT_PERIOD);
    }

    public String getAdministrativeYieldDuration() {
        return getProperty(ADMINISTRATIVE_YIELD_DURATION, DEFAULT_ADMINISTRATIVE_YIELD_DURATION);
    }

    /**
     * The socket port to listen on for a Remote Input Port.
     *
     * @return the remote input port
     */
    public Integer getRemoteInputPort() {
        return getPropertyAsPort(REMOTE_INPUT_PORT, DEFAULT_REMOTE_INPUT_PORT);
    }

    /**
     * @return False if property value is 'false'; True otherwise.
     */
    public Boolean isSiteToSiteSecure() {
        final String secureVal = getProperty(SITE_TO_SITE_SECURE, "true");

        if ("false".equalsIgnoreCase(secureVal)) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * Returns the directory to which Templates are to be persisted
     *
     * @return the template directory
     */
    public Path getTemplateDirectory() {
        final String strVal = getProperty(TEMPLATE_DIRECTORY);
        return (strVal == null) ? DEFAULT_TEMPLATE_DIRECTORY : Paths.get(strVal);
    }

    /**
     * Get the flow service write delay.
     *
     * @return The write delay
     */
    public String getFlowServiceWriteDelay() {
        return getProperty(WRITE_DELAY_INTERVAL);
    }

    /**
     * Returns whether the processors should be started automatically when the
     * application loads.
     *
     * @return Whether to auto start the processors or not
     */
    public boolean getAutoResumeState() {
        final String rawAutoResumeState = getProperty(AUTO_RESUME_STATE,
                DEFAULT_AUTO_RESUME_STATE.toString());
        return Boolean.parseBoolean(rawAutoResumeState);
    }

    /**
     * Returns the number of partitions that should be used for the FlowFile
     * Repository
     *
     * @return the number of partitions
     */
    public int getFlowFileRepositoryPartitions() {
        final String rawProperty = getProperty(FLOWFILE_REPOSITORY_PARTITIONS,
                DEFAULT_FLOWFILE_REPO_PARTITIONS);
        return Integer.parseInt(rawProperty);
    }

    /**
     * Returns the number of milliseconds between FlowFileRepository
     * checkpointing
     *
     * @return the number of milliseconds between checkpoint events
     */
    public String getFlowFileRepositoryCheckpointInterval() {
        return getProperty(FLOWFILE_REPOSITORY_CHECKPOINT_INTERVAL,
                DEFAULT_FLOWFILE_CHECKPOINT_INTERVAL);
    }

    /**
     * @return the restore directory or null if not configured
     */
    public File getRestoreDirectory() {
        final String value = getProperty(RESTORE_DIRECTORY);
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            return new File(value);
        }
    }

    /**
     * @return the user authorities file
     */
    public File getAuthorityProviderConfiguraitonFile() {
        final String value = getProperty(AUTHORITY_PROVIDER_CONFIGURATION_FILE);
        if (StringUtils.isBlank(value)) {
            return new File(DEFAULT_AUTHORITY_PROVIDER_CONFIGURATION_FILE);
        } else {
            return new File(value);
        }
    }

    /**
     * Will default to true unless the value is explicitly set to false.
     *
     * @return Whether client auth is required
     */
    public boolean getNeedClientAuth() {
        boolean needClientAuth = true;
        String rawNeedClientAuth = getProperty(SECURITY_NEED_CLIENT_AUTH);
        if ("false".equalsIgnoreCase(rawNeedClientAuth)) {
            needClientAuth = false;
        }
        return needClientAuth;
    }

    public String getUserCredentialCacheDuration() {
        return getProperty(SECURITY_USER_CREDENTIAL_CACHE_DURATION,
                DEFAULT_USER_CREDENTIAL_CACHE_DURATION);
    }

    public boolean getSupportNewAccountRequests() {
        boolean shouldSupport = true;
        String rawShouldSupport = getProperty(SECURITY_SUPPORT_NEW_ACCOUNT_REQUESTS);
        if ("false".equalsIgnoreCase(rawShouldSupport)) {
            shouldSupport = false;
        }
        return shouldSupport;
    }

    // getters for web properties //
    public Integer getPort() {
        Integer port = null;
        try {
            port = Integer.parseInt(getProperty(WEB_HTTP_PORT));
        } catch (NumberFormatException nfe) {
        }
        return port;
    }

    public Integer getSslPort() {
        Integer sslPort = null;
        try {
            sslPort = Integer.parseInt(getProperty(WEB_HTTPS_PORT));
        } catch (NumberFormatException nfe) {
        }
        return sslPort;
    }

    public int getWebThreads() {
        return getIntegerProperty(WEB_THREADS, DEFAULT_WEB_THREADS);
    }

    public File getWebWorkingDirectory() {
        return new File(getProperty(WEB_WORKING_DIR, DEFAULT_WEB_WORKING_DIR));
    }

    public File getComponentDocumentationWorkingDirectory() {
        return new File(getProperty(COMPONENT_DOCS_DIRECTORY, DEFAULT_COMPONENT_DOCS_DIRECTORY));
    }

    public File getNarWorkingDirectory() {
        return new File(getProperty(NAR_WORKING_DIRECTORY, DEFAULT_NAR_WORKING_DIR));
    }

    public File getFrameworkWorkingDirectory() {
        return new File(getNarWorkingDirectory(), "framework");
    }

    public File getExtensionsWorkingDirectory() {
        return new File(getNarWorkingDirectory(), "extensions");
    }

    public List<Path> getNarLibraryDirectories() {

        List<Path> narLibraryPaths = new ArrayList<>();

        // go through each property
        for (String propertyName : stringPropertyNames()) {
            // determine if the property is a nar library path
            if (StringUtils.startsWith(propertyName, NAR_LIBRARY_DIRECTORY_PREFIX)
                    || NAR_LIBRARY_DIRECTORY.equals(propertyName)) {
                // attempt to resolve the path specified
                String narLib = getProperty(propertyName);
                if (!StringUtils.isBlank(narLib)) {
                    narLibraryPaths.add(Paths.get(narLib));
                }
            }
        }

        if (narLibraryPaths.isEmpty()) {
            narLibraryPaths.add(Paths.get(DEFAULT_NAR_LIBRARY_DIR));
        }

        return narLibraryPaths;
    }

    // getters for ui properties //
    /**
     * Get the title for the UI.
     *
     * @return The UI title
     */
    public String getUiTitle() {
        return this.getProperty(VERSION, DEFAULT_TITLE);
    }

    /**
     * Get the banner text.
     *
     * @return The banner text
     */
    public String getBannerText() {
        return this.getProperty(UI_BANNER_TEXT, StringUtils.EMPTY);
    }

    /**
     * Returns the auto refresh interval in seconds.
     *
     * @return the interval over which the properties should auto refresh
     */
    public String getAutoRefreshInterval() {
        return getProperty(UI_AUTO_REFRESH_INTERVAL);
    }

    // getters for cluster protocol properties //
    public String getClusterProtocolHeartbeatInterval() {
        return getProperty(CLUSTER_PROTOCOL_HEARTBEAT_INTERVAL,
                DEFAULT_CLUSTER_PROTOCOL_HEARTBEAT_INTERVAL);
    }

    public String getNodeHeartbeatInterval() {
        return getClusterProtocolHeartbeatInterval();
    }

    public String getClusterProtocolSocketTimeout() {
        return getProperty(CLUSTER_PROTOCOL_SOCKET_TIMEOUT, DEFAULT_CLUSTER_PROTOCOL_SOCKET_TIMEOUT);
    }

    public String getClusterProtocolConnectionHandshakeTimeout() {
        return getProperty(CLUSTER_PROTOCOL_CONNECTION_HANDSHAKE_TIMEOUT,
                DEFAULT_CLUSTER_PROTOCOL_CONNECTION_HANDSHAKE_TIMEOUT);
    }

    public boolean getClusterProtocolUseMulticast() {
        return Boolean.parseBoolean(getProperty(CLUSTER_PROTOCOL_USE_MULTICAST));
    }

    public InetSocketAddress getClusterProtocolMulticastAddress() {
        try {
            String multicastAddress = getProperty(CLUSTER_PROTOCOL_MULTICAST_ADDRESS);
            int multicastPort = Integer.parseInt(getProperty(CLUSTER_PROTOCOL_MULTICAST_PORT));
            return new InetSocketAddress(multicastAddress, multicastPort);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid multicast address/port due to: " + ex, ex);
        }
    }

    public String getClusterProtocolMulticastServiceBroadcastDelay() {
        return getProperty(CLUSTER_PROTOCOL_MULTICAST_SERVICE_BROADCAST_DELAY);
    }

    public File getPersistentStateDirectory() {
        final String dirName = getProperty(PERSISTENT_STATE_DIRECTORY,
                DEFAULT_PERSISTENT_STATE_DIRECTORY);
        final File file = new File(dirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public int getClusterProtocolMulticastServiceLocatorAttempts() {
        try {
            return Integer
                    .parseInt(getProperty(CLUSTER_PROTOCOL_MULTICAST_SERVICE_LOCATOR_ATTEMPTS));
        } catch (NumberFormatException nfe) {
            return DEFAULT_CLUSTER_PROTOCOL_MULTICAST_SERVICE_LOCATOR_ATTEMPTS;
        }
    }

    public String getClusterProtocolMulticastServiceLocatorAttemptsDelay() {
        return getProperty(CLUSTER_PROTOCOL_MULTICAST_SERVICE_LOCATOR_ATTEMPTS_DELAY,
                DEFAULT_CLUSTER_PROTOCOL_MULTICAST_SERVICE_LOCATOR_ATTEMPTS_DELAY);
    }

    // getters for cluster node properties //
    public boolean isNode() {
        return Boolean.parseBoolean(getProperty(CLUSTER_IS_NODE));
    }

    public InetSocketAddress getClusterNodeProtocolAddress() {
        try {
            String socketAddress = getProperty(CLUSTER_NODE_ADDRESS);
            if (StringUtils.isBlank(socketAddress)) {
                socketAddress = "localhost";
            }
            int socketPort = getClusterNodeProtocolPort();
            return InetSocketAddress.createUnresolved(socketAddress, socketPort);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid node protocol address/port due to: " + ex, ex);
        }
    }

    public Integer getClusterNodeProtocolPort() {
        try {
            return Integer.parseInt(getProperty(CLUSTER_NODE_PROTOCOL_PORT));
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public int getClusterNodeProtocolThreads() {
        try {
            return Integer.parseInt(getProperty(CLUSTER_NODE_PROTOCOL_THREADS));
        } catch (NumberFormatException nfe) {
            return DEFAULT_CLUSTER_NODE_PROTOCOL_THREADS;
        }
    }

    public InetSocketAddress getClusterNodeUnicastManagerProtocolAddress() {
        try {
            String socketAddress = getProperty(CLUSTER_NODE_UNICAST_MANAGER_ADDRESS);
            if (StringUtils.isBlank(socketAddress)) {
                socketAddress = "localhost";
            }
            int socketPort = Integer
                    .parseInt(getProperty(CLUSTER_NODE_UNICAST_MANAGER_PROTOCOL_PORT));
            return InetSocketAddress.createUnresolved(socketAddress, socketPort);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid unicast manager address/port due to: " + ex, ex);
        }
    }

    // getters for cluster manager properties //
    public boolean isClusterManager() {
        return Boolean.parseBoolean(getProperty(CLUSTER_IS_MANAGER));
    }

    public InetSocketAddress getClusterManagerProtocolAddress() {
        try {
            String socketAddress = getProperty(CLUSTER_MANAGER_ADDRESS);
            if (StringUtils.isBlank(socketAddress)) {
                socketAddress = "localhost";
            }
            int socketPort = getClusterManagerProtocolPort();
            return InetSocketAddress.createUnresolved(socketAddress, socketPort);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid manager protocol address/port due to: " + ex, ex);
        }
    }

    public Integer getClusterManagerProtocolPort() {
        try {
            return Integer.parseInt(getProperty(CLUSTER_MANAGER_PROTOCOL_PORT));
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public File getClusterManagerNodeFirewallFile() {
        final String firewallFile = getProperty(CLUSTER_MANAGER_NODE_FIREWALL_FILE);
        if (StringUtils.isBlank(firewallFile)) {
            return null;
        } else {
            return new File(firewallFile);
        }
    }

    public int getClusterManagerNodeEventHistorySize() {
        try {
            return Integer.parseInt(getProperty(CLUSTER_MANAGER_NODE_EVENT_HISTORY_SIZE));
        } catch (NumberFormatException nfe) {
            return DEFAULT_CLUSTER_MANAGER_NODE_EVENT_HISTORY_SIZE;
        }
    }

    public String getClusterManagerNodeApiConnectionTimeout() {
        return getProperty(CLUSTER_MANAGER_NODE_API_CONNECTION_TIMEOUT,
                DEFAULT_CLUSTER_MANAGER_NODE_API_CONNECTION_TIMEOUT);
    }

    public String getClusterManagerNodeApiReadTimeout() {
        return getProperty(CLUSTER_MANAGER_NODE_API_READ_TIMEOUT,
                DEFAULT_CLUSTER_MANAGER_NODE_API_READ_TIMEOUT);
    }

    public int getClusterManagerNodeApiRequestThreads() {
        try {
            return Integer.parseInt(getProperty(CLUSTER_MANAGER_NODE_API_REQUEST_THREADS));
        } catch (NumberFormatException nfe) {
            return DEFAULT_CLUSTER_MANAGER_NODE_API_NUM_REQUEST_THREADS;
        }
    }

    public String getClusterManagerFlowRetrievalDelay() {
        return getProperty(CLUSTER_MANAGER_FLOW_RETRIEVAL_DELAY,
                DEFAULT_CLUSTER_MANAGER_FLOW_RETRIEVAL_DELAY);
    }

    public int getClusterManagerProtocolThreads() {
        try {
            return Integer.parseInt(getProperty(CLUSTER_MANAGER_PROTOCOL_THREADS));
        } catch (NumberFormatException nfe) {
            return DEFAULT_CLUSTER_MANAGER_PROTOCOL_THREADS;
        }
    }

    public String getClusterManagerSafeModeDuration() {
        return getProperty(CLUSTER_MANAGER_SAFEMODE_DURATION,
                DEFAULT_CLUSTER_MANAGER_SAFEMODE_DURATION);
    }

    public String getClusterProtocolManagerToNodeApiScheme() {
        final String isSecureProperty = getProperty(CLUSTER_PROTOCOL_IS_SECURE);
        if (Boolean.valueOf(isSecureProperty)) {
            return "https";
        } else {
            return "http";
        }
    }

    public InetSocketAddress getNodeApiAddress() {

        final String rawScheme = getClusterProtocolManagerToNodeApiScheme();
        final String scheme = (rawScheme == null) ? "http" : rawScheme;

        final String host;
        final Integer port;
        if ("http".equalsIgnoreCase(scheme)) {
            // get host
            if (StringUtils.isBlank(getProperty(WEB_HTTP_HOST))) {
                host = "localhost";
            } else {
                host = getProperty(WEB_HTTP_HOST);
            }
            // get port
            port = getPort();

            if (port == null) {
                throw new RuntimeException(String.format("The %s must be specified if running in a cluster with %s set to false.", WEB_HTTP_PORT, CLUSTER_PROTOCOL_IS_SECURE));
            }
        } else {
            // get host
            if (StringUtils.isBlank(getProperty(WEB_HTTPS_HOST))) {
                host = "localhost";
            } else {
                host = getProperty(WEB_HTTPS_HOST);
            }
            // get port
            port = getSslPort();

            if (port == null) {
                throw new RuntimeException(String.format("The %s must be specified if running in a cluster with %s set to true.", WEB_HTTPS_PORT, CLUSTER_PROTOCOL_IS_SECURE));
            }
        }

        return InetSocketAddress.createUnresolved(host, port);

    }

    /**
     * Returns the database repository path. It simply returns the value
     * configured. No directories will be created as a result of this operation.
     *
     * @return database repository path
     * @throws InvalidPathException If the configured path is invalid
     */
    public Path getDatabaseRepositoryPath() {
        return Paths.get(getProperty(REPOSITORY_DATABASE_DIRECTORY));
    }

    /**
     * Returns the flow file repository path. It simply returns the value
     * configured. No directories will be created as a result of this operation.
     *
     * @return database repository path
     * @throws InvalidPathException If the configured path is invalid
     */
    public Path getFlowFileRepositoryPath() {
        return Paths.get(getProperty(FLOWFILE_REPOSITORY_DIRECTORY));
    }

    /**
     * Returns the content repository paths. This method returns a mapping of
     * file repository name to file repository paths. It simply returns the
     * values configured. No directories will be created as a result of this
     * operation.
     *
     * @return file repositories paths
     * @throws InvalidPathException If any of the configured paths are invalid
     */
    public Map<String, Path> getContentRepositoryPaths() {
        final Map<String, Path> contentRepositoryPaths = new HashMap<>();

        // go through each property
        for (String propertyName : stringPropertyNames()) {
            // determine if the property is a file repository path
            if (StringUtils.startsWith(propertyName, REPOSITORY_CONTENT_PREFIX)) {
                // get the repository key
                final String key = StringUtils.substringAfter(propertyName,
                        REPOSITORY_CONTENT_PREFIX);

                // attempt to resolve the path specified
                contentRepositoryPaths.put(key, Paths.get(getProperty(propertyName)));
            }
        }
        return contentRepositoryPaths;
    }

    /**
     * Returns the provenance repository paths. This method returns a mapping of
     * file repository name to file repository paths. It simply returns the
     * values configured. No directories will be created as a result of this
     * operation.
     *
     * @return the name and paths of all provenance repository locations
     */
    public Map<String, Path> getProvenanceRepositoryPaths() {
        final Map<String, Path> provenanceRepositoryPaths = new HashMap<>();

        // go through each property
        for (String propertyName : stringPropertyNames()) {
            // determine if the property is a file repository path
            if (StringUtils.startsWith(propertyName, PROVENANCE_REPO_DIRECTORY_PREFIX)) {
                // get the repository key
                final String key = StringUtils.substringAfter(propertyName,
                        PROVENANCE_REPO_DIRECTORY_PREFIX);

                // attempt to resolve the path specified
                provenanceRepositoryPaths.put(key, Paths.get(getProperty(propertyName)));
            }
        }
        return provenanceRepositoryPaths;
    }

    public int getMaxFlowFilesPerClaim() {
        try {
            return Integer.parseInt(getProperty(MAX_FLOWFILES_PER_CLAIM));
        } catch (NumberFormatException nfe) {
            return DEFAULT_MAX_FLOWFILES_PER_CLAIM;
        }
    }

    public String getMaxAppendableClaimSize() {
        return getProperty(MAX_APPENDABLE_CLAIM_SIZE);
    }

    @Override
    public String getProperty(final String key, final String defaultValue) {
        final String value = super.getProperty(key, defaultValue);
        if (value == null) {
            return null;
        }

        if (value.trim().isEmpty()) {
            return defaultValue;
        }
        return value;
    }

    public String getBoredYieldDuration() {
        return getProperty(BORED_YIELD_DURATION, DEFAULT_BORED_YIELD_DURATION);
    }
}
