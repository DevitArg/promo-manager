package com.devit.promomanager.mongo.embedded;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.process.extract.UserTempNaming;

/**
 * Class to provide an embedded mongo connection.
 * Create your {@link org.springframework.data.mongodb.core.MongoTemplate}
 * using this {@link MongoClient}
 */
public class EmbeddedMongoConfiguration {

	private static MongoClient mongoClient;

	public synchronized static MongoClient getMongoClient() throws Exception {

		final Command command = Command.MongoD;

		final IFeatureAwareVersion version = de.flapdoodle.embed.mongo.distribution.Version.V3_3_1;

		if (mongoClient == null) {
			final MongodStarter runtime = MongodStarter.getInstance(new RuntimeConfigBuilder().defaults(command)
					.artifactStore(new ExtractedArtifactStoreBuilder()
							.defaults(command)
							.download(new DownloadConfigBuilder()
									.defaultsForCommand(command).build())
							.executableNaming(new UserTempNaming()))
					.build());
			MongodExecutable mongodExecutable = runtime.prepare(new MongodConfigBuilder().version(version).build());
			MongodProcess mongod = mongodExecutable.start();
			mongoClient = new MongoClient(new ServerAddress(mongod.getConfig().net().getServerAddress(),
					mongod.getConfig().net().getPort()));

		}

		return mongoClient;
	}

}