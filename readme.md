# Multiplayer Board Game Server

A proof-of-concept multiplayer server for turn-based board games, built to explore a realistic split between a **stateless CRUD backend** and a **engine, low-latency realtime backend** — the same shape used by real matchmaking/game server systems.

The POC supports **one concurrent match**, but the internals (event namespacing, match identity, connection routing) are deliberately written as if multiple concurrent matches already existed, so scaling from 1 → N matches later is additive rather than a rewrite.

---

## Architecture

The system is split into two independently deployable services that own different concerns:

```
                        ┌───────────────────────┐
                        │   crud-service        │
                        │   (Spring Boot)       │────────►  PostgreSQL
                        │                       │           (via crud-service only)
                        │  Users, Games,        │
                        │  Match history        │
                        └──────────┬────────────┘
                                   │ REST
                                   │ (persist match on completion)
                                   │
┌───────────────┐   WebSocket   ┌────────────────────────┐        ┌─────────┐
│   Player A    │◄─────────────►│                        │◄──────►│ Redis   │
│   Player B    │◄─────────────►│engine-service        │        │(pub/sub)│
└───────────────┘               │  Connection Layer      │        └─────────┘
                                │  Lobby / Matchmaking   │
                                │  Match (gameplay)      │
                                │  Chat                  │
                                └────────────────────────┘
                           
                                     
```

### `crud-service`
Stateless, standard REST CRUD. Owns all persistent data. Nothing else is allowed to write to Postgres directly — the engine service always goes through this service's API.

- **Users** — accounts, basic auth
- **Games** — game type metadata
- **MatchRecord** — completed match results, move logs

### `engine-service`
engine, in-memory, latency-sensitive. Owns live gameplay.

- **Connection layer** — wraps each player's WebSocket session; supports reconnection by swapping the `Connection` inside a live `Match`
- **Redis Event Bus** — pub/sub used for *coordination* events (player availability, match found, chat). All channels/keys are namespaced by `matchId` from day one, even though the POC only ever has one match.
- **Lobby** — matches available players and hands off to a new `Match`; deliberately kept separate from the `Match` itself (produces matches, isn't one)
- **Match** — holds game state and *direct* `Connection` references for both players. This is intentional: the event bus handles coordination, but gameplay moves go straight through the connection for minimum latency.
- **PersistenceEnsurer** — on match completion, calls `crud-service` to persist the result, with retry/backoff on failure

See [`docs/backlog.md`](./backlog.md) for the full task breakdown and current scope.

---

## Design decisions worth knowing

- **EventBus vs. direct Connection.** These are two deliberately different communication paths, not an inconsistency: the event bus (Redis pub/sub) is for low-frequency coordination (lobby, matchmaking, chat); `Match` talks to `Connection` objects directly for the gameplay hot path. This is documented explicitly because it's the one part of the design that could otherwise look accidental.
- **matchId everywhere.** Even with capacity for exactly one match, every event, channel, and log line is keyed by `matchId`. Capacity is capped at 1; identity/keying is not — that's what keeps a future multi-match upgrade from being a rewrite.
- **Concurrency per match.** Each `Match` is intended to run against a single-threaded executor (actor-style) rather than relying on scattered `synchronized` blocks, to avoid race conditions when both players submit moves near-simultaneously.
- **Persistence ownership.** `crud-service` is the only writer to Postgres. `engine-service` never touches the DB directly — it calls `crud-service`'s API via `PersistenceEnsurer` when a match ends.

---

## Known POC limitations (intentional, not oversights)

- Single concurrent match only; no `matchId → pod` routing yet
- Single replica of `engine-service`; no session affinity / load balancing across pods yet
- In-memory match state only — a pod restart loses any in-progress match
- One hardcoded board game; rules engine is not yet generic/pluggable
- 2-player matches only, no N-player support
- Chat is not persisted

These are tracked as explicit future work in the backlog, not silent gaps.

---

## Tech stack

| Layer | Choice |
|---|---|
| Languages  | Java, C++|
| Framework | Spring Boot |
| Database | PostgreSQL |
| Realtime transport | WebSocket |
| Coordination / pub-sub | Redis |
| Deployment target | Kubernetes |
| Local dev | Docker Compose |

---

## Project structure

```
.
├── crud-service/           # Spring Boot REST API (Users, Games, MatchRecord)
├── engine-service/         # WebSocket server (Connection, Lobby, Match, Chat)
├── docs/
│   └── backlog.md
├── docker-compose.yml
└── k8s/                     # Deployment manifests
```

---

## Getting started

### Prerequisites
- Java 21+
- Docker & Docker Compose
- (Optional) `kubectl` + a local cluster (kind/minikube) for K8s manifests

### Run locally

### Running everything via Docker Compose
---

## Roadmap

See [`docs/backlog.md`](./backlog.md) for the full backlog, priorities, and the suggested first-sprint "walking skeleton" (two players connect, get matched, play a full game, result gets persisted).