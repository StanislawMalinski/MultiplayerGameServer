# syntax=docker/dockerfile:1

# ---- Build stage -------------------------------------------------------
FROM debian:bookworm-slim AS build

RUN apt-get update && apt-get install -y --no-install-recommends \
        g++ \
        make \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace
COPY Makefile .
COPY src src
COPY third_party third_party

# Build only the app binary (tests are run in CI, not baked into the image).
RUN make build

# ---- Runtime stage -------------------------------------------------------
FROM debian:bookworm-slim AS runtime

# libstdc++6 is needed at runtime even though we don't need the full g++ toolchain.
RUN apt-get update && apt-get install -y --no-install-recommends \
        libstdc++6 \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system app && useradd --system --gid app app

WORKDIR /app
COPY --from=build /workspace/out/engine-service .

USER app:app
EXPOSE 8081

ENTRYPOINT ["./engine-service"]