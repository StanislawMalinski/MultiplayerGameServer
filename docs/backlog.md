# Multiplayer Board Game Server — Product Backlog

## Scope assumptions (adjust as needed)
- POC target: **one concurrent match**, but code should be written keyed by `matchId` / `channel-per-match` so scaling to N matches later is additive, not a rewrite.
- One specific board game will be hardcoded for the POC (game engine genericization is future work, tracked separately).
- Two deployable services: **CRUD Service** (Spring Boot + Postgres) and **Stateful Service** (WebSocket + Redis).
- Redis used for pub/sub coordination between Lobby/Chat/Matchmaking; not yet used as durable match state store.
- Deployment target is Kubernetes, but K8s-specific concerns (affinity, autoscaling) are scoped to what's needed for a single-pod stateful service.

Priority key: **P0** = needed for POC to function at all · **P1** = important, do soon after · **P2** = nice-to-have / demonstrates depth · **P3** = explicitly deferred / future work

---

## Epic 0 — Foundations & Repo Setup
| # | Story | Priority | Status |
|---|---|---|---|
| 0.1 | Set up multi-module project structure (`crud-service`, `stateful-service`, shared `common` module for DTOs/events) | P0 | Done |
| 0.2 | Set up shared build tooling (Gradle/Maven, consistent Java version, linting/formatting) | P0 | Done |
| 0.3 | Docker Compose for local dev: Postgres, Redis, both services | P0 | Done |
| 0.4 | Basic CI pipeline (build + test on push) | P1 | In progress |
| 0.5 | README documenting architecture, the EventBus-vs-direct-Connection split, and known POC limitations | P1 | In progress |

---

## Epic 1 — CRUD Service: Users
| # | Story | Priority | Status |
|---|---|---|---|
| 1.1 | User entity + Postgres schema (id, username, email, created_at) | P0 | Done |
| 1.2 | Create user endpoint (POST /users) with validation | P0 | Done |
| 1.3 | Get user by id / list users endpoints | P0 |
| 1.4 | Update / delete user endpoints | P1 |
| 1.5 | Basic auth (e.g. simple token or session) so a websocket connection can be tied to a User identity | P0 |
| 1.6 | Password hashing / credential storage (if auth is username+password rather than external IdP) | P1 |

## Epic 2 — CRUD Service: Games & Match History
| # | Story | Priority |
|---|---|---|
| 2.1 | Game entity (represents a game *type*, e.g. "tic-tac-toe") + seed data | P0 |
| 2.2 | MatchRecord entity (players, game type, result, timestamps, move log) | P0 |
| 2.3 | Endpoint to persist a completed match (called by stateful service) | P0 |
| 2.4 | Endpoint(s) to query match history per user | P1 |
| 2.5 | PersistenceService abstraction wrapping match-write logic (single point the Ensurer calls into) | P0 |

---

## Epic 3 — Stateful Service: Connection Layer
| # | Story | Priority |
|---|---|---|
| 3.1 | WebSocket endpoint accepting authenticated connections (reuse User identity from Epic 1.5) | P0 |
| 3.2 | `Connection` abstraction wrapping the raw WebSocket session | P0 |
| 3.3 | Heartbeat / ping-pong to detect dead connections | P1 |
| 3.4 | Reconnection handling: swap a stale `Connection` for a new one inside an existing Match without losing state | P1 |
| 3.5 | Graceful shutdown: on SIGTERM/preStop, notify connected players / drain in-progress match | P2 |

## Epic 4 — Redis-backed Event Bus
| # | Story | Priority |
|---|---|---|
| 4.1 | Redis pub/sub wrapper replacing the in-memory EventBus | P0 |
| 4.2 | Define event schema/contracts (PlayerAvailable, MatchFound, MatchEnded, ChatMessage, etc.) in shared module | P0 |
| 4.3 | Namespace all channels/keys by `matchId` even though only one match exists (`match:{id}:events`) | P0 |
| 4.4 | Document explicitly: EventBus = coordination events, direct `Connection` = gameplay hot path | P1 |

## Epic 5 — Lobby & Matchmaking
| # | Story | Priority |
|---|---|---|
| 5.1 | Lobby component: players publish "available" event on connect | P0 |
| 5.2 | Matchmaking logic: pair two available players into a match | P0 |
| 5.3 | Lobby rejects/queues new players while POC's single match slot is occupied | P0 |
| 5.4 | Notify matched players (MatchFound event) with match details | P0 |
| 5.5 | Lobby designed as "produces matches" not "is the match" — clean handoff boundary to Epic 6 | P1 |

## Epic 6 — Match / Gameplay Engine
| # | Story | Priority |
|---|---|---|
| 6.1 | `Match` object holding game state + both players' `Connection` references | P0 |
| 6.2 | Game rules engine for the one chosen board game (legal move validation, turn order, win/draw detection) | P0 |
| 6.3 | Move submission → validation → broadcast to both connections | P0 |
| 6.4 | Per-match single-threaded execution model (executor/actor per Match) to avoid race conditions on concurrent moves | P1 |
| 6.5 | Match completion triggers `PersistenceEnsurer` call | P0 |
| 6.6 | In-memory move log kept for the duration of the match (feeds MatchRecord on completion) | P1 |

## Epic 7 — Persistence Bridge (Stateful → CRUD)
| # | Story | Priority |
|---|---|---|
| 7.1 | `PersistenceEnsurer` on stateful side: calls CRUD service's persistence endpoint when match ends | P0 |
| 7.2 | Retry with backoff on failed persistence calls | P1 |
| 7.3 | Fallback on exhausted retries (log / dead-letter to a Redis list) rather than silently dropping | P1 |
| 7.4 | *(Stretch)* Swap direct REST call for outbox-lite: publish "MatchCompleted" to Redis, consumed by CRUD service | P2 |

## Epic 8 — Chat
| # | Story | Priority |
|---|---|---|
| 8.1 | Chat message event type + routing to both players in a match | P1 |
| 8.2 | Basic profanity/length validation on chat messages | P2 |
| 8.3 | Chat history stored in-memory for match duration only (no persistence for POC) | P2 |

---

## Epic 9 — Kubernetes & Deployment
| # | Story | Priority |
|---|---|---|
| 9.1 | Dockerfiles for both services | P0 |
| 9.2 | K8s manifests (Deployment + Service) for crud-service, Postgres (or managed instance), Redis | P0 |
| 9.3 | K8s manifests for stateful-service (single replica for POC) | P0 |
| 9.4 | Ingress/Service config for WebSocket support (sticky-ish routing, even at replica=1) | P1 |
| 9.5 | ConfigMap/Secret management for DB creds, Redis connection info | P1 |
| 9.6 | *(Stretch)* HPA for crud-service only (stateless, trivially scalable) | P2 |

## Epic 10 — Observability
| # | Story | Priority |
|---|---|---|
| 10.1 | Structured logging across both services, correlated by `matchId` | P1 |
| 10.2 | Basic health/readiness endpoints for both services | P0 |
| 10.3 | Metrics (active connections, matches in progress, persistence failures) — even just via Actuator | P2 |

---

## Explicitly Deferred (P3 — future work, not POC)
- Multi-match support: `matchId -> podId` routing table in Redis, match registry/lookup structures
- Session affinity / load balancing across multiple stateful-service pods
- Generic/pluggable game rules engine supporting arbitrary board games
- N-player (>2) match support
- Durable in-progress match state (surviving pod restarts) via Redis or snapshotting
- ELO / skill-based matchmaking
- Outbox pattern with guaranteed delivery semantics

---

## Suggested first sprint (walking skeleton)
Goal: two hardcoded users can connect via WebSocket, get matched, play one full game of the chosen board game, and see the result persisted in Postgres.

1. 0.1, 0.3 — repo + docker-compose skeleton
2. 1.1–1.3, 1.5 — minimal User CRUD + auth stub
3. 2.1–2.3, 2.5 — Game/MatchRecord entities + persistence endpoint
4. 3.1–3.2 — WebSocket + Connection
5. 4.1–4.3 — Redis event bus, namespaced from day one
6. 5.1–5.4 — Lobby matches two players
7. 6.1–6.3, 6.5 — Match plays out, persists on completion
8. 7.1 — Ensurer calls CRUD service

Everything else layers on top once this path works end-to-end.