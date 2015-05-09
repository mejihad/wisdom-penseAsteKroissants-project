FROM dockerfile/java:oracle-java8

# Add the penseAsteKroissants distribution

COPY penseAsteKroissants-web*.zip /wisdom/penseAsteKroissants/

RUN cd /wisdom/penseAsteKroissants/ \
&& unzip penseAsteKroissants-web*.zip \
&& rm penseAsteKroissants-web*.zip


# Expose the port 9000

EXPOSE 9000

# Change workdir.

WORKDIR /wisdom/penseAsteKroissants

RUN touch /wisdom/penseAsteKroissants.log

# For easier handling, we dump the log, so `docker logs containerId` displays
# the log.
RUN chmod +x chameleon.sh

CMD export JVM_ARGS="-Dapplication.configuration=conf/application-prod.conf";./chameleon.sh start; tail -F logs/penseAsteKroissants.log