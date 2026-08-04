# stateful-service (C++)

Hello-world scaffold for a C++ implementation of the stateful/realtime service, with a working build + test setup so real functionality (WebSocket handling, Lobby, Match, etc.) can be added incrementally.

## Layout

```
.
├── Makefile
├── src/
│   ├── hello.hpp     # example lib function
│   ├── hello.cpp
│   └── main.cpp       # app entry point
├── tests/
│   └── test_hello.cpp # doctest-based unit tests
└── third_party/
    └── doctest.h       # vendored single-header test framework (MIT)
```

`src/hello.{hpp,cpp}` is a placeholder to prove the toolchain, test framework, and Makefile all work end-to-end — swap it out as the real Connection/Lobby/Match classes get built.

## Requirements

- g++ with C++17 support (or clang++, adjust `CXX` in the Makefile)
- GNU Make

No package manager or internet access needed to build — [doctest](https://github.com/doctest/doctest) is vendored as a single header in `third_party/`.

## Usage

```bash
make run     # build and run the app
make test    # build and run the test suite
make build   # just build the app binary (out/stateful-service)
make clean   # remove build artifacts
```

## Notes

- Tests and the app share everything in `src/` except `main.cpp`, so library code only needs to be written once.
- Build artifacts go to `out/`, kept out of version control (add `out/` to `.gitignore`).
